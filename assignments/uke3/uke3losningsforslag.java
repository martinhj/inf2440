import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

/**
 * Sist i dette løsningsforslaget (rett etter tråd-klassen) så er det
 * fire metoder, den første printer ut matrisene og de tre siste er
 * kun for å sjekke at alle beregninger fungerer som de skal, disse er
 * ikke nødvendige for løsningen, men er altså kun for å sjekke at
 * resultatet stemmer.
 */
public class Uke3_Oppg1 {
    public static final boolean PRINT_MATRICES = false; // Print the original/result matrices
    public static final boolean DO_CHECKING = false;    // Check for correct result
    private static boolean transposedB;                 // Transpose B matrix
    private double[][] a, b;

    public static void main(String[] args) {
	if (args.length != 3) {
	    System.out.println("Usage:\n\tUke3_Oppg1 <size of array> " +
			       "<number of tests> <do transpose [true / false]>");
	    System.out.println("example:\n\tUke3_Oppg1 500 9 true");
	    System.exit(1);
	}

	int n = Integer.parseInt(args[0]);              // Size of arrays
	int numberOfTests = Integer.parseInt(args[1]);  // Number of test runs
	transposedB = Boolean.valueOf(args[2]);         // Gives the boolean meaning of a String

        Uke3_Oppg1 o = new Uke3_Oppg1(n);
	o.startProgram(n, numberOfTests);
    }

    Uke3_Oppg1(int maxsize) {
        a = new double[maxsize][maxsize];
        b = new double[maxsize][maxsize];
    }

    /**
     * Starts the sequential and parallel solution, and manage the results.
     * @param n size of arrays
     * @param numberOfTests number of times it will test
     */
    public void startProgram(int n, int numberOfTests) {
        checkCorrectMatrixMultiplication(100);          // Test of the multiplication algorithm

        if (transposedB) {
            System.out.println("Transposing is ON\n");
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

	if (transposedB)
	    tran = "transposed";

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
                b[i][j] = r.nextDouble();
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
        double[][] c = new double[n][n];

        if (transposedB) {
            inplaceTransposeMatrix(b, n);
        }
        multiplyMatrices(a, b, c, 0, n, n);

        if (PRINT_MATRICES) {
            printMatrix(a, n);
            printMatrix(b, n);
            printMatrix(c, n);
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

        long start = System.nanoTime();

        double[][] c = new double[n][n];
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads + 1);

        if (transposedB) {
            inplaceTransposeMatrix(b, n);
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
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (PRINT_MATRICES) {
            printMatrix(a, n);
            printMatrix(b, n);
            printMatrix(c, n);
        }

        if (DO_CHECKING) {
            checkParallel(a, b, c, n);
        }

        return (System.nanoTime() - start) / 1000000.0;
    }

    /**
     * Multiply two matrices and store in a third.
     *
     * @param a first matrix to be multiplied
     * @param b second matrix to be multiplied
     * @param c result of multiplication
     * @param left left column in C to start multiplication
     * @param right right column in C to start multiplication
     * @param n dimensions of the matrices
     */
    public void multiplyMatrices(double[][] a, double[][] b, double[][] c,
				 int left, int right, int n) {
        checkMatrixSize(a, n, "multiply");
        checkMatrixSize(b, n, "multiply");
        checkMatrixSize(c, n, "multiply");

        double elem;

        // for each row in C
        for (int i = 0; i < n; i++) {
            // for each column in C
            for (int j = left; j < right; j++) {
                elem = 0.0;
                if (transposedB) {
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
    public void inplaceTransposeMatrix(double[][] matrix, int n) {
        checkMatrixSize(matrix, n, "transpose");

        for (int i = 0; i < n; i++) {

            // Less than "i" because we don't want double transpose,
            // and end up with what we had
            for (int j = 0; j < i; j++) {
                double temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }


    /**
     * Threads calling the matrix multiplication method on their part
     * of the matrix.
     */
    class MultiplyMatricesWorkerThread implements Runnable {
        final int left, right, n;
        final double[][] a, b, c;
        CyclicBarrier barrier;

        MultiplyMatricesWorkerThread(CyclicBarrier barrier, int left, int right,
                                     double[][] a, double[][] b, double[][] c, int n) {
            this.barrier = barrier;
            this.left = left;
            this.right = right;
            this.a = a;
            this.b = b;
            this.c = c;
            this.n = n;
        }

        public void run() {
            multiplyMatrices(a, b, c, left, right, n);

            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
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
     */
    public void printMatrix(double[][] matrix, int n) {
        checkMatrixSize(matrix, n, "print");

        System.out.printf("Matrix with dimensions %d x %d. \n",
                          n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%5.2f ", matrix[i][j]);
            }
            System.out.println();
        }

        System.out.println();
    }

    /**
     * Checks that the matrix given has dimension larger than "n", and
     * prints an error message containing given string if not.
     *
     * @param matrix matrix to be checked
     * @param n proposed dimension
     * @param action string to be contained in error message
     */
    public void checkMatrixSize(double[][] matrix, int n, String action) {
        if (DO_CHECKING) {
            if (n > matrix.length) {
                System.err.printf("Matrix has dimension %d, but tried to %s with dimension %d\n",
                                  matrix.length, action, n);
                System.exit(1);
            }
        }
    }


    /**
     * Multiplies two matrices with all values set to 1.0, and then
     * checks to see if all the values in the resulting array is set
     * to "n", which it should be. Quits with error message if not
     * equal.
     *
     * @param n size of matrices
     */
    public void checkCorrectMatrixMultiplication(int n) {
        System.out.print("Executes tests for multiplyMatrices()... ");

        double[][] a = new double[n][n];
        double[][] b = new double[n][n];
        double[][] c = new double[n][n];

	// Populate arrays with only 1.0's
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 1.0;
                b[i][j] = 1.0;
            }
        }

        multiplyMatrices(a, b, c, 0, n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
		double res = ((double) n) - c[i][j];

		// 0.01 is the margin of error, as double-values can't
		// be compared directly.
		if (res >= 0.01) {
		    System.err.print(" ERROR!");
		    System.out.printf("\t margin of error to big: %.2f - %.2f = %.2f\n", (double)n, c[i][j], res);
		    System.exit(1);
		}
            }
        }

        System.out.println(" PASSED!");
    }

    /**
     * Does a sequential calculation on the same arrays as the
     * parallel did, and check if the result from the parallel
     * calculation is the same as the sequential. Quits with error
     * message if not equal.
     *
     * @param a the "a" matrix from the parallel calculation
     * @param b the "b" matrix from the parallel calculation
     * @param c the result matrix from the parallel calculation
     * @param n dimension of all the matrices
     */
    public void checkParallel(double[][] a, double[][] b, double[][] c, int n) {
        // First check that it multiplies correct sequentially
        checkCorrectMatrixMultiplication(n);

	System.out.print("Executes tests for parallel solution... ");

	// Store the sequential results here
        double [][] d = new double[n][n];

        // Then calculate the correct result
        multiplyMatrices(a, b, d, 0, n, n);

        // And then check that the parallel result equals the sequentially result
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
		double res = d[i][j] - c[i][j];

		// 0.01 is the margin of error, as double-values can't
		// be compared directly.
		if (res >= 0.01) {
		    System.err.print(" ERROR!");
		    System.out.printf("\t margin of error to big: %.2f - %.2f = %.2f\n", d[i][j], c[i][j], res);
		    System.exit(1);
		}
            }
        }

	System.out.println(" PASSED!");
    }
}
