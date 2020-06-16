package np.shape;

import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

abstract class Shape implements Scalable, Stackable, Comparable<Shape> {
    private String id;
    private Color color;

    Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Shape that) {
        int descendingWeightCompare = -Float.compare(this.weight(), that.weight());
        if (descendingWeightCompare != 0)
            return descendingWeightCompare;
        return this.id.compareTo(that.id);
    }

    @Override
    public String toString() {
        return String.format("%-5s%-10s%10.2f\n", id, color, weight());
    }
}

class Circle extends Shape {

    float radius;

    Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return radius * radius * (float) Math.PI;
    }

    @Override
    public String toString() {
        return "C: " + super.toString();
    }
}

class Rectangle extends Shape {

    float width;
    float height;

    Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return "R: " + super.toString();
    }
}

class Canvas {

    TreeSet<Shape> shapes;

    Canvas() {
        shapes = new TreeSet<>();
    }

    void add(String id, Color color, float radius) {
        shapes.add(new Circle(id, color, radius));
    }

    void add(String id, Color color, float width, float height) {
        shapes.add(new Rectangle(id, color, width, height));
    }

    void scale(String id, float scaleFactor) {
        TreeSet<Shape> tmp = new TreeSet<>();
        for (Shape shape : shapes) {
            if (shape.getId().equals(id))
                shape.scale(scaleFactor);
            tmp.add(shape);
        }
        shapes = tmp;
    }

    @Override
    public String toString() {
        return shapes.stream()
                .map(Shape::toString)
                .collect(Collectors.joining());
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}