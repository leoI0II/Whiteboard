```mermaid
classDiagram
    namespace org.model {
        namespace base {
            class ARGBColor {
                +int alpha
                +int red
                +int green
                +int blue
            }

            class Point {
                +double x
                +double y
            }

            class BrushContext {
                -double thickness
                -ARGBColor color
                +double getThickness()
                +void setThickness(double)
                +ARGBColor getColor()
                +void setColor(ARGBColor)
            }

            class Stroke {
                -List~Point~ points
                -double thickness
                -ARGBColor color
                -BoundingBox boundingBox
                +void addPoint(Point)
                +List~Point~ getPoints()
                +double getThickness()
                +void setThickness(float)
                +ARGBColor getColor()
                +void setColor(ARGBColor)
            }
        }

        namespace utils {
            class BoundingBox {
                -double topLeftX
                -double topLeftY
                -double bottomRightX
                -double bottomRightY
            }

            class RectangleBBox {
                -double x
                -double y
                -double width
                -double height
                +BoundingBox toBoundingBox()
                +boolean intersects(BoundingBox)
                +boolean contains(Point)
            }
        }

        namespace interfaces {
            interface Drawable {
                <<interface>>
                +void render()
            }

            interface Erasable {
                <<interface>>
            }

            abstract class Controller {
                #boolean isPressed
                #double lastX
                #double lastY
                +void handleMousePressed(double, double)
                +void handleMouseDragged(double, double)
                +void handleMouseReleased(double, double)
                #void onMousePressed(double, double)*
                #void onMouseDragged(double, double)*
                #void onMouseReleased(double, double)*
            }
        }

        class DrewPool {
            -Stack~Drawable~ drewObjects
            -Stack~Drawable~ temporaryRemovedDrawables
            +void addObject(Drawable)
            +void removeObject(Drawable)
            +void undo()
            +void redo()
        }

        class Viewport {
            -double offsetX
            -double offsetY
            -double zoom
            +Point getOffset()
            +void setOffset(double, double)
            +double getZoom()
            +void setZoom(double)
        }
    }

    namespace org.Controller {
        class PenController {
            -Stroke currentStroke
            -DrewPool strokePool
            -Viewport viewport
            -BrushContext context
        }

        class PanController {
            -Viewport viewport
        }

        class EraserController
    }


    org.model.base.Stroke ..|> org.model.interfaces.Drawable
    org.model.base.Stroke ..|> org.model.interfaces.Erasable
    org.model.base.Stroke *-- org.model.base.Point
    org.model.base.Stroke *-- org.model.base.ARGBColor
    org.model.base.Stroke *-- org.model.utils.BoundingBox
    org.model.base.BrushContext *-- org.model.base.ARGBColor
    
    org.model.utils.BoundingBox *-- org.model.base.Point
    org.model.utils.RectangleBBox *-- org.model.base.Point
    org.model.utils.RectangleBBox ..> org.model.utils.BoundingBox

    org.model.DrewPool *-- org.model.interfaces.Drawable

    org.model.Viewport *-- org.model.base.Point

    org.Controller.PenController --|> org.model.interfaces.Controller
    org.Controller.PenController o-- org.model.DrewPool
    org.Controller.PenController o-- org.model.Viewport
    org.Controller.PenController o-- org.model.base.BrushContext
    org.Controller.PenController ..> org.model.base.Stroke
    
    org.Controller.PanController --|> org.model.interfaces.Controller
    org.Controller.PanController o-- org.model.Viewport

    org.Controller.EraserController --|> org.model.interfaces.Controller
```
