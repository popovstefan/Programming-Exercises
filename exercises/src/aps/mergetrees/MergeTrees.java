package aps.mergetrees;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Да се направи функција за спојување на две бинарни пребарувачки дрва во едно балансирано бинарно пребарувачко дрво AVL.
 * Нека бројот на елементите на првото дрво е n, а на второто е m. Функцијата за спојување треба да биде со сложеност O(n+m).
 * Забелешка: Секоја друга имплементација која нема да соодветствува со бараната сложеност нема да се земе во предвид.
 * Да се напише во коментар како е добиена бараната сложеност со вашата функција.
 * Влез: Во првиот ред се вчитува бројот на јазли кои ќе се внесуваат во првото дрво.
 * Во вториот ред се вчитува бројот на јазли кои ќе се внесуваат во второто дрво.
 * Понатака во секој ред одделно се дадени вредностите на јазлите од првото бинарно дрво, а после тоа од второто бинарно дрво.
 * Излез: Да се испечатат елементите во новодобиеното AVL дрво. (Користете ја методата print() од имплементацијата за AVL дрва)
 * Име на класа (Јава):MergeTrees
 */

class BNode<E extends Comparable<E>> {

    public E info;
    public BNode<E> left;
    public BNode<E> right;

    public BNode(E info) {
        this.info = info;
        left = null;
        right = null;
    }

    public BNode(E info, BNode<E> left, BNode<E> right) {
        this.info = info;
        this.left = left;
        this.right = right;
    }

}

class BinarySearchTree<E extends Comparable<E>> {

    /**
     * The tree root.
     */
    private BNode<E> root;

    public BNode<E> getRoot() {
        return root;
    }

