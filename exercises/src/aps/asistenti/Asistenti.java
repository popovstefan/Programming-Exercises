
package aps.asistenti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

interface Queue<E> {

    // Elementi na redicata se objekti od proizvolen tip.

    // Metodi za pristap:

    public boolean isEmpty();
    // Vrakja true ako i samo ako redicata e prazena.

    public int size();
    // Ja vrakja dolzinata na redicata.

    public E peek();
    // Go vrakja elementot na vrvot t.e. pocetokot od redicata.

    // Metodi za transformacija:

    public void clear();
    // Ja prazni redicata.

    public void enqueue(E x);
    // Go dodava x na kraj od redicata.

    public E dequeue();
    // Go otstranuva i vrakja pochetniot element na redicata.
}

class ArrayQueue<E> implements Queue<E> {

    // Redicata e pretstavena na sledniot nacin:
    // length go sodrzi brojot na elementi.
    // Ako length > 0, togash elementite na redicata se zachuvani vo elems[front...rear-1]
    // Ako rear > front, togash vo  elems[front...maxlength-1] i elems[0...rear-1]
    E[] elems;
    int length, front, rear;

    // Konstruktor ...

    @SuppressWarnings("unchecked")
    public ArrayQueue(int maxlength) {
        elems = (E[]) new Object[maxlength];
        clear();
    }

    public boolean isEmpty() {
        // Vrakja true ako i samo ako redicata e prazena.
        return (length == 0);
    }

    public int size() {
        // Ja vrakja dolzinata na redicata.
        return length;
    }

    public E peek() {
        // Go vrakja elementot na vrvot t.e. pocetokot od redicata.
        if (length > 0)
            return elems[front];
        else
            throw new NoSuchElementException();
    }

    public void clear() {
        // Ja prazni redicata.
        length = 0;
        front = rear = 0;  // arbitrary
    }

    public void enqueue(E x) {
        // Go dodava x na kraj od redicata.
        elems[rear++] = x;
        if (rear == elems.length) rear = 0;
        length++;
    }

    public E dequeue() {
        // Go otstranuva i vrakja pochetniot element na redicata.
        if (length > 0) {
            E frontmost = elems[front];
            elems[front++] = null;
            if (front == elems.length) front = 0;
            length--;
            return frontmost;
        } else
            throw new NoSuchElementException();
    }
}

public class Asistenti {

    private static boolean contains(String[] array, String element) {
        for (String s : array)
            if (s.equals(element))
                return true;
        return false;
    }

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int brojAsistenti = Integer.parseInt(bufferedReader.readLine());
        ArrayQueue<String> assistenti = new ArrayQueue<>(brojAsistenti);
        for (int i = 0; i < brojAsistenti; i++)
            assistenti.enqueue(bufferedReader.readLine());


        int brojPredmeti = Integer.parseInt(bufferedReader.readLine());
        ArrayQueue<String> predmeti = new ArrayQueue<>(brojPredmeti);
        for (int i = 0; i < brojPredmeti; i++)
            predmeti.enqueue(bufferedReader.readLine());

        int brojOtsutni = Integer.parseInt(bufferedReader.readLine());
        String[] otsutniAsistenti = new String[brojOtsutni];
        for (int i = 0; i < brojOtsutni; i++)
            otsutniAsistenti[i] = bufferedReader.readLine();

        while (!predmeti.isEmpty()) {

            String[] parts = predmeti.dequeue().split("\\s+");
            int potrebniAsistenti = Integer.parseInt(parts[1]);

            System.out.println(parts[0]);
            System.out.println(potrebniAsistenti);

            while (potrebniAsistenti > 0) {
                String asistent = assistenti.dequeue();
                if (!contains(otsutniAsistenti, asistent)) {
                    System.out.println(asistent);
                    assistenti.enqueue(asistent);
                    potrebniAsistenti--;
                }
            }
        }

    }
}
