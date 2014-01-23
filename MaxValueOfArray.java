import java.util.Arrays;
import java.util.Random;
/**
 * Find the maximum value of a array. Parallelized.
 */
class MaxValueOfArray {
int n = 16; // number of array elements
int numberContainer[];
Random ranGen = new Random();
public int quantityCores = Runtime.getRuntime().availableProcessors();
public static void main(String [] args) {
    new MaxValueOfArray();

MaxValueOfArray() {
    numberContainer = new int[n];
    System.out.println("Array length: " + numberContainer.length);
    generateNumbers();
    printNumbers();
    FindMaxSequential ms = new FindMaxSequential(numberContainer);
    FindMaxParallel mp = new FindMaxParallel(numberContainer, quantityCores);
    System.out.println("Seq spent: " + ms.time);
    System.out.println("Par spent: " + mp.time);
}
void generateNumbers() {
    for (int i = 0; i < n; i++) {
        numberContainer[i] = ranGen.nextInt(n);
    }
}
void printNumbers() {
    for (int i: numberContainer)
        System.out.print(i+",");
    System.out.println();
}

int findLargestSeq() {
    int largest = 0;
    for (int i = 0; i < numberContainer.length; i++)
        if (numberContainer[i] > largest) largest = numberContainer[i];
    return largest;
}

} //End MaxValueOfArray

class FindMaxSequential {
    int [] numberContainer;
    int largest = 0;
    long time;
    FindMaxSequential(int [] nc) {
        numberContainer = nc;
        long startTime = System.nanoTime();
        findLargest();
        time = System.nanoTime() - startTime;
        System.out.println("Max sequential: " + largest);
    }
    void findLargest() {
        for (int i = 0; i < numberContainer.length; i++)
            if (numberContainer[i] > largest) largest = numberContainer[i];
    }
} //end class FindMaxSequential

/**
 *
 */
class FindMaxParallel {
boolean debug = false;
/* boolean debug = true; */
int [] numberContainer;
int numberOfThreads;
int globalLargest = 0;
long time;
/**
 *
 */
FindMaxParallel(int[] nc, int nt) {
    numberContainer = nc;
    numberOfThreads = nt;
    findLargest();
    time = 0;
    System.out.println("Max parallel: " + globalLargest);
}

void findLargest() {
    //boolean remainder = numberContainer.length % numberOfThreads > 0;
    // brukte denne for å ta med remainder, men denne tok ikke høyde for
    // alle tilfeller
    //int l = numberContainer.length/numberOfThreads + ((remainder) ? 1 : 0);
    if (debug) System.out.println(l);
    if (debug) System.out.println("###");
    // does not kick off last thread to check how many int's in the last segment
    int rest = numbercontainer.length % numberofthreads;
    int l = numbercontainer.length/numberofthreads;
    thread runners[] = new thread[numberofthreads];
    for (int i = 0; i < numberofthreads-1; i++) {
        if (debug) {
        system.out.println("kicking off thread " + i + ".");
        system.out.println("with array: ");
        system.out.println(i*l + " - " + (i*l+l));
        system.out.println("###: " + i);
        }
        runners[i] = new thread(new maxparalellrunner(arrays.copyofrange(numbercontainer, i*l,i*l+l)));
        if (debug) {
        for (int j = i*l; j < i*l+l; j++) {
            system.out.print("[" + j + "]" + ": " + numbercontainer[j] + " ");
        }
        system.out.println();
        for (int k:arrays.copyofrange(numbercontainer, i*l,i*l+l))
            system.out.print("[n]: " + "[" + k + "]");
        system.out.println();
        }
    }
    runners[numberofthreads-1] = new thread(new maxparalellrunner(arrays.copyofrange(numbercontainer, (numberofthreads-1)*l,(numberofthreads-1)*l+l+rest)));
    for (int i = 0; i < runners.length; i++)
        runners[i].start();
}
}

class MaxParalellRunner implements Runnable {
boolean debug = false;
//boolean debug = true;
int numberContainer[];
int largest = 0;
MaxParalellRunner(int [] nc) {
    numberContainer = nc;
}
public void run() {
    findLargest();
}
void findLargest() {
    if (debug) for (int i = 0; i < numberContainer.length; i++)
        System.out.println(Thread.currentThread().getName() + " " + i + ": " + "[" + numberContainer[i] + "] ");
    for (int i = 0; i < numberContainer.length; i++)
        if (numberContainer[i] > largest) largest = numberContainer[i];
    if (debug) System.out.println(Thread.currentThread().getName() + "Largest: " + "X: " + largest);
}
}