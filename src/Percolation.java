
/**
 * Created by jeff on 7/3/15.
 */

public class Percolation {
    //true = open false = closed
    boolean grid[][], otherGrid[];
    int openedSites = 0;
    WeightedQuickUnionUF weightedQuickUnionUF;
    int N, init;
    double fractionOpen;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
            if( N <= 0){
                throw new java.lang.IllegalArgumentException("N must be > 0");
            }
            this.init = (N*N) + (2*N);
            weightedQuickUnionUF = new WeightedQuickUnionUF(this.init);

            //java primitive boolean is set to false by default...
            this.grid = new boolean[N+2][N];
            this.otherGrid = new boolean[this.init];
            //set top and bottom rows to connected
            for(int  j= 0; j < N; j++){
                //top
                weightedQuickUnionUF.union(0,j);
                //bottom
                weightedQuickUnionUF.union((N*N) + N, (N*N) + (N + j));
            }
        this.N = N;
    }

    private void checkInputs(int i, int j){
        if (i <= 0 || j <= 0){
            throw new java.lang.IllegalArgumentException("i and j BOTH must be > 0. i = " + i + " j = " + j + " N = " + this.N);
        }
        if (i > N || j > N){
            throw new java.lang.IllegalArgumentException("i and j BOTH must be < N. i = " + i + " j = " + j + " N = " + this.N);
        }
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j){
        checkInputs(i,j);
        this.grid[i][j-1] = true;
        int site = N*i + j - 1;
        this.otherGrid[site] = true;
        openedSites++;
        unionFlow(i, j, site);
    }

    private void unionFlow(int i, int j, int site) {
        //check for left most
        if(i % N != 0){
            if(this.isOpen(i-1,j)){
                weightedQuickUnionUF.union(site, site - 1);
            }
        }
        //check for right most
        if(i % (N +1) != 0){
            if(this.isOpen(i-1,j)){
                weightedQuickUnionUF.union(site, site + 1);
            }
        }
        //below
        weightedQuickUnionUF.union(site, site + N);
        //above
        weightedQuickUnionUF.union(site, site - N);
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        checkInputs(i,j);
        return (this.otherGrid[N*i + j - 1]);
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j){
        checkInputs(i,j);
        if(!isOpen(i,j-1)){
            return false;
        }
        return(weightedQuickUnionUF.connected(0,j-1));
    }
    // does the system percolate?
    public boolean percolates(){
        boolean isPercolating = weightedQuickUnionUF.connected(0,this.init - 1);
        this.fractionOpen = this.openedSites/this.N;
        //first connected to last.
        return isPercolating;
    }

   // public static void main(String[] args)   // test client (optional)
}

