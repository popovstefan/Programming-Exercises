package aps.apteka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}


class CBHT<K extends Comparable<K>, E> {

    // An object of class CBHT is a closed-bucket hash table, containing
    // entries of class MapEntry.
    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        // Construct an empty CBHT with m buckets.
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        // Translate key to an index of the array buckets.
        String kkey = (String) key;
        return ((29 * (29 * kkey.charAt(0) + kkey.charAt(1)) + kkey.charAt(2)) % 102780) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
        // Find which if any node of this CBHT contains an entry whose key is
        // equal
        // to targetKey. Return a link to that node (or null if there is none).
        int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(curr.element.key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {		// Insert the entry <key, val> into this CBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(curr.element.key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }
        // Insert newEntry at the front of the 1WLL in bucket b ...
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}


class Lek {
    String name;
    int positiveList;
    int cena;
    int zaliha;

    public Lek(String name, int positiveList, int cena, int zaliha) {
        this.name = name;
        this.positiveList = positiveList;
        this.cena = cena;
        this.zaliha = zaliha;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%d\n%d", name, positiveList > 0 ? "POS" : "NEG", cena, zaliha);
    }
}

public class Apteka {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        CBHT<String, Lek> lekovi = new CBHT<>(n + 1);

        while (n > 0) {
            String [] lineParts = bufferedReader.readLine().split("\\s+");

            String name = lineParts[0].toLowerCase();
            int positiveList = Integer.parseInt(lineParts[1]);
            int cena = Integer.parseInt(lineParts[2]);
            int zaliha = Integer.parseInt(lineParts[3]);

            lekovi.insert(name, new Lek(name, positiveList, cena, zaliha));
            n--;
        }

        String lekLine = bufferedReader.readLine();

        while (! lekLine.equals("KRAJ")) {
            int requested = Integer.parseInt(bufferedReader.readLine());

            SLLNode<MapEntry<String, Lek>> node = lekovi.search(lekLine.toLowerCase());
            if (node == null)
                System.out.println("Nema takov lek");
            else if (requested > node.element.value.zaliha)
                System.out.println("Nema dovolno lekovi");
            else {
                //node.element.value.zaliha -= requested;
                System.out.println(node.element.value);
                System.out.println("Napravena naracka");
            }

            lekLine = bufferedReader.readLine();
        }

    }
}
