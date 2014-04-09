import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

/**
 * First step in radix sort
 */
public class Uke5_Oppg1 {
    public static final int NUMBER_OF_THREADS   = Runtime.getRuntime().availableProcessors();
    public static final int HOUNDRED_MILLIONS   = 100000000;

    private final int numberOfTests             = 9;     // number of tests runs (for median)
    private final boolean printDebugData        = false; // set to true to enable debug prints
    private final int numBits;                           // Number of bits in a digit
    private final int numSif;                            // Number of different digits

    private int[][] allCount;                            // Temporary storage for each thread
    private int[] sumCount;                              // Final result
    private int[] a;                                     // Problem data (numbers to sort)

    public static void main(String[] args) {
	int tempBits = 10;

	if (args.length == 1) {
	    tempBits = Integer.parseInt(args[0]);
	}

        Uke5_Oppg1 o = new Uke5_Oppg1(tempBits);
        o.startProgram();
    }

    Uke5_Oppg1(int numBits) {
        this.numBits = numBits;

        // Number of digits in a number == 2^numBits
        this.numSif = (int) Math.pow(2.0, (double) numBits);

        allCount = new int[NUMBER_OF_THREADS][];
        sumCount = new int[numSif];
    }

    /**
     * Starts the sequential and parallel solution, and manage the
     * results.
     */
    public void startProgram() {
	System.out.printf("numSif: %d - bits: %d - numberOfThreads: %d - tests: %d\n",
			  numSif, numBits, NUMBER_OF_THREADS, numberOfTests);

        double[] sequentiallyResults = new double[numberOfTests];
        double[] parallelResults = new double[numberOfTests];

        boolean doTests = true;    // Compare sequential to parallel
        int[] tempSumCount = null; // Used for testing only

        for (int n = HOUNDRED_MILLIONS; n > 1000; n /= 10) {

            for (int i = 0; i < numberOfTests; i++) {
                sequentiallyResults[i] = executeSequentially(n);

                if (doTests) {
		    // Assumes that the sequential does it correct
                    tempSumCount = sumCount.clone();
                }

                parallelResults[i] = executeParallel(n);

                if (doTests) {
                    compareAgainstGlobalResult(tempSumCount);
                    doTests = false; // Only test once
                }
            }

            printResult(sequentiallyResults, parallelResults, n);
        }
    }

    /**
     * Does a sequential execution of the second step of radix sort
     * @param n size of problem data
     * @return time usage in millisec
     */
    public double executeSequentially(int n) {
        createAndPopulateArrays(n); // Create arrays

        long start = System.nanoTime();

        if (printDebugData) {
            System.out.println("Problem data: ");
            for (int i = 0; i < a.length; i++) {
                System.out.println("a[" + i + "]: " + a[i]);
            }
            System.out.println();
        }

        radixSort(0, n, sumCount, numBits, 0);

        if (printDebugData) {
	    System.out.println("Frequency of each number: ");
            for (int i = 0; i < sumCount.length; i++) {
                System.out.println("sumCount[" + i + "]: " + sumCount[i]);
            }
        }

        return (System.nanoTime() - start) / 1000000.0;
    }

    /**
     * Does a sequential execution of the second step of radix sort
     * @param n size of problem data
     * @return time usage in millisec
     */
    public double executeParallel(int n) {
        createAndPopulateArrays(n); // Create arrays

        long start = System.nanoTime();
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS + 1);
        int elemetsPerThread = n / NUMBER_OF_THREADS;

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int left = i * elemetsPerThread;
            int right = (i+1) * elemetsPerThread;

            if (i == NUMBER_OF_THREADS-1) {
                // Last thread takes the remainding elements
                right = n;
            }

