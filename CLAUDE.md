# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Run the application
./gradlew run

# Run tests
./gradlew test

# Clean
./gradlew clean
```

**Stack:** Java 21 (managed via SDKman at `~/.sdkman/candidates/java/21.0.10-tem`), JavaFX 21.0.1 (modules: controls, fxml, graphics), JUnit Jupiter 5, Guava. Main class: `org.app.Launcher`.

A Mermaid class diagram of the full model/controller hierarchy is maintained in [whiteboard-uml.md](whiteboard-uml.md).

---

## Architecture

### Package Overview

| Package | Role |
|---|---|
| `org.app.jfx` | Entry point (`Launcher` → `MainApp`) — JavaFX Application lifecycle, canvas setup, session wiring |
| `org.Controller` | Tool controllers, routing, and session-level settings |
| `org.model` | Core state: `DrewPool` (drawable collection + undo/redo), `Viewport` (camera) |
| `org.model.base` | Drawing primitives: `Stroke`, `Eraser`, `ARGBColor`, `Point`, context objects |
| `org.model.base.context` | `BrushContext` (thickness + color), `EraserContext` (radius) |
| `org.model.utils` | `BoundingBox`, `RectangleBBox` — AABB collision helpers |
| `org.model.interfaces` | Abstract `Controller`, `Drawable`, `Erasable`, `Selectable`, observer interfaces |
| `org.view.jfx` | `BoardRenderer` (Visitor implementation), `ToolBarBuilder` |
| `org.view.interfaces` | `RendererVisitor` interface |

---

### Entry Point and Wiring

`Launcher.main()` → `MainApp.start()` creates:
- Two stacked `Canvas` nodes bound to the window size:
  - `mainCanvas` — strokes and selection highlights
  - `cursorCanvas` — tool cursor overlay (mouse-transparent, always on top)
- `WhiteboardSession` — owns all model/controller/renderer instances and wires them together

`WhiteboardSession` initialises:
- `Viewport(0, 0, 1.0)` — camera starts at origin, zoom 1
- `DrewPool` — empty stack
- `Eraser` with `EraserContext` (default radius = 5)
- `ContextSetting` — shared `BrushContext` (thickness=10, black) + `EraserContext`
- `ToolsController` built via `ToolsControllerBuilder.buildStandardToolset()`
- `BoardRenderer` pointed at the `Viewport` and `ContextSetting`

Setup wires:
- Mouse events on `mainCanvas` → `ToolsController.handle*()`
- Scroll / pinch-zoom → `Viewport` zoom with focal-point correction
- `Ctrl+Z` / `Ctrl+Y` → `DrewPool.undo()` / `redo()`
- Tool-changed observer → updates cursor, sets `cursorRendererRunnable` and `selectionRendererRunnable`

---

### Event Flow

```
Mouse event → Canvas handler (WhiteboardSession)
  → ToolsController.handle*(x, y)
    → activeController.handle*(x, y)          ← Template Method dispatch
      → PenController / EraserController / PanController / SelectController
        → mutates DrewPool or Viewport
  → redrawCanvas()                             ← always after press/drag/release
    → BoardRenderer.render(drewPool)           ← visits each Drawable
    → selectionRendererRunnable.run()          ← draws selection box + highlights

Mouse moved → redrawCursorCanvas()
    → cursorRendererRunnable.run()             ← draws eraser circle (if ERASER tool)
