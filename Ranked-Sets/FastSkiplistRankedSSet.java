package comp2402a3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/**
 * An implementation of skiplists for searching
 *
 * @param <T>
 * @author morin
 */
public class FastSkiplistRankedSSet<T> implements RankedSSet<T> {
    protected Comparator<T> c;

    @SuppressWarnings("unchecked")
    protected static class Node<T> {
        int[] ln;
        T x;
        Node<T>[] next;

        public Node(T ix, int h) {
            x = ix;
            next = (Node<T>[]) Array.newInstance(Node.class, h + 1);
            ln = new int[h + 1];
        }

        public int height() {
            return next.length - 1;
        }
    }

    /**
     * This node<T> sits on the left side of the skiplist
     */
    protected Node<T> sentinel;

    /**
     * The maximum height of any element
     */
    int h;

    /**
     * The number of elements stored in the skiplist
     */
    int n;

    /**
     * A source of random numbers
     */
    Random rand;

    /**
     * Used by add(x) method
     */
    protected Node<T>[] stack;

    @SuppressWarnings("unchecked")
    public FastSkiplistRankedSSet(Comparator<T> c) {
        this.c = c;
        n = 0;
        sentinel = new Node<T>(null, 32);
        stack = (Node<T>[]) Array.newInstance(Node.class, sentinel.next.length);
        h = 0;
        rand = new Random();
    }

    public FastSkiplistRankedSSet() {
        this(new DefaultComparator<T>());
    }

    /**
     * Find the node<T> u that precedes the value x in the skiplist.
     *
     * @param x - the value to search for
     * @return a node<T> u that maximizes u.x subject to
     * the constraint that u.x < x --- or sentinel if u.x >= x for
     * all node<T>s x
     */
    protected Node<T> findPredNode(T x) {
        Node<T> u = sentinel;
        int r = h;
        while (r >= 0) {
            while (u.next[r] != null && c.compare(u.next[r].x, x) < 0)
                u = u.next[r];   // go right in list r
            r--;               // go down into list r-1
        }
        return u;
    }

    public T find(T x) {
        Node<T> u = findPredNode(x);
        return u.next[0] == null ? null : u.next[0].x;
    }

    public T findGE(T x) {
        if (x == null) {   // return first node<T>
            return sentinel.next[0] == null ? null : sentinel.next[0].x;
        }
        return find(x);
    }

    public T findLT(T x) {
        if (x == null) {  // return last node<T>
            Node<T> u = sentinel;
            int r = h;
            while (r >= 0) {
                while (u.next[r] != null)
                    u = u.next[r];
                r--;
            }
            return u.x;
        }
        return findPredNode(x).x;
    }

    public Node<T> p_find(int e) {
        Node z = sentinel;
        int a = -1;
        int b = h;

        while (b >= 0) {
            while (z.next[b] != null && a + z.ln[b] < e) {
                a += z.ln[b];
                z = z.next[b];
            }
            b--;
        }
        return z;
    }

    public T get(int i) {
        return p_find(i).next[0].x;
    }

    public int rank(T x) {
        Node<T> z = sentinel;
        int a = 0;
        int b = h;
        while (b >= 0) {
            while (z.next[b] != null && c.compare(z.next[b].x, x) < 0) {
                a = a + z.ln[b];
                z = z.next[b];
            }
            b--;
        }
        return a;
    }

    public boolean remove(T x) {
        boolean removed = false;
        Node<T> z = sentinel;
        int a = -1;
        int b = h;
        int comp = 0;
        while (b >= 0) {
            while (z.next[b] != null && (comp = c.compare(z.next[b].x, x)) < 0 ) {
                a = z.ln[b];
                z = z.next[b];
            }
            z.ln[b]--;
            if (comp == 0 && z.next[b] != null) {
                x = z.next[b].x;
                z.ln[b] = z.ln[b] + z.next[b].ln[b];
                z.next[b] = z.next[b].next[b];
                removed = true;
                if (z == sentinel && z.next[b] == null) {
                    h--;
                }
            }
            b--;
        }
        if (removed) {
            n--;
        }
        return removed;
    }

    /**
     * Simulate repeatedly tossing a coin until it comes up tails.
     * Note, this code will never generate a height greater than 32
     *
     * @return the number of coin tosses - 1
     */
    protected int pickHeight() {
        int z = rand.nextInt();
        int k = 0;
        int m = 1;
        while ((z & m) != 0) {
            k++;
            m <<= 1;
        }
        return k;
    }

    public void clear() {
        n = 0;
        h = 0;
        Arrays.fill(sentinel.next, null);
    }

    public int size() {
        return n;
    }

    public Comparator<T> comparator() {
        return c;
    }

    /**
     * Create a new iterator in which the next value in the iteration is u.next.x
     * TODO: Constant time removal requires the use of a skiplist finger (a stack)
     *
     * @param u
     * @return
     */
    protected Iterator<T> iterator(Node<T> u) {
        class SkiplistIterator implements Iterator<T> {
            Node<T> u, prev;

            public SkiplistIterator(Node<T> u) {
                this.u = u;
                prev = null;
            }

            public boolean hasNext() {
                return u.next[0] != null;
            }

            public T next() {
                prev = u;
                u = u.next[0];
                return u.x;
            }

            public void remove() {
                // Not constant time
                FastSkiplistRankedSSet.this.remove(prev.x);
            }
        }
        return new SkiplistIterator(u);
    }

    public Iterator<T> iterator() {
        return iterator(sentinel);
    }

    public Iterator<T> iterator(T x) {
        return iterator(findPredNode(x));
    }

    public int get_idx(int i, Node<T> n) {
        int a = -1;
        int b = h;
        int e = n.height();
        Node<T> z = sentinel;
        i++;
        while (b >= 0) {
            while ((z.next[b] != null) && (a + z.ln[b] < i)) {
                a = a + z.ln[b];
                z = z.next[b];
            }
            z.ln[b]++;
            if (b <= e) {
                n.next[b] = z.next[b];
                z.next[b] = n;
                n.ln[b] = z.ln[b] - (i - a);
                z.ln[b] = i - a;
            }
            b--;
        }
        return a;
    }

    public boolean add(T x) {
        Node<T> z = sentinel;
        Node<T> zy = new Node<>(x, pickHeight());
        int a = -1;
        int b = h;
        int comp = 0;
        while (b >= 0) {
            while (z.next[b] != null && (comp = c.compare(z.next[b].x, x)) < 0) {
                a = a + z.ln[b];
                z = z.next[b];
            }
            if (z.next[b] != null && comp == 0) {
                return false;
            }
            b--;
        }
        if (zy.height() > h) {
            h = zy.height();
        }
        get_idx(a, zy);
        n++;
        return true;
    }

    public static void main(String[] args) {
        int n = 20;

        Random rand = new java.util.Random();
        RankedSSet<Integer> rss = new FastSkiplistRankedSSet<>();

        for (int i = 0; i < n; i++) {
            rss.add(rand.nextInt(3 * n));
        }

        System.out.print("Contents: ");
        for (Integer x : rss) {
            System.out.print(x + ",");
        }
        System.out.println();

        System.out.println("size()=" + rss.size());

        for (int i = 0; i < rss.size(); i++) {
            Integer x = rss.get(i);
            System.out.println("get(" + i + ")=" + x);
        }

        for (Integer x = 0; x < 3 * n + 1; x++) {
            int i = rss.rank(x);
            System.out.println("rank(" + x + ")=" + i);
        }
    }
}
