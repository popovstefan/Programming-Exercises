package np.clusters;

import java.util.*;

abstract class Element<T> {
    private long id;

    Element(long id) {
        this.id = id;
    }

    abstract double distance(T other);

    long getId() {
        return id;
    }
}


class Point2D extends Element<Point2D> {

    private float x;
    private float y;

    Point2D(long id, float x, float y) {
        super(id);
        this.x = x;
        this.y = y;
    }

    @Override
    double distance(Point2D that) {
        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
    }
}

class Cluster<T extends Element> {
    private Map<Long, T> items;

    Cluster() {
        items = new HashMap<>();
    }

    void addItem(T element) {
        items.put(element.getId(), element);
    }

    void near(long id, int top) {
        final T item = items.get(id);


        List<T> elements = new ArrayList<>(items.values());
        elements.sort(Comparator.comparingDouble(element -> element.distance(item)));

        elements = elements.subList(0, top + 1);

        for (int i = 1; i < elements.size(); i++)
            System.out.printf("%d. %d -> %.3f\n", i, elements.get(i).getId(), elements.get(i).distance(item));
    }
}


public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}