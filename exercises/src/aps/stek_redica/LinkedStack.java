package aps.stek_redica;

import java.util.NoSuchElementException;

public class LinkedStack<E> implements Stack<E> {

    //Stekot e pretstaven na sledniot nacin: top e link do prviot jazol
    // na ednostrano-povrzanata lista koja sodrzi gi elementite na stekot .
    private SLLNode<E> top;

    public LinkedStack () {
    	// Konstrukcija na nov, prazen stek.
        top = null;
    }

    public boolean isEmpty () {
    	// Vrakja true ako i samo ako stekot e prazen.
        return (top == null);
    }

    public E peek () {
    	// Go vrakja elementot na vrvot od stekot.
        if (top == null)
            throw new NoSuchElementException();
        return top.element;
    }

    public void clear () {
    	// Go prazni stekot.
        top = null;
    }

    public void push (E x) {
    	// Go dodava x na vrvot na stekot.
        top = new SLLNode<E>(x, top);
    }

    public E pop () {
    	// Go otstranuva i vrakja elementot shto e na vrvot na stekot.
        if (top == null)
            throw new NoSuchElementException();
        E topElem = top.element;
        top = top.succ;
        return topElem;
    }

}
