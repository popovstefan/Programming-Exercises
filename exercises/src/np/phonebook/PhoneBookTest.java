package np.phonebook;

import java.util.*;

class DuplicateNumberException extends RuntimeException {
    DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact implements Comparable<Contact> {
    private String name;
    private String number;

    Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Contact that) {
        if (this.name.equals(that.name))
            return this.number.compareTo(that.number);
        return this.name.compareTo(that.name);
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }
}

class PhoneBook {

    private TreeSet<Contact> contacts;
    private TreeMap<String, TreeSet<Contact>> contactsByName;
    private TreeMap<String, TreeSet<Contact>> contactsByNumber;

    PhoneBook() {
        contacts = new TreeSet<>();
        contactsByName = new TreeMap<>();
        contactsByNumber = new TreeMap<>();
    }

    void addContact(String name, String number) {
        Contact contact = new Contact(name, number);
        boolean added = contacts.add(contact);
        if (added) {
            contactsByName.putIfAbsent(name, new TreeSet<>());
            contactsByName.computeIfPresent(name, (key, set) -> {
                set.add(contact);
                return set;
            });

            List<String> numberParts = getNumberParts(number);

            numberParts.forEach(numberPart -> {
                contactsByNumber.putIfAbsent(numberPart, new TreeSet<>());
                contactsByNumber.computeIfPresent(numberPart, (key, set) -> {
                    set.add(contact);
                    return set;
                });
            });

        } else
            throw new DuplicateNumberException("Duplicate number: " + number);
    }

    private List<String> getNumberParts(String number) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= number.length() - 3; i++)
            for (int j = 3; j <= number.length() - i; j++)
                result.add(number.substring(i, i + j));
        return result;
    }

    void contactsByNumber(String number) {
        /*contactsByNumber.getOrDefault(number, new TreeSet<>())
                .forEach(System.out::println);*/
        if (contactsByNumber.containsKey(number))
            contactsByNumber.get(number)
                    .forEach(System.out::println);
        else
            System.out.println("NOT FOUND");
    }

    void contactsByName(String name) {
        /*contactsByName.getOrDefault(name, new TreeSet<>())
                .forEach(System.out::println);*/
        if (contactsByName.containsKey(name))
            contactsByName.get(name)
            .forEach(System.out::println);
        else
            System.out.println("NOT FOUND");
    }
}

public class PhoneBookTest {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }
}