    /**
     * Construct the tree.
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(E x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(E x) {
        root = remove(x, root);
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public E findMin() {
        return elementAt(findMin(root));
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public E findMax() {
        return elementAt(findMax(root));
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * @return the matching item or null if not found.
     */
    public BNode<E> find(E x) {
        return find(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    /**
     * Internal method to get element field.
     *
     * @param t the node.
     * @return the element field or null if t is null.
     */
    private E elementAt(BNode<E> t) {
        if (t == null)
            return null;
        return t.info;
    }

    /**
     * Internal method to insert into a subtree.
     *
     * @param x the item to insert.
     * @param t the node that roots the tree.
     * @return the new root.
     */
    private BNode<E> insert(E x, BNode<E> t) {
        if (t == null) {
            t = new BNode<E>(x, null, null);
        } else if (x.compareTo(t.info) < 0) {
            t.left = insert(x, t.left);
        } else if (x.compareTo(t.info) > 0) {
            t.right = insert(x, t.right);
        } else ;  // Duplicate; do nothing
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     *
     * @param x the item to remove.
     * @param t the node that roots the tree.
     * @return the new root.
     */
    private BNode<E> remove(Comparable x, BNode<E> t) {
        if (t == null)
            return t;   // Item not found; do nothing

        if (x.compareTo(t.info) < 0) {
            t.left = remove(x, t.left);
        } else if (x.compareTo(t.info) > 0) {
            t.right = remove(x, t.right);
        } else if (t.left != null && t.right != null) { // Two children
            t.info = findMin(t.right).info;
            t.right = remove(t.info, t.right);
        } else {
            if (t.left != null)
                return t.left;
            else
                return t.right;
        }
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private BNode<E> findMin(BNode<E> t) {
        if (t == null) {
            return null;
        } else if (t.left == null) {
            return t;
        }
        return findMin(t.left);
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private BNode<E> findMax(BNode<E> t) {
        if (t == null) {
            return null;
        } else if (t.right == null) {
            return t;
        }
        return findMax(t.right);
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return node containing the matched item.
     */
    private BNode<E> find(E x, BNode<E> t) {
        if (t == null)
            return null;

        if (x.compareTo(t.info) < 0) {
            return find(x, t.left);
        } else if (x.compareTo(t.info) > 0) {
            return find(x, t.right);
        } else {
            return t;    // Match
        }
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(BNode<E> t) {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.info);
            printTree(t.right);
        }
    }

    private Set<E> getInorder(BNode<E> node, Set<E> set) {
        if (node == null)
            return set;
        getInorder(node.left, set);
        set.add(node.info);
        getInorder(node.right, set);
        return set;
    }

    Set<E> getInorder(BNode<E> node) {
        return getInorder(node, new TreeSet<>());
    }
}

class AVLNode<E extends Comparable<E>> {

    public E info;
    public AVLNode<E> left;
    public AVLNode<E> right;
    public int height;

    // Constructors
    AVLNode(E theElement) {
        this(theElement, null, null);
    }

    AVLNode(E info, AVLNode<E> left, AVLNode<E> right) {
        this.info = info;
        this.left = left;
        this.right = right;
        height = 0;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

class AVLTree<E extends Comparable<E>> {

    public AVLNode<E> getRoot() {
        return root;
    }

    public int myHeight(AVLNode<E> node) {
        if (node == null) return 0;
        return 1 +Math.max(myHeight(node.left), myHeight(node.right));
    }

    /**
     * The tree root.
     */
    private AVLNode<E> root;

    /**
     * Construct the tree.
     */
    public AVLTree() {
        root = null;
    }

    public void setRoot(AVLNode<E> root) {
        this.root = root;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(E x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(E x) {
        root = remove(x, root);
    }

    // key - x
    // root - t

    private AVLNode<E> remove(E x, AVLNode<E> t) {

        // STEP 1: PERFORM STANDARD BST DELETE
        if (t == null)
            return t;

        // If the key to be deleted is smaller than the root's key,
        // then it lies in left subtree
        if (x.compareTo(t.info) < 0)
            t.left = remove(x, t.left);
        else if (x.compareTo(t.info) > 0)
            t.right = remove(x, t.right);
        else {
            // if key is same as root's key, then This is the node
            // to be deleted
            AVLNode<E> tmp;

            // node with only one child or no child
            if ((t.left == null) || (t.right == null)) {

                if (t.left != null)
                    tmp = t.left;
                else
                    tmp = t.right;

                // no child case
                if (tmp == null) {
                    tmp = t;
                    t = null;
                } else {
                    // one child case
                    t = tmp;
                }
            } else {
                // node with two children: Get the inorder successor (smallest
                // in the right subtree)
                tmp = findMin(t.right);
                // Copy the inorder successor's data to this node
                t.info = tmp.info;
                // Delete the inorder successor
                t.right = remove(tmp.info, t.right);
            }

        }

        // If the tree had only one node then return
        if (t == null)
            return t;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        t.height = max(height(t.left), height(t.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        //  this node became unbalanced)
        int balance = getBalance(t);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if ((balance > 1) && (getBalance(t.left) >= 0))
            return rotateWithLeftChild(t);

        // Left Right Case
        if ((balance > 1) && (getBalance(t.left) < 0))
            return doubleWithLeftChild(t);

        // Right Right Case
        if ((balance < -1) && (getBalance(t.right) <= 0))
            return rotateWithRightChild(t);

        // Right Left Case
        if ((balance < -1) && (getBalance(t.right) > 0))
            return doubleWithRightChild(t);

        return t;
    }

    // Get Balance factor of node N
    int getBalance(AVLNode<E> n) {
        if (n == null)
            return 0;
        return height(n.left) - height(n.right);
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public E findMin() {
        return elementAt(findMin(root));
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public E findMax() {
        return elementAt(findMax(root));
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * @return the matching item or null if not found.
     */
    public E find(E x) {
        return elementAt(find(x, root));
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    /**
     * Internal method to get element field.
     *
     * @param t the node.
     * @return the element field or null if t is null.
     */
    private E elementAt(AVLNode<E> t) {
        if (t == null)
            return null;
        else
            return t.info;
    }

    /**
     * Internal method to insert into a subtree.
     *
     * @param x the item to insert.
     * @param t the node that roots the tree.
     * @return the new root.
     */
    private AVLNode<E> insert(E x, AVLNode<E> t) {
        if (t == null) {
            t = new AVLNode<E>(x, null, null);
        } else if (x.compareTo(t.info) < 0) {
            t.left = insert(x, t.left);
            if (height(t.left) - height(t.right) == 2) {
                if (x.compareTo(t.left.info) < 0) {
                    t = rotateWithLeftChild(t);
                } else {
                    t = doubleWithLeftChild(t);
                }
            }
        } else if (x.compareTo(t.info) > 0) {
            t.right = insert(x, t.right);
            if (height(t.right) - height(t.left) == 2) {
                if (x.compareTo(t.right.info) > 0) {
                    t = rotateWithRightChild(t);
                } else {
                    t = doubleWithRightChild(t);
                }
            }
        } else
            ;  // Duplicate; do nothing
        t.height = max(height(t.left), height(t.right)) + 1;
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AVLNode<E> findMin(AVLNode<E> t) {
        if (t == null) {
            return t;
        }

        while (t.left != null) {
            t = t.left;
        }
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AVLNode<E> findMax(AVLNode<E> t) {
        if (t == null) {
            return t;
        }

        while (t.right != null) {
            t = t.right;
        }
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return node containing the matched item.
     */
    private AVLNode<E> find(E x, AVLNode<E> t) {
        while (t != null) {
            if (x.compareTo(t.info) < 0) {
                t = t.left;
            } else if (x.compareTo(t.info) > 0) {
                t = t.right;
            } else {
                return t;    // Match
            }
        }
        return null;   // No match
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(AVLNode<E> t) {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.info);
            printTree(t.right);
        }
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(AVLNode<E> t) {
        if (t == null)
            return -1;
        else
            return t.height;
    }

    /**
     * Return maximum of lhs and rhs.
     */
    private int max(int lhs, int rhs) {
        if (lhs > rhs)
            return lhs;
        else
            return rhs;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AVLNode<E> rotateWithLeftChild(AVLNode<E> k2) {
        AVLNode<E> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = max(height(k2.left), height(k2.right)) + 1;
        k1.height = max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AVLNode<E> rotateWithRightChild(AVLNode<E> k1) {
        AVLNode<E> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = max(height(k1.left), height(k1.right)) + 1;
        k2.height = max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AVLNode<E> doubleWithLeftChild(AVLNode<E> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AVLNode<E> doubleWithRightChild(AVLNode<E> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }


    ////////////Tree print ////////////
    public void print() {
        // Print a textual representation of this AVL-tree.
        printSubtree(root, "", null);
    }

    private void printSubtree(AVLNode<E> tmp, String indent, AVLNode<E> parent) {
        // Print a textual representation of the subtree of this AVL-tree whose
        // topmost AVLNode is top, indented by the string of spaces indent.
        if (tmp != null) {
            //System.out.println(indent + "o-->");
            String childIndent = indent + "    ";
            printSubtree(tmp.right, childIndent, tmp);

            int total = this.myHeight(root);
            int height = Math.abs(total - tmp.height - 1);
            System.out.println(childIndent + tmp.info + " (" + height + ")");
            //+ (parent == null ? "" : " parent " + parent.info.toString()));
            printSubtree(tmp.left, childIndent, tmp);
        }
    }

    private AVLNode<E> listToTree(List<E> list, int start, int end, int height) {
        if (start > end)
            return null;

        int mid = (start + end) / 2;

        AVLNode<E> node = new AVLNode<E>(list.get(mid));

        node.setHeight(height);

        node.left = listToTree(list, start, mid - 1, height + 1);
        node.right = listToTree(list, mid + 1, end, height + 1);

        return node;
    }

    void listToTree(List<E> list) {
        AVLNode<E> integerBNode = listToTree(list, 0, list.size() - 1, 0);
        this.setRoot(integerBNode);
    }
}

public class MergeTrees {
    public static void main(String [] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        BinarySearchTree<Integer> first = new BinarySearchTree<>();
        BinarySearchTree<Integer> second = new BinarySearchTree<>();

        int n = Integer.parseInt(bufferedReader.readLine());
        int m = Integer.parseInt(bufferedReader.readLine());

        while (n > 0) {
            first.insert(Integer.parseInt(bufferedReader.readLine()));
            n--;
        }

        while (m > 0) {
            second.insert(Integer.parseInt(bufferedReader.readLine()));
            m--;
        }

        Set<Integer> set = first.getInorder(first.getRoot());
        set.addAll(second.getInorder(second.getRoot()));

        AVLTree<Integer> result = new AVLTree<>();
        result.listToTree(new ArrayList<>(set));

        result.print();
    }
}
