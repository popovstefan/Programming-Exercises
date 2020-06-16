package aps.middlepoints;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Given a linked list of line segments, remove middle points.
 * Given a linked list of co-ordinates where adjacent points either form a vertical line or a horizontal line.
 * Delete points from the linked list which are in the middle of a horizontal or vertical line.
 *
 * Examples:
 *
 * Input:  (0,10)->(1,10)->(5,10)->(7,10)->(7,5)->(20,5)->(40,5)
 *
 * Output: Linked List should be changed to following
 * (0,10)->(7,10)->(7,5)->(40,5)
 *
 * The given linked list represents a horizontal line from (0,10) to (7, 10) followed by a vertical line from (7, 10) to (7, 5),
 * followed by a horizontal line from (7, 5) to (40, 5).
 *
 * Input:     (2,3)->(4,3)->(6,3)->(10,3)->(12,3)
 *
 * Output: Linked List should be changed to following
 * (2,3)->(12,3)
 *
 * There is only one vertical line, so all middle points are removed.
 */

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

class SLL<E> {
    private SLLNode<E> first;

    public SLL() {
        // Construct an empty SLL
        this.first = null;
    }

    public void deleteList() {
        first = null;
    }

    public int length() {
        int ret;
        if (first != null) {
            SLLNode<E> tmp = first;
            ret = 1;
            while (tmp.succ != null) {
                tmp = tmp.succ;
                ret++;
            }
            return ret;
        } else
            return 0;

    }

    @Override
    public String toString() {
        String ret = new String();
        if (first != null) {
            SLLNode<E> tmp = first;
            ret += tmp + "->";
            while (tmp.succ != null) {
                tmp = tmp.succ;
                ret += tmp + "->";
            }
        } else
            ret = "Prazna lista!!!";
        return ret;
    }

    public void insertFirst(E o) {
        SLLNode<E> ins = new SLLNode<E>(o, first);
        first = ins;
    }

    public void insertAfter(E o, SLLNode<E> node) {
        if (node != null) {
            SLLNode<E> ins = new SLLNode<E>(o, node.succ);
            node.succ = ins;
        } else {
            System.out.println("Dadenot jazol e null");
        }
    }

    public void insertBefore(E o, SLLNode<E> before) {

        if (first != null) {
            SLLNode<E> tmp = first;
            if (first == before) {
                this.insertFirst(o);
                return;
            }
            //ako first!=before
            while (tmp.succ != before)
                tmp = tmp.succ;
            if (tmp.succ == before) {
                SLLNode<E> ins = new SLLNode<E>(o, before);
                tmp.succ = ins;
            } else {
                System.out.println("Elementot ne postoi vo listata");
            }
        } else {
            System.out.println("Listata e prazna");
        }
    }

    public void insertLast(E o) {
        if (first != null) {
            SLLNode<E> tmp = first;
            while (tmp.succ != null)
                tmp = tmp.succ;
            SLLNode<E> ins = new SLLNode<E>(o, null);
            tmp.succ = ins;
        } else {
            insertFirst(o);
        }
    }

    public E deleteFirst() {
        if (first != null) {
            SLLNode<E> tmp = first;
            first = first.succ;
            return tmp.element;
        } else {
            System.out.println("Listata e prazna");
            return null;
        }
    }

    public E delete(SLLNode<E> node) {
        if (first != null) {
            SLLNode<E> tmp = first;
            if (first == node) {
                return this.deleteFirst();
            }
            while (tmp.succ != node && tmp.succ.succ != null)
                tmp = tmp.succ;
            if (tmp.succ == node) {
                tmp.succ = tmp.succ.succ;
                return node.element;
            } else {
                System.out.println("Elementot ne postoi vo listata");
                return null;
            }
        } else {
            System.out.println("Listata e prazna");
            return null;
        }

    }

    public SLLNode<E> getFirst() {
        return first;
    }

    public SLLNode<E> find(E o) {
        if (first != null) {
            SLLNode<E> tmp = first;
            while (tmp.element != o && tmp.succ != null)
                tmp = tmp.succ;
            if (tmp.element == o) {
                return tmp;
            } else {
                System.out.println("Elementot ne postoi vo listata");
            }
        } else {
            System.out.println("Listata e prazna");
        }
        return first;
    }

    public Iterator<E> iterator() {
        // Return an iterator that visits all elements of this list, in left-to-right order.
        return new LRIterator<E>();
    }

    // //////////Inner class ////////////

    private class LRIterator<E> implements Iterator<E> {

        private SLLNode<E> place, curr;

        private LRIterator() {
            place = (SLLNode<E>) first;
            curr = null;
        }

        public boolean hasNext() {
            return (place != null);
        }

        public E next() {
            if (place == null)
                throw new NoSuchElementException();
            E nextElem = place.element;
            curr = place;
            place = place.succ;
            return nextElem;
        }

        public void remove() {
            //Not implemented
        }
    }

    public void mirror() {
        if (first != null) {
            //m=nextsucc, p=tmp,q=next
            SLLNode<E> tmp = first;
            SLLNode<E> newsucc = null;
            SLLNode<E> next;

            while (tmp != null) {
                next = tmp.succ;
                tmp.succ = newsucc;
                newsucc = tmp;
                tmp = next;
            }
            first = newsucc;
        }

    }

    public void merge(SLL<E> in) {
        if (first != null) {
            SLLNode<E> tmp = first;
            while (tmp.succ != null)
                tmp = tmp.succ;
            tmp.succ = in.getFirst();
        } else {
            first = in.getFirst();
        }
    }
}

class Point {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}

public class MiddlePoints {

    private static SLL<Point> removeMiddlePoints(SLL<Point> list) {
        SLLNode<Point> current = list.getFirst();
        while (current.succ != null) {


            if (current.element.x == current.succ.element.x) { // same x coord's, case of vertical line
                if (current.succ.succ != null) { // there are at least three points left
                    if (current.succ.succ.element.x == current.element.x)
                        list.delete(current.succ);
                    else
                        current = current.succ;
                }
                else // less than three points left, nothing left to do
                    break;
            }


            if (current.element.y == current.succ.element.y) { // same y coord's, case of horizontal line
                if (current.succ.succ != null) { // there are at least three points left
                    if (current.succ.succ.element.y == current.element.y)
                        list.delete(current.succ);
                    else
                        current = current.succ;
                }
                else // less than three points left, nothing left to do
                    break;
            }
        }
        return list;
    }

    public static void main(String [] args) {
        SLL<Point> list = new SLL<>();

        // First scenario
        list.insertLast(new Point(0, 10));
        list.insertLast(new Point(1, 10));
        list.insertLast(new Point(5, 10));
        list.insertLast(new Point(7, 10));
        list.insertLast(new Point(7, 5));
        list.insertLast(new Point(20, 5));
        list.insertLast(new Point(40, 5));

        // Second scenario
        /*list.insertLast(new Point(2, 3));
        list.insertLast(new Point(4, 3));
        list.insertLast(new Point(6, 3));
        list.insertLast(new Point(10, 3));
        list.insertLast(new Point(12, 3));*/


        System.out.println("List before removing middle points:");
        System.out.println(list);
        System.out.println("List after removing middle points:");
        System.out.println(removeMiddlePoints(list));

    }
}
