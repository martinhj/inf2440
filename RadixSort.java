// inf2440 v2014 ukeoppgave 5
//
//
// Oppgave 3 - utgangspunkt i numSif???
// n = lengden på array
// k = kjener (t er == kjerner her)
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
class RadixSort {
final static int MAX_VALUE = 100000;
static int cores, t, c;
int[][] allCount;
int[] sumCount, a;
Random randomg; 
CyclicBarrier bsync, bfinish;
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
    c = 10001;
    a = new int[c];
    randomg = new Random();
    generateNumbers();
} // end initialize
void generateNumbers() {
    for (int i = 0; i < c; i++) {
        a[i] = randomg.nextInt(MAX_VALUE);
    }
} //end generateNumbers
void splitA(int[] a, int i) {
    // se oppgave 2 i ukeoppgave 5
    /* for (; i <   */
    if (i == t - 1) 
        System.out.println(i + ": start: " + c/t*i + " stop: " + (c-1));
    else
        System.out.println(i + ": start: " + c/t*i + " stop: " + (c/t*(i+1)-1));

}
void parwork() {
	bwait = new CyclicBarrier(t + 1);
	bfinish = new CyclicBarrier(t + 1);
	for (int i = 0; i < t; i++) 
		new Thread(new ParWorker(i)).start();
    try {
        bsync.await();
        bfinish.await();
    } catch (Exception e) {return;}
} // end parwork
class ParWorker implements Runnable {
    int index;
    // numSif ???
    //int [] count = new int[numSif];
    ParWorker(int index) {
        this.index = index;
    }
    public void run() {
        System.out.println(index + "test");
        try {
            splitA(a, index);
            bsync.await();
            bfinish.await();
        } catch (Exception e) {return;}
        //allCount[i] = count;
    }
} // end ParWorker class
} // end RadixSort class
