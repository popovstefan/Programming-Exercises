package np.movies;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Movie {
    private String title;
    private int[] ratings;

    Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    double averageRating() {
        return IntStream.of(ratings)
                .average()
                .orElse(0);
    }

    int ratingsLength() {
        return ratings.length;
    }

    String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, averageRating(), ratingsLength());
    }
}

class MoviesList {
    private List<Movie> movies;

    MoviesList() {
        movies = new LinkedList<>();
    }

    void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::averageRating)
                        .reversed()
                        .thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    List<Movie> top10ByRatingCoef() {
        int maxRatingLength = movies.stream()
                .mapToInt(Movie::ratingsLength)
                .max()
                .orElse(0);
        return movies.stream()
                .sorted((m1, m2) -> {
                    double ratingCoefficient1 = m1.averageRating() * m1.ratingsLength() / maxRatingLength;
                    double ratingCoefficient2 = m2.averageRating() * m2.ratingsLength() / maxRatingLength;

                    if (ratingCoefficient1 == ratingCoefficient2)
                        return m1.getTitle().compareTo(m2.getTitle());
                    else
                        return -Double.compare(ratingCoefficient1, ratingCoefficient2);
                })
                .limit(10)
                .collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde