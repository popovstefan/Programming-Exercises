package np.word_vectors;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Word {
    private String word;

    Word(String word) {
        this.word = word;
    }

    String getWord() {
        return word;
    }
}

class Vector {
    static final Vector DEFAULT = new Vector(Arrays.asList(5, 5, 5, 5, 5));
    static final Vector IDENTITY = new Vector(Arrays.asList(0, 0, 0, 0, 0));

    private List<Integer> numbers;

    Vector(List<Integer> numbers) {
        this.numbers = numbers;
    }

    int max() {
        return this.numbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    Vector sum(Vector that) {
        return new Vector(IntStream.range(0, that.numbers.size())
                .map(index -> this.numbers.get(index) + that.numbers.get(index))
                .boxed()
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return this.numbers.toString();
    }
}

class WordVectors {
    private Map<Word, Vector> wordVectorMap;
    private List<Vector> vectors;

    WordVectors(String[] words, List<List<Integer>> vectors) {
        wordVectorMap = new TreeMap<>(Comparator.comparing(Word::getWord));

        IntStream.range(0, words.length)
                .forEach(index -> wordVectorMap.put(new Word(words[index]), new Vector(vectors.get(index))));
    }

    void readWords(List<String> words) {
        vectors = words.stream()
                .map(word -> wordVectorMap.getOrDefault(new Word(word), Vector.DEFAULT))
                .collect(Collectors.toList());
    }

    List<Integer> slidingWindow(int n) {
        return IntStream.range(0, vectors.size() - n + 1)
                .mapToObj(i -> vectors.subList(i, i + n)
                        .stream()
                        .reduce(Vector.IDENTITY, Vector::sum))
                .map(Vector::max)
                .collect(Collectors.toList());
    }
}

public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}