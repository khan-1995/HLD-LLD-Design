// Element interface
interface Shape {
    void accept(ShapeVisitor visitor);
}

// Concrete element classes
class Circle implements Shape {
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}

class Square implements Shape {
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}

// Visitor interface
interface ShapeVisitor {
    void visit(Circle circle);
    void visit(Square square);
}

// Concrete Visitor classes
class AreaVisitor implements ShapeVisitor {
    @Override
    public void visit(Circle circle) {
        System.out.println("Calculating area of circle");
        // Calculate area of circle
    }

    @Override
    public void visit(Square square) {
        System.out.println("Calculating area of square");
        // Calculate area of square
    }
}

class PerimeterVisitor implements ShapeVisitor {
    @Override
    public void visit(Circle circle) {
        System.out.println("Calculating perimeter of circle");
        // Calculate perimeter of circle
    }

    @Override
    public void visit(Square square) {
        System.out.println("Calculating perimeter of square");
        // Calculate perimeter of square
    }
}

public class Main {
    public static void main(String[] args) {
        // Create shapes
        Shape circle = new Circle();
        Shape square = new Square();

        // Create visitors
        ShapeVisitor areaVisitor = new AreaVisitor();
        ShapeVisitor perimeterVisitor = new PerimeterVisitor();

        // Calculate area of shapes
        circle.accept(areaVisitor);
        square.accept(areaVisitor);

        // Calculate perimeter of shapes
        circle.accept(perimeterVisitor);
        square.accept(perimeterVisitor);
    }
}

/*In this example, the Visitor pattern allows us to add new operations (e.g., calculating area, perimeter) to existing object structures (e.g., Circle, Square) without modifying their classes. 
We can simply create new Visitor classes implementing the ShapeVisitor interface and pass them to the accept method of each shape object. 
This promotes code extensibility and maintainability, as we can easily add new operations without impacting existing code.*/
