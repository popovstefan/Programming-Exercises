package aps.konsultacii;

import aps.nizi_listi.Array;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class Konsultacii {

    public static void main(String [] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int brojPrasanja = Integer.parseInt(bufferedReader.readLine());
        ArrayQueue<String> studentiPrasanja = new ArrayQueue<>(brojPrasanja);
        for (int i = 0; i < brojPrasanja; i++)
            studentiPrasanja.enqueue(bufferedReader.readLine());

        int brojZadaci = Integer.parseInt(bufferedReader.readLine());
        ArrayQueue<String> studentiZadaci = new ArrayQueue<>(brojZadaci);
        for (int i = 0; i < brojZadaci; i++)
            studentiZadaci.enqueue(bufferedReader.readLine());

        int brojDvete = Integer.parseInt(bufferedReader.readLine());
        ArrayQueue<String> studentiDvete = new ArrayQueue<>(brojDvete);
        for (int i = 0; i < brojDvete; i++)
            studentiDvete.enqueue(bufferedReader.readLine());

        ArrayQueue<String> redosled = new ArrayQueue<>((brojDvete + brojPrasanja + brojZadaci) * 2);

        while (! (studentiPrasanja.isEmpty() && studentiZadaci.isEmpty() && studentiDvete.isEmpty())) {
            if (! studentiPrasanja.isEmpty())
                redosled.enqueue(studentiPrasanja.dequeue());
            else {
                if (! studentiDvete.isEmpty()) {
                    String student = studentiDvete.dequeue();
                    redosled.enqueue(student);
                    studentiZadaci.enqueue(student);
                }
            }

            if (! studentiZadaci.isEmpty())
                redosled.enqueue(studentiZadaci.dequeue());
            else {
                if (! studentiDvete.isEmpty()) {
                    String student = studentiDvete.dequeue();
                    redosled.enqueue(student);
                    studentiPrasanja.enqueue(student);
                }
            }
        }
        while (! redosled.isEmpty())
            System.out.println(redosled.dequeue());
    }
}
