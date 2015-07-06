public class PercolationStats {
    private int N, T;
    private double mean, stdDev, ninetyFivePercentConfidence;
    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getT() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T){
        setN(N);
        setT(T);
        if(N <= 0){
            throw new IllegalArgumentException("N must be > 0");
        }
        if(T <= 0){
            throw new IllegalArgumentException("T must be > 0");
        }
        Percolation [] percolation = new Percolation[T];
        for(int i = 0; i < T; i++){
            percolation[i] = new Percolation(N);
        }
        for(Percolation p : percolation){
            runExperiment(p);
        }
        for(Percolation p : percolation){
            this.mean += p.fractionOpen;
        }
        //computes mean
        this.mean /= percolation.length;
        //finds std dev
        for(Percolation p : percolation){
            this.stdDev += Math.pow((p.fractionOpen - this.mean), 2);
        }
        this.stdDev /= (percolation.length- 1);
        this.stdDev = Math.sqrt(this.stdDev);

        ninetyFivePercentConfidence = (1.96 * this.stdDev) / Math.sqrt(percolation.length);
    }
    // sample mean of percolation threshold
    public double mean(){
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return stdDev;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return this.mean - this.ninetyFivePercentConfidence;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return this.mean - this.ninetyFivePercentConfidence;
    }

        private void runExperiment(Percolation p){
            while(!p.percolates()){
                //god, why have you forsaken me. this logic should be in percolation.java, but i'm forced to a predefined api.
                int x = StdRandom.uniform(1, p.N);
                /*int y = StdRandom.uniform(p.N, p.init - p.N);*/
                int y = StdRandom.uniform(1, p.N);
                p.open(x, y);
            }

        }

    // test client (described below)
    public static void main(String[] args)   {
        //int arg1 = StdIn.readInt(), arg2 = StdIn.readInt();
        int arg1 = 5, arg2 = 25;
        PercolationStats percolationStats = new PercolationStats(arg1, arg2);
        System.out.println("Mean: " + percolationStats.mean());
        System.out.println("Standard Deviation: " + percolationStats.stddev());
        System.out.println("95% confidence level high " + percolationStats.confidenceHi());
        System.out.println("95% confidence level low " + percolationStats.confidenceLo());
    }
}
