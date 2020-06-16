package np.discount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Product {
    private int discountPrice;
    private int price;

    Product(int discountPrice, int price) {
        this.discountPrice = discountPrice;
        this.price = price;
    }

    int discountInPercentage() {
        return (int) ((price - discountPrice * 1.0) / price * 100);
    }

    int totalDiscount() {
        return price - discountPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", discountInPercentage(), discountPrice, price);
    }
}

class Store {
    private String name;
    private List<Product> products;

    Store(String name) {
        this.name = name;
        products = new LinkedList<>();
    }

    void add(Product product) {
        this.products.add(product);
    }

    String getName() {
        return name;
    }

    double averageDiscountPercentage() {
        return products.stream()
                .mapToInt(Product::discountInPercentage)
                .average()
                .orElse(0);
    }

    int totalDiscount() {
        return products.stream()
                .mapToInt(Product::totalDiscount)
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("%s\n", name));

        stringBuilder.append(String.format("Average np.discount: %.1f%%\n", averageDiscountPercentage()));

        stringBuilder.append(String.format("Total np.discount: %d\n", totalDiscount()));

        String productString = products.stream()
                .sorted(Comparator.comparing(Product::discountInPercentage)
                        .thenComparing(Product::totalDiscount)
                        .reversed())
                .map(Product::toString)
                .collect(Collectors.joining("\n"));

        stringBuilder.append(productString);

        return stringBuilder.toString();
    }
}

class Discounts {

    List<Store> stores;

    Discounts() {
        stores = new LinkedList<>();
    }


    int readStores(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        bufferedReader.lines().forEach(line -> {
            String[] storeParts = line.split("\\s+");
            Store store = new Store(storeParts[0]);

            for (int i = 1; i < storeParts.length; i++) {
                String[] productParts = storeParts[i].split(":");
                store.add(new Product(Integer.parseInt(productParts[0]), Integer.parseInt(productParts[1])));
            }
            stores.add(store);
        });

        return stores.size();
    }

    List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscountPercentage)
                        .reversed()
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }

    List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount)
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average np.discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total np.discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}