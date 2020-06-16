package aps.tancovi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.NoSuchElementException;

interface Queue<E> {

    // Elementi na redicata se objekti od proizvolen tip.

    // Metodi za pristap:

    boolean isEmpty();
    // Vrakja true ako i samo ako redicata e prazena.

    int size();
    // Ja vrakja dolzinata na redicata.

    E peek();
    // Go vrakja elementot na vrvot t.e. pocetokot od redicata.

    // Metodi za transformacija:

    void clear();
    // Ja prazni redicata.

    void enqueue(E x);
    // Go dodava x na kraj od redicata.

    E dequeue();
    // Go otstranuva i vrakja pochetniot element na redicata.
}

interface Stack<E> {

    // Elementi na stekot se objekti od proizvolen tip.

    // Metodi za pristap:

    boolean isEmpty();
    // Vrakja true ako i samo ako stekot e prazen.

    E peek();
    // Go vrakja elementot na vrvot od stekot.

    // Metodi za transformacija:

    void clear();
    // Go prazni stekot.

    void push(E x);
    // Go dodava x na vrvot na stekot.

    E pop();
    // Go otstranuva i vrakja elementot shto e na vrvot na stekot.
}

class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}

class LinkedQueue<E> implements Queue<E> {

    // Redicata e pretstavena na sledniot nacin:
    // length go sodrzi brojot na elementi.
    // Elementite se zachuvuvaat vo jazli dod SLL
    // front i rear se linkovi do prviot i posledniot jazel soodvetno.
    SLLNode<E> front, rear;
    int length;

    // Konstruktor ...

    public LinkedQueue() {
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
        if (front == null)
            throw new NoSuchElementException();
        return front.element;
    }

    public void clear() {
        // Ja prazni redicata.
        front = rear = null;
        length = 0;
    }

    public void enqueue(E x) {
        // Go dodava x na kraj od redicata.
        SLLNode<E> latest = new SLLNode<E>(x, null);
        if (rear != null) {
            rear.succ = latest;
            rear = latest;
        } else
            front = rear = latest;
        length++;
    }

    public E dequeue() {
        // Go otstranuva i vrakja pochetniot element na redicata.
        if (front != null) {
            E frontMost = front.element;
            front = front.succ;
            if (front == null) rear = null;
            length--;
            return frontMost;
        } else
            throw new NoSuchElementException();
    }

}

class LinkedStack<E> implements Stack<E> {

    //Stekot e pretstaven na sledniot nacin: top e link do prviot jazol
    // na ednostrano-povrzanata lista koja sodrzi gi elementite na stekot .
    private SLLNode<E> top;

    public LinkedStack() {
        // Konstrukcija na nov, prazen stek.
        top = null;
    }

    public boolean isEmpty() {
        // Vrakja true ako i samo ako stekot e prazen.
        return (top == null);
    }

    public E peek() {
        // Go vrakja elementot na vrvot od stekot.
        if (top == null)
            throw new NoSuchElementException();
        return top.element;
    }

    public void clear() {
        // Go prazni stekot.
        top = null;
    }

    public void push(E x) {
        // Go dodava x na vrvot na stekot.
        top = new SLLNode<E>(x, top);
    }

    public E pop() {
        // Go otstranuva i vrakja elementot shto e na vrvot na stekot.
        if (top == null)
            throw new NoSuchElementException();
        E topElem = top.element;
        top = top.succ;
        return topElem;
    }

    public int size() {
        int result = 0;

        SLLNode<E> tmp = top;

        while (tmp != null) {
            result++;
            tmp = tmp.succ;
        }

        return result;
    }

}

public class Dances {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        LinkedStack<String> osnovni = new LinkedStack<>();
        LinkedStack<String> standardni = new LinkedStack<>();
        LinkedStack<String> latino = new LinkedStack<>();

        String[] people = bufferedReader.readLine().split("\\s+");

        for (String human : people) {
            switch (human) {
                case "OM":
                    osnovni.push(human);
                    break;
                case "SM":
                    standardni.push("SM");
                    break;
                case "LM":
                    latino.push("LM");
                    break;
            }
        }

        for (String human : people) {
            switch (human) {
                case "OZ":
                    if (!osnovni.isEmpty())
                        osnovni.pop();
                    break;
                case "SZ":
                    if (!standardni.isEmpty())
                        standardni.pop();
                    break;
                case "LZ":
                    if (!latino.isEmpty())
                        latino.pop();
                    break;

            }
        }

        int aloneCount = standardni.size() + osnovni.size() + latino.size();

        System.out.println(aloneCount);

        while (!osnovni.isEmpty())
            System.out.println(osnovni.pop());

        while (!standardni.isEmpty())
            System.out.println(standardni.pop());

        while (!latino.isEmpty())
            System.out.println(latino.pop());
    }
}
