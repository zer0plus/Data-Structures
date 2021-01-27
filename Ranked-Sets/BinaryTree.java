package comp2402a3;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class BinaryTree implements PrefixStack {
    int arr[];
    long sum_arr[];
    int int_temp[];
    long sum_temp[];
    int size, length;


    public RyabkoTree() {

        length = 2;
        arr = new int[length+1];
        sum_arr = new long[length+1];
        size = 0;

    }

    public void push(int x) {
        set(size++, x);
    }

    public int pop() {
        size--;
        int i = size + 1;
        int tem = i;
        int tem2 = arr[i];
        int p = 0;

        //rebalance
        if (size >= length){
            length *= 2;
            int_temp = new int[length+1];
            sum_temp = new long[length+1];
            System.arraycopy(sum_arr, 1, sum_temp, 1, size);
            System.arraycopy(arr, 1, int_temp, 1, size);
            sum_temp[length] = sum_arr[size];
            arr = int_temp;
            sum_arr= sum_temp;
        }

        while(i <= length+1) {
            sum_arr[i] += p-arr[tem];
            i += i & -i;
        }

        arr[tem] = p;
        return tem2;

    }


    public int set(int i, int x) {
        i++;
        int tem = i;
        int tem2 = arr[i];
        //rebalance
        if (size >= length){
            length *= 2;
            int_temp = new int[length+1];
            sum_temp = new long[length+1];
            System.arraycopy(sum_arr, 1, sum_temp, 1, size);
            System.arraycopy(arr, 1, int_temp, 1, size);
            sum_temp[length] = sum_arr[size];
            arr = int_temp;
            sum_arr= sum_temp;
        }
        while(i <= length+1) {
            sum_arr[i] += x-arr[tem];
            i += i & -i;
        }
        arr[tem] = x;
        return tem2;
    }




    public int get(int i) {
        return arr[i+1];
    }

    public long prefixSum(int i) {
        i++;
        long sum = 0;
        while (i > 0) {
            sum += sum_arr[i];
            i &= ~lsb(i);
        }
        return sum;
    }

    private static int lsb(int i) {

        // Isolates the lowest one bit value
        return i & -i;

    }


    public int size() {
        return size;
    }

    public Iterator<Integer> iterator() {
        return Arrays.stream(arr).iterator();
    }

}


