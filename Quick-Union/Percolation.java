import edu.princeton.cs.algs4.WeightedQuickUnionUF;
// javac-algs4 Percolation.java && java-algs4 Percolation
public class Percolation {

    private byte[][] siteStatus;
    private int numOpenSites;
    private final    WeightedQuickUnionUF unionObj;
    private boolean percCheck;
    private final int theEn;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Enter value of n greater than 0");
        }
        theEn = n;
        numOpenSites = 0;
        // Set to true whenever a node is opened which connects to top and bottom
        percCheck = false;
        // Using 1 index
        unionObj = new WeightedQuickUnionUF((n*n) + 2);
        // Using 1 Index
        siteStatus = new byte[n+1][n+1];

        // Initializing top values to be 4, meaning they are connected to the top
        for (int i = 1; i < siteStatus[1].length; i++) {
            siteStatus[1][i] |= 4;
        }
        // Initializing bottom values to be 2, meaning they are connected to the bottom
        for (int i = 1; i < siteStatus[n].length; i++) {
            siteStatus[n][i] |= 2;
        }
    }

    private int converterOneDee(int row, int col) {
        return ((row - 1) * theEn + col);
    }
    private int convertTwoDeeRow(int root) {
        if (root % theEn == 0) {
            return ((root / theEn));
        }
        return ((root / theEn) + 1);
    }
    private int convertTwoDeeCol(int root) {
        if (root % theEn == 0) {
            return theEn;
        }
        else {
            return (root % theEn);
        }
    }

    private void exceptionHandler(int row, int col) {
        if (row <= 0 || row > theEn) {
            throw new IllegalArgumentException("row index i out of bounds");
        }
        if (col <= 0 || col > theEn) {
            throw new IllegalArgumentException("col index i out of bounds");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        exceptionHandler(row, col);

        int root;
         if (isOpen(row, col)) {
            return;
         }
         else {
            // Increasing number of open sites
            numOpenSites++;
            // Bit Manipulation to set the set's status to open
            siteStatus[row][col] |= 1;

            if ((col - 1 > 0) && isOpen(row, col - 1)) {
                root = unionObj.find(converterOneDee(row, col-1));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
                siteStatus[row][col] |= siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)];
                unionObj.union(converterOneDee(row, col), converterOneDee(row, col - 1));
                root = unionObj.find(converterOneDee(row, col - 1));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
            }

            if ((row - 1 > 0) && isOpen(row - 1, col)) {
                root = unionObj.find(converterOneDee(row-1, col));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
                siteStatus[row][col] |= siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)];
                unionObj.union(converterOneDee(row, col), converterOneDee(row - 1, col));
                root = unionObj.find(converterOneDee(row - 1, col));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
            }

            if ((row + 1 < theEn + 1) && isOpen(row + 1, col)) {
                root = unionObj.find(converterOneDee(row+1, col));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
                siteStatus[row][col] |= siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)];
                unionObj.union(converterOneDee(row, col), converterOneDee(row + 1, col));
                root = unionObj.find(converterOneDee(row + 1, col));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
            }

            if ((col + 1 < theEn + 1) && isOpen(row, col + 1)) {
                root = unionObj.find(converterOneDee(row, col + 1));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
                siteStatus[row][col] |= siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)];
                unionObj.union(converterOneDee(row, col), converterOneDee(row, col+1));
                root = unionObj.find(converterOneDee(row, col + 1));
                siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] |= siteStatus[row][col];
            }

             root = unionObj.find(converterOneDee(row, col));
             if (siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] == 7) {
                percCheck = true;
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        exceptionHandler(row, col);

        if (siteStatus[row][col] == 1 || siteStatus[row][col] == 3 || siteStatus[row][col] == 5 || siteStatus[row][col] == 7) {
            return true;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        exceptionHandler(row, col);
        int root = unionObj.find(converterOneDee(row, col));
        if (siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] == 5 || siteStatus[convertTwoDeeRow(root)][convertTwoDeeCol(root)] == 7) {
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percCheck;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(1);
        perc.open(1, 1);
        //
        // System.out.println(perc.isOpen(1, 3));
        // perc.open(2, 3);
        // perc.open(2, 2);
        // perc.open(1, 3);
        // perc.open(4, 3);
        // perc.open(5, 3);
        //
        // perc.open(3, 2);
        // perc.open(4, 2); // 5
        //
        // System.out.println("isOpen: " + perc.isOpen(1, 3));
        // System.out.println("isFull: " +perc.isFull(2, 2));
        // System.out.println("isFull: " +perc.isFull(2, 3));
        // System.out.println("num: " + perc.numberOfOpenSites());
        System.out.println("Percz: " + perc.percolates());
        // System.out.println(perc.isFull(5, 3));
        // // System.out.println(perc.siteStatus[4][3]);
        // System.out.println(perc.siteStatus[perc.convertTwoDeeRow(perc.unionObj.find(perc.converterOneDee(5, 3)))][perc.convertTwoDeeCol(perc.unionObj.find(perc.converterOneDee(5, 3)))]);
    }
}