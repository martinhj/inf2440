// inf2440 v2014 ukeoppgave 5
//
//
// Oppgave 3 - utgangspunkt i numSif???
// n = lengden på array
// k = kjener (t er == kjerner her)
// numSif -> Antall mulige sifferverdier på det sifferet vi sorterer på:
//  hvis 10-talls: 2**10
//  Først shift - 0, da flytter man ikke på noe og sjekker siste siffer,
//  shift = 1 sjekker vi andre siffer osv.
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
class RadixSort {
final static int MAX_VALUE = 500;
static int cores, t;
static int c = 30;
static int numSif = (int) Math.pow(2.0, 10.0);
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
    a = new int[c];
    randomg = new Random();
    generateNumbers();
    seqwork();
} // end initialize
void generateNumbers() {
    for (int i = 0; i < c; i++) {
        a[i] = randomg.nextInt(MAX_VALUE);
    }
} //end generateNumbers
void splitA(int[] a, int i) {
    // se oppgave 2 i ukeoppgave 5
    /* for (; i <   */
    boolean debug = false;
    if (debug) {
    if (i == t - 1) 
        System.out.println(i + ": start: " + c/t*i + " stop: " + (c-1));
    else
        System.out.println(i + ": start: " + c/t*i + " stop: " + (c/t*(i+1)-1));
    }

}
void seqwork() {
    int shift = 2;
    int mask = (1<<5)-1;
    for (int i = 0; i < c; i++) {
        System.out.print(Integer.toBinaryString(a[i]) + " : ");
        System.out.print(Integer.toBinaryString(mask) + " : ");
        System.out.print(Integer.toBinaryString(((a[i]>> shift))) + " : ");
        System.out.print(Integer.toBinaryString(((a[i]>> shift) & mask)) + " : ");
        System.out.println(a[i] + " : " + ((a[i]>> shift) & mask));
    }
}
void parwork() {
	bsync = new CyclicBarrier(t + 1);
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
    int [] count = new int[numSif];
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
