package comp2402a4;
import java.util.Iterator;
import java.util.Arrays;

public class RyabkoTree implements PrefixStack {
    int sz;
    int lng;
    long store[];
    long buffTotal[];
    int inpVal[], buffVal[];
    public RyabkoTree() {
        sz = 0;
        lng = 2;
        inpVal = new int[lng + 1];
        store = new long[lng + 1];
    }
    private static int lsb(int i) {
        return Integer.lowestOneBit(i); //i & --i;
    }

    public void push(int x) {
        set(sz++, x);
    }

    public int pop() {
        int var = 0;
        sz--;
        int i = sz + 1;
        int z1 = i, y2 = inpVal[i];
        if (sz >= lng) {
            lng *= 2;
            buffVal = new int[lng + 1];
            buffTotal = new long[lng + 1];
            System.arraycopy(inpVal, 1, buffVal, 1, sz);
            System.arraycopy(store, 1, buffTotal, 1, sz);
            buffTotal[lng] = store[sz];
            store = buffTotal;
            inpVal = buffVal;
        }
        while (i <= lng + 1) {
            store[i] += var - inpVal[z1];
            i = i + Integer.lowestOneBit(i);
        }
        inpVal[z1] = var;
        return y2;
    }
    public int get(int i) {
        return inpVal[i + 1];
    }
    public int set(int i, int x) {
        i++;
        int z2 = i;
        int y1 = inpVal[i];
        if (sz >= lng) {
            lng *= 2;
            buffVal = new int[lng + 1];
            buffTotal = new long[lng + 1];
            System.arraycopy(store, 1, buffTotal, 1, sz);
            System.arraycopy(inpVal, 1, buffVal, 1, sz);
            buffTotal[lng] = store[sz];
            inpVal = buffVal;
            store = buffTotal;
        }
        while (i <= (lng + 1)) {
            store[i] += x - inpVal[z2];
            i += Integer.lowestOneBit(i);
        }
        inpVal[z2] = x;
        return y1;
    }

    public long prefixSum(int i) {
        i++;
        long totalStore = 0;
        while (i > 0) {
            totalStore += store[i];
            i &= ~lsb(i);
        }
        return totalStore;
    }

    public int size() {
        return sz;
    }

    public Iterator<Integer> iterator() {
        return (Arrays.stream(inpVal).iterator());
    }
}


