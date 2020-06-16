package np.dialer;

import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Да се имплементира апликација Dialer.
 * <p>
 * Во апликацијата треба да се води евиденција за сите типови на повици (Dialled, Missed и Received).
 * <p>
 * Целосната евиденција се води на ниво на контакти кои можат да имаат еден или повеќе телефонски броеви.
 * <p>
 * Контактот е дефиниран со име и листа од телефонски броеви. Dialer е класа која ќе чита од влезен тек
 * (стандарден влез, датотека, ...) податоци за телефонски повици.
 * <p>
 * Податоците содржат телефонски број, момент на иницирање на повикот (репрезентиран како Long),
 * времетрање на разговорот (формат HH:mm:ss) и типот на повикот (D -Dialled, M -Missed и R - Received).
 * <p>
 * Пример за форматот на податоците:
 * 075333212 12345678L 00:04:35 D (Dialled повик од 4 минути и 35 секунди до бројот 075333212 инициран во моментот 12345678L).
 * <p>
 * Во влезните податоци може да постојат и невалидни податоци и за нив треба да се фрли исклучок од класата WrongCallFormat.
 * За невалидни податоци се сметаат оние чии што телефонски број нема точно 9 знаци, не почнува со 07
 * или деловите од времетраењето не се разделени со :.
 * <p>
 * Ваша задача е да ги имплементирате методите:
 * <p>
 * Dialer(List<Contact> contacts) - default конструктор
 * <p>
 * void readCalls(InputStream inputStream) - метод за читање на податоците за повиците.
 * <p>
 * void writeCalls(OutputStream outputStream, String type) - метод кој ги печати сите разговори од дадениот тип
 * сортирани според времетраењето на разговорот во опаѓачки редослед
 * (ако два разговори имаат исто времетраење тогаш се подредуваат според бројот лексикографски).
 * <p>
 * void writeContactsCalls(OutputStream outputStream) - метод кој ги печати сите разговори групирани по контакт
 * сортирани според моментот на иницирање на разговорот во опаѓачки редослед
 * (се печати повикот за контактот со кој песледно сме комуницирале.
 * па потоа повикот на контактот со кој предпоследно сме комуницирале и.т.н).
 * <p>
 * void writeContactsCallsDetails(Contact contact, OutputStream outputStream) - метод кој ги печати сите разговори
 * за даден контакт сортирани според моментот на иницирање на разговорот во опаѓачки редослед.
 * При печатањето, прво се печати името на контактот со 20 места пормането во лево,
 * па бројот со 15 места порамнето во лево, па времетраењето во формат (HH:mm:ss) со 10 места порамнето во лево
 * и времетраењето во секунди со 10 места порамнети во десно.
 * <p>
 * void makeCall(String phoneNumber, Long timestamp, String duration, String type) - метод кој генерира нов повик
 * кон бројот phoneNumber во временски момент timestamp со времетраење duration и тип type.
 * Доколку не постои контакт за тој телефонски број се генерира нов контакт со име "Unknown".
 */

class Dialer {
    private List<Contact> contacts;
    private List<TelephoneCall> calls;

    Dialer(List<Contact> contacts) {
        this.contacts = contacts;
        this.calls = new LinkedList<>();
    }

    void readCalls(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        bufferedReader.lines()
                .forEach(line -> {
                    String[] callParts = line.split("\\s+");

                    // Exception cases
                    if (!callParts[0].startsWith("07"))
                        throw new WrongCallFormat("Phone number does not start with 07\n");
                    if (callParts[0].length() != 9)
                        throw new WrongCallFormat("Phone number should have exactly 9 characters\n");
                    if (! callParts[2].matches("^..:..:..$"))
                        throw new WrongCallFormat("Duration does not match expression");

                    calls.add(new TelephoneCall(callParts[0], Long.parseLong(callParts[1].substring(0, callParts[1].length() - 1)), callParts[2], callParts[3]));
                });
    }

    void writeCalls(OutputStream outputStream, String type) {
        PrintWriter printWriter = new PrintWriter(outputStream);

        calls.stream()
                .filter(it -> it.getType().equals(type))
                .sorted(Comparator.comparing(TelephoneCall::getDuration)
                        .thenComparing(TelephoneCall::getCallerNumber))
                .forEach(printWriter::println);

        printWriter.flush();
        // printWriter.close();
    }

    void writeContactCalls(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);

        contacts.forEach(contact -> calls.stream()
                .filter(it -> contact.hasPhoneNumber(it.getCallerNumber()))
                .sorted(Comparator.comparing(TelephoneCall::getDuration)
                        .reversed())
                .forEach(printWriter::println));

        printWriter.flush();
        // printWriter.close();
    }

    void writeContactsCallsDetails(Contact contact, OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);

        calls.stream()
                .filter(it -> contact.hasPhoneNumber(it.getCallerNumber()))
                .sorted(Comparator.comparing(TelephoneCall::getDuration)
                        .reversed())
                .forEach(printWriter::println);


        printWriter.flush();
        // printWriter.close();
    }

    void makeCall(String phoneNumber, Long timestamp, String duration, String type) {
        calls.add(new TelephoneCall(phoneNumber, timestamp, duration, type));

        boolean contactExists = contacts.stream()
                .anyMatch(it -> it.hasPhoneNumber(phoneNumber));

        if (! contactExists) {
            Contact contact = new Contact("Unknown");
            contact.addPhoneNumber(phoneNumber);
            contacts.add(contact);
        }
    }
}

class TelephoneCall {
    private String callerNumber;
    private String duration;
    private Long timestamp;
    private String type;

    TelephoneCall(String callerNumber, Long timestamp, String duration, String type) {
        this.callerNumber = callerNumber;
        this.timestamp = timestamp;
        this.duration = duration;
        this.type = type;
    }

    String getType() {
        return type;
    }

    String getCallerNumber() {
        return callerNumber;
    }

    String getDuration() {
        return duration;
    }
}

class Contact {
    private String name;
    private List<String> phoneNumbers;

    Contact(String name) {
        this.name = name;
        this.phoneNumbers = new LinkedList<>();
    }

    void addPhoneNumber(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    boolean hasPhoneNumber(String phoneNumber) {
        return this.phoneNumbers.contains(phoneNumber);
    }
}

class WrongCallFormat extends RuntimeException {
    WrongCallFormat(String message) {
        super(message);
    }
}