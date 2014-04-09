import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;


/**
 * Denne ukas oppgaver er like som forrige, bortsett fra at vi nå
 * skulle dele opp matrisen på rader (altså at hver tråd får
 * forskjellige rader). Forrige uke delte vi opp så hver tråd fikk
 * sine kolonner.
 *
 * Som oppgaven også spesifiserte, er det like greit å parallellisere transponeringen av B,
 */
public class Uke4_Oppg1 {
    public static final boolean PRINT_MATRICES = false;// Print the original/result matrices
    private static boolean sequentialTransposedB;      // Sequential transpose B matrix
    private static boolean parallelTransposedB;        // Parallel transpose B matrix

    private double[][] a, b;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage:\n\tUke4_Oppg1 <size of array> " +
                               "<number of tests> <do transpose [true / false]>");
            System.out.println("example:\n\tUke4_Oppg1 500 9 true");
            System.exit(1);
        }

        int n = Integer.parseInt(args[0]);               // Size of arrays
        int numberOfTests = Integer.parseInt(args[1]);   // Number of test runs

    /* Comment out the next line, and comment in the line about
     * parallel to do sequential transposing. */
        //sequentialTransposedB = Boolean.valueOf(args[2]); // Get third argument (true/false)
        parallelTransposedB = Boolean.valueOf(args[2]);  // Get third argument (true/false)

        Uke4_Oppg1 o = new Uke4_Oppg1(n);
        o.startProgram(n, numberOfTests);
    }

    Uke4_Oppg1(int maxsize) {
        a = new double[maxsize][maxsize];
        b = new double[maxsize][maxsize];
    }

    /**
     * Starts the sequential and parallel solution, and manage the results.
     * @param n size of arrays
     * @param numberOfTests number of times it will test
     */
    public void startProgram(int n, int numberOfTests) {
        if (sequentialTransposedB) {
            System.out.println("Transposing is ON  (sequential)\n");
        } else if (parallelTransposedB) {
            System.out.println("Transposing is ON  (parallel)\n");
    } else {
            System.out.println("Transposing is OFF\n");
        }

        double[] sequentiallyResults = new double[numberOfTests];
        double[] parallelResults = new double[numberOfTests];

        for (int i = 0; i < numberOfTests; i++) {
            sequentiallyResults[i] = executeSequentially(n);
            parallelResults[i] = executeParallel(n);

            System.out.printf("\t\t%d pass seq %.1f, par %.1f\n", i,
                              sequentiallyResults[i], parallelResults[i]);
        }

        printResult(sequentiallyResults, parallelResults);

    }

    /**
     * Prints the results in a nice format.
     */
    public void printResult(double[] sequentiallyResults, double[] parallelResults) {
        String tran = "not transposed";

        if (sequentialTransposedB)
            tran = "sequentially transposed";
    else if (parallelTransposedB)
        tran = "parallel transposed";

        // Sort so we can pick the median:
        Arrays.sort(sequentiallyResults);
        Arrays.sort(parallelResults);

        System.out.printf("\nMedian sequentially results: %.2f milliseconds (%s)\n",
                          sequentiallyResults[sequentiallyResults.length/2], tran);
        System.out.printf("Median parallel results:     %.2f milliseconds (%s)\n",
                          parallelResults[parallelResults.length/2], tran);
    }

    /**
     * Fills the global double-arrays "a" and "b" with random numbers
     * between 0.0 and 1.0. Uses a seed, so every time this method is
     * called, the exact same "random" numbers is generated.
     */
    private void populateArrays() {
        Random r = new Random(6734);

        // Populate the arrays with random numbers between 0.0 and 1.0
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
        a[i][j] = r.nextDouble();
        b[i][j] = a[i][j];
            }
        }
    }

    /**
     * Does a sequential execution of the matrix multiplication.
     * @param n size of double-arrays
     * @return time usage in millisec
     */
    public double executeSequentially(int n) {
        populateArrays(); // Reset arrays, in case of transposes or other things

        long start = System.nanoTime();
        double[][] c = new double[n][n]; // Results in here

        if (sequentialTransposedB || parallelTransposedB) {
            inplaceTransposeMatrix(b, n, 0, n);
        }
        multiplyMatrices(a, b, c, 0, n, n);

        if (PRINT_MATRICES) {
            printMatrix(a, n, "a[] seq");
            printMatrix(b, n, "b[] seq");
            printMatrix(c, n, "c[] seq");
        }

        return (System.nanoTime() - start) / 1000000.0;
    }

    /**
     * Does a parallel execution of the matrix multiplication.
     * @param n size of double-arrays
     * @return time usage in millisec
     */
    public double executeParallel(int n) {
        populateArrays(); // Reset arrays, in case of transposes or other things

        double[][] c = new double[n][n];
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads + 1);

        long start = System.nanoTime();

        if (sequentialTransposedB) {
            inplaceTransposeMatrix(b, n, 0, n);
        }

        int elemetsPerThread = n / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            int left = i * elemetsPerThread;
            int right = (i+1) * elemetsPerThread;

            if (i == numberOfThreads-1) {
                // Last thread takes the remainding elements
                right = n;
            }

            new Thread(new MultiplyMatricesWorkerThread(barrier, left, right, a, b, c, n)).start();
        }

        try {
            if (parallelTransposedB) barrier.await();
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (PRINT_MATRICES) {
            printMatrix(a, n, "a[] par");
            printMatrix(b, n, "b[] par");
            printMatrix(c, n, "c[] par");
        }

        return (System.nanoTime() - start) / 1000000.0;
    }

    /**
     * Multiply two matrices and store in a third.
     *
     * @param a first matrix to be multiplied
     * @param b second matrix to be multiplied
     * @param c result of multiplication
     * @param top top row in C to start multiplication
     * @param bottom bottom row in C to end multiplication
     * @param n dimensions of the matrices
     */
    public void multiplyMatrices(double[][] a, double[][] b, double[][] c,
                 int top, int bottom, int n) {
        double elem;

        // for each row in C
        for (int i = top; i < bottom; i++) {
            // for each column in C
            for (int j = 0; j < n; j++) {
                elem = 0.0;
                if (parallelTransposedB || sequentialTransposedB) {
                    for (int k = 0; k < n; k++) {
                        elem += a[i][k]*b[j][k];
                    }
                } else {
                    for (int k = 0; k < n; k++) {
                        elem += a[i][k]*b[k][j];
                    }
                }
                c[i][j] = elem;
            }
        }
    }

    /**
     * Inplace transpose of a matrix. Only a "n" times "n" matrix is
     * transposed, even if the matrix is larger.
     *
     * @param matrix matrix to be inplace transposed
     * @param n dimensions of the matrix
     */
    public void inplaceTransposeMatrix(double[][] matrix, int n, int left, int right) {
        for (int i = left; i < right; i++) {

            // Switch with column (do the transposing)
            for (int j = i+1; j < n; j++) {
                double temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }


    /**
     * Prints the matrix given as parameter. How much of the matrix
     * that is actually printed is controlled by the second arameter,
     * if it is 5, only a 5x5 matrix will be printed (even if the
     * original matrix is 10x10).
     *
     * @param matrix matrix to print
     * @param n dimensions of the matrix to print
     * @param text name of this matrix and if used in par/seq algorithm
     */
    public void printMatrix(double[][] matrix, int n, String text) {
        System.out.printf("Matrix with dimensions %d x %d. %s\n",
                          n, n, text);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%5.2f ", matrix[i][j]);
            }
            System.out.println();
        }

        System.out.println();
    }


    /**
     * Threads calling the matrix multiplication method on their part
     * of the matrix.
     */
    class MultiplyMatricesWorkerThread implements Runnable {
        final int top, bottom; // gives the range of rows to multiply
    final int n;           // total size of this matrix
        final double[][]
        a,                 // first matrix to be multiplied
        b,                 // second matrix to be multiplied
        c;                 // results goes in here
        CyclicBarrier barrier;

        MultiplyMatricesWorkerThread(CyclicBarrier barrier, int top, int bottom,
                                     double[][] a, double[][] b, double[][] c, int n) {
            this.barrier = barrier;
            this.top = top;
            this.bottom = bottom;
            this.a = a;
            this.b = b;
            this.c = c;
            this.n = n;
        }

        public void run() {

            try {
                if (parallelTransposedB) {
            // If parallel transposing, do that and wait for
            // the other threads
                    inplaceTransposeMatrix(b, n, top, bottom);
                    barrier.await();
                }

        // The matrix is now ready (transposed or not), do the mult:
                multiplyMatrices(a, b, c, top, bottom, n);
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}