package np.iminja;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Name implements Comparable<Name> {
    private String name;
    private int occurrences;

    Name(String name) {
        this.name = name;
    }

    int getOccurrences() {
        return occurrences;
    }

    String getName() {
        return name;
    }

    void recordOccurrence() {
        occurrences++;
    }

    private long uniqueLetters() {
        return name.toLowerCase()
                .chars()
                .distinct()
                .count();
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, occurrences, uniqueLetters());
    }

    @Override
    public int compareTo(Name that) {
        return Comparator.comparing(Name::getName)
                .compare(this, that);
    }
}

class Names {
    private HashMap<String, Name> names;

    Names() {
        names = new HashMap<>();
    }

    void addName(String name) {
        names.computeIfAbsent(name, Name::new);
        names.computeIfPresent(name, (key, value) -> {
            value.recordOccurrence();
            return value;
        });
    }

    private Predicate<Name> byOccurrences(int n) {
        return nameObj -> nameObj.getOccurrences() >= n;
    }

    void printN(int n) {
        names.values()
                .stream()
                .filter(byOccurrences(n))
                .sorted()
                .forEach(System.out::println);
    }

    String findName(int len, int x) {
        List<Name> filteredNames = names.values()
                .stream()
                .filter(it -> it.getName().length() >= len)
                .sorted()
                .collect(Collectors.toList());

        x %= filteredNames.size();

        return filteredNames.get(x)
                .getName();
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}