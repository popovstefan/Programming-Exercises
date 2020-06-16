package aps.interleavequeue;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;


/**
 *Given a queue of integers of even length, rearrange the elements by interleaving
 * the first half of the queue with the second half of the queue.
 *
 * Only a stack can be used as an auxiliary space.
 *
 * Examples:
 *
 * Input :  1 2 3 4
 * Output : 1 3 2 4
 *
 * Input : 11 12 13 14 15 16 17 18 19 20
 * Output : 11 16 12 17 13 18 14 19 15 20
 *
 * https://www.geeksforgeeks.org/interleave-first-half-queue-second-half/
 */

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

class ArrayQueue<E> implements Queue<E> {

    // Redicata e pretstavena na sledniot nacin:
    // length go sodrzi brojot na elementi.
    // Ako length > 0, togash elementite na redicata se zachuvani vo elems[front...rear-1]
    // Ako rear > front, togash vo  elems[front...maxlength-1] i elems[0...rear-1]
    E[] elems;
    int length, front, rear;

    // Konstruktor ...

    @SuppressWarnings("unchecked")
    public ArrayQueue (int maxlength) {
        elems = (E[]) new Object[maxlength];
        clear();
    }

    public boolean isEmpty () {
        // Vrakja true ako i samo ako redicata e prazena.
        return (length == 0);
    }

    public int size () {
        // Ja vrakja dolzinata na redicata.
        return length;
    }

    public E peek () {
        // Go vrakja elementot na vrvot t.e. pocetokot od redicata.
        if (length > 0)
            return elems[front];
        else
            throw new NoSuchElementException();
    }

    public void clear () {
        // Ja prazni redicata.
        length = 0;
        front = rear = 0;  // arbitrary
    }

    public void enqueue (E x) {
        // Go dodava x na kraj od redicata.
        elems[rear++] = x;
        if (rear == elems.length)  rear = 0;
        length++;
    }

    public E dequeue () {
        // Go otstranuva i vrakja pochetniot element na redicata.
        if (length > 0) {
            E frontmost = elems[front];
            elems[front++] = null;
            if (front == elems.length)  front = 0;
            length--;
            return frontmost;
        } else
            throw new NoSuchElementException();
    }
}

class ArrayStack<E> implements Stack<E> {

    // Stekot e pretstaven na sledniot nacin:
    //depth e dlabochinata na stekot, a
    // elems[0...depth-1] se negovite elementi.
    private E[] elems;
    private int depth;

    @SuppressWarnings("unchecked")
    public ArrayStack (int maxDepth) {
        // Konstrukcija na nov, prazen stek.
        elems = (E[]) new Object[maxDepth];
        depth = 0;
    }


    public boolean isEmpty () {
        // Vrakja true ako i samo ako stekot e prazen.
        return (depth == 0);
    }


    public E peek () {
        // Go vrakja elementot na vrvot od stekot.
        if (depth == 0)
            throw new NoSuchElementException();
        return elems[depth-1];
    }


    public void clear () {
        // Go prazni stekot.
        for (int i = 0; i < depth; i++)  elems[i] = null;
        depth = 0;
    }


    public void push (E x) {
        // Go dodava x na vrvot na stekot.
        elems[depth++] = x;
    }


    public E pop () {
        // Go otstranuva i vrakja elementot shto e na vrvot na stekot.
        if (depth == 0)
            throw new NoSuchElementException();
        E topmost = elems[--depth];
        elems[depth] = null;
        return topmost;
    }
}

public class InterleaveQueue {

    public static void main(String [] args) {

        int n = 10;
        ArrayQueue<Integer> queue = new ArrayQueue<>(n + 1);
        IntStream.range(1, n + 1).forEach(queue::enqueue);

        ArrayStack<Integer> stack = new ArrayStack<>(n + 1);

        // queue: 6 7 8 9 10 (F), stack: 1 2 3 4 5 (T)
        for (int i = 0; i < n / 2; i++)
            stack.push(queue.dequeue());

        // queue: 6 7 8 9 10 5 4 3 2 1 (F)
        while (! stack.isEmpty())
            queue.enqueue(stack.pop());

        // queue: 5 4 3 2 1 6 7 8 9 10 (F)
        for (int i = 0; i < n / 2; i++)
            queue.enqueue(queue.dequeue());

        // queue: 6 7 8 9 10 (F), stack: 5 4 3 2 1 (T)
        for (int i = 0; i < n / 2; i++)
            stack.push(queue.dequeue());

        // queue: 1 6 2 7 3 8 4 9 5 10 (F)
        while (! stack.isEmpty()) {
            queue.enqueue(stack.pop());
            queue.enqueue(queue.dequeue());
        }

        // print
        while (! queue.isEmpty())
            System.out.printf("%d ", queue.dequeue());
    }
}
