package aps.drva.tri;

public class BNode<E> {
    
    public E info;
    public BNode<E> left;
    public BNode<E> right;
    char ltag;
    char rtag;
    
    static int LEFT = 1;
    static int RIGHT = 2;
    
    public BNode(E info) {
        this.info = info;
        left = null;
        right = null;
        ltag = '-';
        rtag = '-';
    }
    
}
