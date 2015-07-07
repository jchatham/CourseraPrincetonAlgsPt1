
/**
 * Created by jeff on 7/3/15.
 */

public class Percolation {
    //true = open false = closed
    private boolean otherGrid[];
    private double openedSites = 0;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int N, init;
    private double fractionOpen;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
            if( N <= 0){
                throw new java.lang.IllegalArgumentException("N must be > 0");
            }
            this.init = (N*N) + N;
            weightedQuickUnionUF = new WeightedQuickUnionUF(this.init);

            //java primitive boolean is set to false by default...
            this.otherGrid = new boolean[this.init];
            this.N = N;
            //connect top
            for(int  j= 0; j < N; j++){
                //top
                weightedQuickUnionUF.union(0,j);
            }
            for(int i = 0; i < N; i++){
                this.otherGrid[i] = true;
            }
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
        int site = N*i + j - 1;
        //if not open, open and incrementcounter of opened sites
        if(!this.otherGrid[site]){
            this.otherGrid[site] = true;
            openedSites++;
        }

        unionFlow(i, j, site);
    }

    private void unionFlow(int i, int j, int site) {
        checkInputs(i,j);
        //check for left most
        if(i != 1){
            if(this.isOpen(i-1,j)){
                weightedQuickUnionUF.union(site - 1, site);
            }
        }
        //check for right most
        if(i != N){
            if(this.isOpen(i+1,j)){
                weightedQuickUnionUF.union(site + 1, site);
            }
        }
        //below
        if(j == 1){
            weightedQuickUnionUF.union(site, site - N);
        } else{
            if(this.isOpen(i,j-1)){
                weightedQuickUnionUF.union(site, site - N);
            }
        }

        //above
        if(j != N) {
            if (this.isOpen(i, j + 1)) {
                weightedQuickUnionUF.union(site, site + N);
            }
        }



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
        boolean isPercolating = false;
        for(int i = this.init - this.N - 1; i < this.init; i++){
            isPercolating = weightedQuickUnionUF.connected(0,i);
            //break on true
            if(isPercolating){
                break;
            }
        }
        if(isPercolating){
            this.fractionOpen = this.openedSites/(this.N * this.N);
        }

        //first connected to last.
        return isPercolating;
    }

   // public static void main(String[] args)   // test client (optional)
}

