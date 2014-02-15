// inf2440 v2014 ukeoppgave 5
// n = 
// k = 
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
class RadixSort {
final static int MAX_VALUE = 100000;
static int cores, t, c;
int[][] allCount;
int[] sumCount, a;
Random randomg; 
CyclicBarrier bwait, bfinish;
public static void main(String[] args) {
    new RadixSort();
}
RadixSort() {
    initialize();
    parwork();
}
void initialize() {
    cores = Runtime.getRuntime().availableProcessors();
    t = cores;
    allCount = new int[t][];
    sumCount = new int[t];
    c = 10000;
    a = new int[c];
    randomg = new Random();
    generateNumbers();
} // end initialize
void generateNumbers() {
    for (int i = 0; i < c; i++) {
        a[i] = randomg.nextInt(MAX_VALUE);
    }
} //end generateNumbers
void splitA(int[] a, int i) {
    // se oppgave 2 i ukeoppgave 5
   /* for (; i <   */
}
void parwork() {
	bwait = new CyclicBarrier(t + 1);
	bfinish = new CyclicBarrier(t + 1);
	for (int i = 0; i < t; i++) 
		new Thread(new ParWorker(i)).start();
    try {
        bwait.await();
        bfinish.await();
    } catch (Exception e) {return;}
} // end parwork
class ParWorker implements Runnable {
    int index;
    ParWorker(int index) {
        this.index = index;
    }
    public void run() {
        System.out.println(index + "test");
        try {
            bwait.await();
            bfinish.await();
        } catch (Exception e) {return;}
    }
} // end ParWorker class
} // end RadixSort class
