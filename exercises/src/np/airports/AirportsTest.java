package np.airports;


import java.util.*;
import java.util.stream.Collectors;

class Flight implements Comparable<Flight> {
    private String airportFromCode;
    private String airportToCode;
    private int takeOffTime;
    private int duration;

    Flight(String airportFromCode, String airportToCode, int takeOffTime, int duration) {
        this.airportFromCode = airportFromCode;
        this.airportToCode = airportToCode;
        this.takeOffTime = takeOffTime;
        this.duration = duration;
    }

    String getAirportToCode() {
        return airportToCode;
    }

    String getAirportFromCode() {
        return airportFromCode;
    }

    int getTakeOffTime() {
        return takeOffTime;
    }

    @Override
    public int compareTo(Flight that) {
        return Integer.compare(this.takeOffTime, that.takeOffTime);
    }

    @Override
    public String toString() {

        int takeOffHours = takeOffTime / 60;
        int takeOffMinutes = takeOffTime % 60;
        int landingHours = ((takeOffTime + duration) / 60) % 24;
        int landingMinutes = (takeOffTime + duration) % 60;

        int durationHours = duration / 60;
        int durationMinutes = duration % 60;

        String extraDay = (takeOffTime + duration) >= 24 * 60 ? "+1d " : "";

        return String.format("%s-%s %02d:%02d-%02d:%02d %s%dh%02dm", airportFromCode, airportToCode,
                takeOffHours, takeOffMinutes, landingHours, landingMinutes, extraDay, durationHours, durationMinutes);
    }
}

class Airport {
    private String name;
    private String country;
    private String code;
    private int yearlyPassengers;

    private TreeMap<String, TreeSet<Flight>> flights;


    Airport(String name, String country, String code, int yearlyPassengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.yearlyPassengers = yearlyPassengers;

        flights = new TreeMap<>();
    }

    void addFlight(String to, int takeOffTime, int duration) {
        flights.computeIfAbsent(to, key -> new TreeSet<>());
        flights.computeIfPresent(to, (key, value) -> {
            value.add(new Flight(code, to, takeOffTime, duration));
            return value;
        });
    }

    TreeSet<Flight> getFlights(Comparator<Flight> comparator) {
        return flights.values()
                .stream()
                .flatMap(TreeSet::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, yearlyPassengers);
    }
}

class Airports {

    private TreeMap<String, Airport> airports;

    Airports() {
        airports = new TreeMap<>();
    }

    void addAirport(String name, String country, String code, int yearlyPassengers) {
        airports.computeIfAbsent(code, key -> new Airport(name, country, code, yearlyPassengers));
    }

    void addFlights(String from, String to, int takeOffTime, int duration) {
        airports.get(from)
                .addFlight(to, takeOffTime, duration);
    }

    void showFlightsFromAirport(String code) {
        Airport airport = airports.get(code);
        System.out.println(airport);
        TreeSet<Flight> flights = airport.getFlights(Comparator.comparing(Flight::getAirportToCode)
                .thenComparing(Flight::getTakeOffTime));
        int i = 1;
        for (Flight flight : flights)
            System.out.printf("%d. %s\n", i++, flight);

    }

    void showDirectFlightsFromTo(String from, String to) {
        TreeSet<Flight> flights = airports.get(from)
                .getFlights(Comparator.comparing(Flight::getTakeOffTime))
                .stream()
                .filter(flight -> flight.getAirportToCode().equals(to))
                .collect(Collectors.toCollection(TreeSet::new));
        if (flights.isEmpty())
            System.out.printf("No flights from %s to %s\n", from, to);
        else
            flights.forEach(System.out::println);
    }

    void showDirectFlightsTo(String to) {
        airports.values()
                .stream()
                .map(airport -> airport.getFlights(Comparator.comparing(Flight::getTakeOffTime)))
                .flatMap(TreeSet::stream)
                .filter(flight -> flight.getAirportToCode().equals(to))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Flight::getTakeOffTime)
                        .thenComparing(Flight::getAirportFromCode))))
                .forEach(System.out::println);
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}


/*

class Flight implements Comparable<Flight> {
    private String from, to;
    private int time, duration;

    Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }


    private int getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public int compareTo(Flight that) {
        return Comparator.comparing(Flight::getTime)
                .thenComparing(Flight::getFrom)
                .compare(this, that);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%s-%s", from, to));


        int hour = time / 60;
        int minute = time % 60;

        int durationHour = duration / 60;
        int durationMinute = duration % 60;

        int arriveTime = time + duration;
        int arriveHour = arriveTime / 60;
        int arriveMinute = arriveTime % 60;
        if (arriveTime > (24 * 60))
            arriveHour -= 24;
        str.append(String.format(" %02d:%02d-%02d:%02d", hour, minute, arriveHour, arriveMinute));


        if (time + duration > (24 * 60)) {
            str.append(" +1d");

        }

        str.append(String.format(" %dh%02dm", durationHour, durationMinute));
        return str.toString();
    }
}

class Airport {
    private String name, country, code;
    private int passengers;
    Map<String, Set<Flight>> flights;

    Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeMap<>();
    }

    void addFlight(Flight flight) {
        Set<Flight> flightSet = flights.computeIfAbsent(flight.getTo(), key -> new TreeSet<>());
        flightSet.add(flight);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Airports {
    private TreeMap<String, Airport> airports;

    Airports() {
        airports = new TreeMap<>();
    }

    void addAirport(String name, String country, String code, int passengers) {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    void addFlights(String from, String to, int time, int duration) {
        airports.get(from)
                .addFlight(new Flight(from, to, time, duration));
    }

    void showFlightsFromAirport(String code) {
        System.out.println(airports.get(code));
        int i = 1;
        for (String fromCode : airports.get(code).flights.keySet()) {
            Set<Flight> flightsFrom = airports.get(code)
                    .flights.get(fromCode);
            for (Flight flight : flightsFrom)
                System.out.println(String.format("%d. %s", i++, flight.toString()));
        }
    }

    void showDirectFlightsFromTo(String from, String to) {
        if (airports.get(from).flights.get(to) == null)
            System.out.println("No flights from " + from + " to " + to);
        else {
            airports.get(from)
                    .flights.values()
                    .forEach(flightSet -> flightSet.stream()
                            .filter(flight -> flight.getTo().equals(to))
                            .forEach(System.out::println));
        }
    }

    void showDirectFlightsTo(String to) {
        Set<Flight> flightsTo = new TreeSet<>();
        for (Airport airport : airports.values()) {
            if (airport.flights.get(to) != null)
                flightsTo.addAll(airport.flights.get(to));
        }
        flightsTo.forEach(System.out::println);
    }
}

*/