            new Thread(new RadixSortWorkerThread(barrier, left, right, i)).start();
        }

        try {
            barrier.await(); // Each thread sort their part
            barrier.await(); // Do the "merging" of the individual results
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return (System.nanoTime() - start) / 1000000.0;
    }

    /**
     * Finds the frequency for each digit in the problem data
     * ("a"). The results will be stored in the given parameter
     * "localCount". On index 0, the frequency of digit 0 will ble
     * placed, on index 1 the frequency of digit 1 will ble placed and so
     * on.
     *
     * @param left start of range from where to calculate frequency of numbers
     * @param right end of range from where to calculate frequency of numbers
     * @param localCount local results for each thread
     * @param maskLen how many bits are this digit
     * @param shift which digit are you looking for
     */
    public void radixSort(int left, int right, int[] localCount, int maskLen, int shift) {
        int mask = (1 << maskLen) - 1;

        for (int i = left; i < right; i++) {
            localCount[(a[i] >> shift) & mask]++;
        }
    }

    /**
     * Adds up a number of columns from the temporary storage for each
     * thread to the global array `sumCount` where the final result
     * will be after all threads have called this method with their
     * columns.
     *
     * @param leftColumn where to start from
     * @param numElements how many columns are we going to add inn
     */
    public void sumResultsFromThreadsToGlobalResult(int leftColumn, int numColumns) {
        int rightColumn = leftColumn + numColumns;

        // Traverses all columns in a specific range
        for (int j = leftColumn; j < rightColumn; j++) {
            int tempStorage = 0;

            // To traverse one column, you have to check all rows
            // (each thread has previously calculated each row)
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                tempStorage += allCount[i][j];
            }

            // Store that column in the total result
            sumCount[j] = tempStorage;
        }
    }

    /**
     * Threads calling the radix method on their part of the matrix.
     */
    class RadixSortWorkerThread implements Runnable {
        final int left, right; // range in a[]
        final int threadIndex; // this thread index
        final int[] count;     // local storage of number frequency from a[]

        final int numElements; // number of elements in sumCount (second step)
        final int leftColumn;  // where to start in sumCount (second step)
        final CyclicBarrier barrier;

        RadixSortWorkerThread(CyclicBarrier barrier, int left, int right, int threadIndex) {
            this.barrier = barrier;
            this.left = left;
            this.right = right;
            this.threadIndex = threadIndex;
            this.count = new int[numSif];

            int tempNumElements = numSif / NUMBER_OF_THREADS;
            this.leftColumn = threadIndex*tempNumElements;

            if (threadIndex == NUMBER_OF_THREADS-1) {
                // Last thread takes the remaining elements
                this.numElements = tempNumElements + (numSif % NUMBER_OF_THREADS);
            } else {
                this.numElements = tempNumElements;
            }
        }

        public void run() {
            radixSort(left, right, count, numBits, 0);
            allCount[threadIndex] = count; // Store results of this thread

            try {
                barrier.await();

                // Sum up columns
                sumResultsFromThreadsToGlobalResult(leftColumn, numElements);

                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Prints the results in a nice format.
     */
    public void printResult(double[] sequentiallyResults, double[] parallelResults, int n) {
        // Sort so we can pick the median:
        Arrays.sort(sequentiallyResults);
        Arrays.sort(parallelResults);

        System.out.printf("\n%10d: Median of sequentially results: %.2f milliseconds\n",
                          n, sequentiallyResults[sequentiallyResults.length/2]);
        System.out.printf("%10d: Median of parallel results:     %.2f milliseconds\n",
                          n, parallelResults[parallelResults.length/2]);
    }

    /**
     * Fills the global array "a" with random numbers with size
     * between 0 and numSif. Uses a seed, so every time this method is
     * called, the exact same "random" numbers is generated.
     *
     * Also clears the result array, sumCount, so new results can be
     * stored.
     *
     * @param n size of problem data
     */
    private void createAndPopulateArrays(int n) {
        Random r = new Random(6734);

        a = new int[n];

        // Populate the arrays with random numbers between 0 and numSif
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(numSif);
        }

        // Clear the results, since we are going to find new ones
        sumCount = new int[numSif];
    }

    /**
     * Checks global results against the results given as
     * parameter. Method exits program if the arrays doesn't match.
     *
     * @param correctRes result to be checked against (should be correct)
     */
    public void compareAgainstGlobalResult(int[] correctRes) {
        boolean failed = false;

        for (int i = 0; i < correctRes.length; i++) {
            if (correctRes[i] != sumCount[i]) {
                System.err.printf("Index %d correct (%d) != global (%d)\n",
                                  i, correctRes[i], sumCount[i]);
                failed = true;
            }
        }

        if (failed) {
            System.exit(1);
        }
    }
}
