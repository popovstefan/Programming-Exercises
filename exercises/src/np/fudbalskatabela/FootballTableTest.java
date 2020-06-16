package np.fudbalskatabela;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


class Team implements Comparable<Team> {
    private String name;
    private int wins, draws, losses;
    private int scoredGoals, concededGoals;

    Team(String name) {
        this.name = name;
    }

    void addWin() {
        wins++;
    }

    void addDraw() {
        draws++;
    }

    void addLoss() {
        losses++;
    }

    void recordGoalScore(int scoredGoals, int concededGoals) {
        this.scoredGoals += scoredGoals;
        this.concededGoals += concededGoals;
    }

    private int points() {
        return wins * 3 + draws;
    }

    private int goalDifference() {
        return scoredGoals - concededGoals;
    }

    private String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5s%5s%5s%5s%5s", name, wins + draws + losses, wins, draws, losses, points());
    }

    @Override
    public int compareTo(Team that) {
        return Comparator.comparing(Team::points)
                .thenComparing(Team::goalDifference)
                .reversed()
                .thenComparing(Team::getName)
                .compare(this, that);
    }
}


class FootballTable {

    private HashMap<String, Team> teams;

    FootballTable() {
        teams = new HashMap<>();
    }

    void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = teams.computeIfAbsent(homeTeam, Team::new);
        Team away = teams.computeIfAbsent(awayTeam, Team::new);

        home.recordGoalScore(homeGoals, awayGoals);
        away.recordGoalScore(awayGoals, homeGoals);

        if (homeGoals > awayGoals) {
            home.addWin();
            away.addLoss();
        } else if (homeGoals < awayGoals) {
            home.addLoss();
            away.addWin();
        } else {
            home.addDraw();
            away.addDraw();
        }
    }

    void printTable() {
        List<Team> table = teams.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < table.size(); i++)
            System.out.printf("%2d. %s\n", i + 1, table.get(i));
    }

}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Y