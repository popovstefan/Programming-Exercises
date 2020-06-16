package aps.shiftstack;

/*
 * Write a method shift that takes a stack of integers and an integer n as parameters
 * and that shifts n values from the bottom of the stack to the top of the stack.
 * For example, if a variable called s stores the following sequence of values:
 *
 * bottom [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] top
 *
 * If we make the call shift(s, 6); the method shifts the six values at the bottom of the stack
 * to the top of the stack and leaves the other values in the same order producing:
 *
 * bottom [7, 8, 9, 10, 6, 5, 4, 3, 2, 1] top
 *
 *
 * Notice that the value that was at the bottom of the stack is now at the top,
 * the value that was second from the bottom is now second from the top,
 * the value that was third from the bottom is now third from the top, and so on,
 * and that the four values not involved in the shift are now at the bottom of the stack in their original order.
 *
 * If s had stored these values instead:
 *
 * bottom [7, 23, -7, 0, 22, -8, 4, 5] top
 *
 * If we make the following call: shift(s, 3); then s should store these values after the call:
 *
 * bottom [0, 22, -8, 4, 5, -7, 23, 7] top
 *
 * You are to use one queue as auxiliary storage to solve this problem. You may assume that the parameter n is >= 0
 * and not larger than the number of elements in the stack.
 */

import java.util.NoSuchElementException;
import java.util.stream.IntStream;

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

public class ShiftStack {

    private static ArrayStack<Integer> shift(ArrayStack<Integer> stack, int stackSize, int places) {
        ArrayQueue<Integer> queue = new ArrayQueue<>(stackSize + 1);

        // stack: 1 2 3 4 5 6 (T), queue: 7 8 9 10 (F)
        for (int i = 0; i < stackSize - places; i++)
            queue.enqueue(stack.pop());

        // stack: 1 2 3 4 5 6 10 9 8 7 (T)
        for (int i = 0; i < stackSize - places; i++)
            stack.push(queue.dequeue());

        // queue: 7 8 9 10 6 5 4 3 2 1 (F)
        for (int i = 0; i < stackSize; i++)
            queue.enqueue(stack.pop());

        // stack: 1 2 3 4 5 6 10 9 8 7 (T)
        for (int i = 0; i < stackSize; i++)
            stack.push(queue.dequeue());

        return stack;
    }

    public static void main(String[] args) {
        int n = 8;
        int places = 3;
        ArrayStack<Integer> stack = new ArrayStack<>(n + 1);

        // First scenario
        /*IntStream.range(1, n + 1).forEach(it -> {
            System.out.printf("%d ", it);
            stack.push(it);
        });*/

        // Second scenario
        IntStream.of(7, 23, -7, 0, 22, -8, 4, 5).forEach(it -> {
            System.out.printf("%d ", it);
            stack.push(it);
        });

        System.out.println(); // just a new line

        ArrayStack<Integer> shiftedStack = shift(stack, n, places);
        System.out.printf("Stack after shifting elements by %d places:\n", places);
        while (! shiftedStack.isEmpty())
            System.out.printf("%d ", shiftedStack.pop());

    }
}
