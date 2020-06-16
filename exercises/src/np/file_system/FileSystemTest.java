package np.file_system;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class File implements Comparable<File> {
    private String name;
    private int size;
    private LocalDateTime createdAt;

    File(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    String getName() {
        return name;
    }

    int getSize() {
        return size;
    }

    LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(File that) {
        return Comparator.comparing(File::getCreatedAt)
                .thenComparing(File::getName)
                .thenComparing(File::getSize)
                .compare(this, that);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }
}

class FileSystem {

    private Map<Character, Set<File>> files;
    private Predicate<File> hiddenFiles = file -> file.getName().startsWith(".");

    FileSystem() {
        files = new TreeMap<>();
    }

    void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        files.putIfAbsent(folder, new TreeSet<>());

        files.computeIfPresent(folder, (__, value) -> {
            value.add(new File(name, size, createdAt));
            return value;
        });
    }

    List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        Predicate<File> filesWithSizeLessThen = file -> file.getSize() < size;
        Predicate<File> hiddenFilesWithSizeLessThen = filesWithSizeLessThen.and(hiddenFiles);

        return files.values()
                .stream()
                .flatMap(Set::stream)
                .filter(hiddenFilesWithSizeLessThen)
                .collect(Collectors.toList());
    }

    int totalSizeOfFilesFromFolders(List<Character> folders) {
        return files.entrySet()
                .stream()
                .filter(entry -> folders.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Set::stream)
                .mapToInt(File::getSize)
                .sum();
    }

    Map<Integer, Set<File>> byYear() {
        Map<Integer, Set<File>> result = new TreeMap<>();
        files.values()
                .forEach(set -> set.forEach(file -> {
                    Integer key = file.getCreatedAt().getYear();

                    result.putIfAbsent(key, new TreeSet<>());

                    result.computeIfPresent(key, (__, value) -> {
                        value.add(file);
                        return value;
                    });
                }));

        return result;
    }

    Map<String, Long> sizeByMonthAndDay() {
        Map<String, Long> result = new TreeMap<>();
        files.values()
                .forEach(set -> set.forEach(file -> {

                    String key = String.format("%s-%s", file.getCreatedAt().getMonth(), file.getCreatedAt().getDayOfMonth());

                    result.putIfAbsent(key, 0L);

                    result.computeIfPresent(key, (__, value) -> {
                        value += file.getSize();
                        return value;
                    });
                }));
        return result;
    }
}


public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}