```

---

### Coordinate System

`Viewport` is the single source of truth for the world↔screen transform:

```
screenToWorldX(sx)  =  sx / zoom + offsetX
worldToScreenX(wx)  =  (wx - offsetX) * zoom
```

**All drawables store their data in world coordinates.** `BoardRenderer` converts to screen on every render pass. Never store screen coordinates in model objects.

Zoom-invariant stroke thickness: `PenController` stores `thickness / zoom` at the moment of stroke creation. When rendered, `lineWidth = storedThickness * zoom`, which gives a constant visual pixel size regardless of zoom level.

Scroll zoom with focal-point correction (in `WhiteboardSession.setupGestures()`):
```java
double worldX = (mouseX / oldZoom) + viewport.getOffsetX();
double worldY = (mouseY / oldZoom) + viewport.getOffsetY();
viewport.setZoom(newZoom);
viewport.setOffset(worldX - mouseX / newZoom, worldY - mouseY / newZoom);
```
This keeps the pixel under the cursor fixed as you zoom.

---

### Key Design Patterns

- **Template Method** — `org.model.interfaces.Controller` defines `handleMousePressed/Dragged/Released/Moved`; subclasses implement `onMousePressed/Dragged/Released/Moved`.
- **Visitor** — `Drawable.acceptRenderer(RendererVisitor)` + `BoardRenderer` implements `RendererVisitor`. Add all rendering logic to `BoardRenderer.visit()`, never inside model classes.
- **Observer** — `HistoryObserver` (undo/redo button state), `ToolChangedObserver` (cursor/runnable update). Registered on `DrewPool` / `ToolsController`.
- **Strategy** — Each `Controller` subclass is a tool strategy. `ToolsController.setActiveTool()` swaps the active strategy.
- **Builder** — `ToolsControllerBuilder.buildStandardToolset()` assembles all controllers in one place.

---

### Model Classes in Detail

#### `DrewPool`
- `drewObjects: Stack<Drawable>` — the current canvas state
- `temporaryRemovedDrawables: Stack<Drawable>` — redo buffer
- `addObject()` clears the redo buffer (linear undo)
- `undo()` pops from `drewObjects` → pushes to redo stack
- `redo()` pops from redo stack → pushes to `drewObjects`
- `removeObject()` (used by eraser) pushes removed item to redo stack
- `clear()` moves everything to redo stack
- Notifies `HistoryObserver` after every mutation

#### `Stroke` — implements `Drawable`, `Erasable`, `Selectable`
- `points: List<Point>` — world-coordinate path
- `context: BrushContext` — a **copy** made at stroke creation (zoom-adjusted thickness, color at draw time)
- `boundingBox: BoundingBox` — updated incrementally as points are added
- `intersectsEraser(cx, cy, radius)` — iterates segments, checks each with `lineIntersectsCircle()` (includes half-stroke-thickness in the radius test)
- `intersects(RectangleBBox)` — AABB fast-reject, then point-in-box test, then segment–edge crossing test
- `move(deltaX, deltaY)` — shifts all points and the bounding box

#### `Eraser` — implements `Drawable`
- Just a mutable position `(pointX, pointY)` + reference to `EraserContext`
- `acceptRenderer()` calls `visitor.visit(this)` → `BoardRenderer` draws the cursor circle

#### `Viewport`
- `offsetX/Y: double` — world coordinates of the top-left corner of the screen
- `zoom: double` — pixels per world unit (1.0 = 1:1)
- Getters/setters + `setOffset(x, y)` for atomic pan

#### `BoundingBox` vs `RectangleBBox`
- `BoundingBox(tlX, tlY, brX, brY)` — AABB stored as two corners; default constructed with ±∞ so `updateBoundingBox` works correctly on the first point
- `RectangleBBox(x, y, width, height)` — origin + size form used for the selection rectangle drag; `toBoundingBox()` converts for intersection tests

---

### Controller Classes in Detail

#### `PenController`
On press: copies `BrushContext`, divides `thickness / zoom`, creates new `Stroke`, adds to pool, calls `onMouseDragged` immediately so a single-click dot is recorded.  
On drag: `viewport.screenToWorld(x, y)` → `stroke.addPoint()`.  
On release: clears `currentStroke` reference.

#### `EraserController`
On press: snapshots current `Erasable` items from pool into `contextItems`.  
On drag: moves `eraser` to world position, computes AABB, iterates `contextItems` checking AABB then `intersectsEraser()`, collects matches in `itemsToRemove`, then removes them (two-pass to avoid ConcurrentModificationException).  
On move (no click): updates eraser position for cursor rendering only.

#### `PanController`
On drag: delta in screen pixels → divide by zoom → subtract from `viewport.offset`.

#### `SelectController`
On press: converts press position to world. If it falls inside any previously selected item's bbox → enters **move mode**. Otherwise re-initialises selection context.  
On drag (selection mode): updates `selectionBox` (world rect), tests all `contextItems.intersects(selectionBox)`, adds/removes from `selectedItems` live.  
On drag (move mode): `deltaX/Y` in world coords → calls `item.move()` on each selected item.  
On release: nullifies `selectionBox`, exits move mode, clears `contextItems`.  
`clearSelection()` is called by `ToolsControllerBuilder` whenever a non-SELECTION tool is activated.

#### `ToolsController`
Routes all `handle*()` calls to `activeController`. Notifies `ToolChangedObserver` list on tool change.

---

### View Classes in Detail

#### `BoardRenderer`
Holds `gc`, `viewport`, `contextSetting` (injected after construction).  
`render(DrewPool)` — iterates `drewPool.getDrewObjects()` and calls `acceptRenderer(this)` on each.  
`visit(Stroke)` — sets stroke color (ARGB→JavaFX Color), sets `lineWidth = thickness * zoom`, renders path in screen coordinates with ROUND cap/join.  
`visit(Eraser)` — draws unfilled circle at `worldToScreen(eraser.point)` with `radius * zoom`.  
`renderSelectionBox(RectangleBBox)` — dashed blue rectangle in screen space.  
`renderSelectedItemHighlight(List<Selectable>)` — solid blue bounding rect per item, expanded by `halfBrushThickness`.

#### `ToolBarBuilder`
Builds the `HBox` toolbar: ToggleGroup with Pen / Eraser / Pan / Select buttons + Undo / Redo / Clear action buttons.  
Undo/Redo disabled state driven by `DrewPool.HistoryObserver`.  
Registers a second `HistoryObserver` that clears the selection whenever the history changes.

---

### Undo / Redo Behaviour

- **Add stroke** → clears redo stack
- **Erase stroke(s)** → each erased item pushed to redo stack individually
- **Undo** → removes top of draw stack, pushes to redo
- **Redo** → removes top of redo stack, pushes back to draw stack
- **Clear** → all items moved to redo stack (so Undo restores them one at a time)
- Move (SelectController) is **not undoable** yet — it mutates point coordinates in place

---

### Erasing — Collision Pipeline

```
1. Broad phase: eraser BoundingBox vs Stroke.getBoundingBox()  (fast AABB)
2. Narrow phase: Stroke.intersectsEraser(cx, cy, radius)
   → for each segment (p1, p2):
      project eraser centre onto segment → clamp t ∈ [0,1]
      distance to closest point on segment
      targetRadius = eraserRadius + strokeThickness / 2
      hit if distanceSquared ≤ targetRadius²
```

---

## Unimplemented Tools

`Tools` enum defines: PEN, ERASER, HAND, SELECTION, SHAPE, TEXT, COLOR_PICKER, ZOOM.

| Tool | Status |
|---|---|
| PEN | Fully implemented |
| ERASER | Fully implemented |
| HAND | Fully implemented |
| SELECTION | Implemented (drag select, move, live deselect) |
| SHAPE | No controller, no renderer |
| TEXT | No controller, no renderer |
| COLOR_PICKER | No controller |
| ZOOM | No controller (scroll zoom is handled directly in `WhiteboardSession`) |

To add a new tool:
1. Add entry to `Tools` enum
2. Create a `Controller` subclass
3. Register it in `ToolsControllerBuilder`
4. Add a `visit()` override in `RendererVisitor` + `BoardRenderer` if a new `Drawable` type is needed
5. Add a toolbar button in `ToolBarBuilder`
6. Handle cursor / render runnable in `WhiteboardSession.setupObservers()`
