// TODO:
// Merge
// Sammenligne med arrays.sort

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CyclicBarrier;
class Oblig1 {
    Random randomg = new Random();
	CyclicBarrier b;
    static int c = 100000000;
	static int q = Runtime.getRuntime().availableProcessors();
    final static int MAX_VALUE = 1000;
    ArrayList<Long> itimes = new ArrayList<Long>();
    ArrayList<Long> atimes = new ArrayList<Long>();
    ArrayList<Long> ptimes = new ArrayList<Long>();
    int ns[], ns2[], nstemp[];
    public static void main(String [] args) {
        if (args.length > 0) c = Integer.parseInt(args[0]);
        new Oblig1();
    }
    Oblig1() {
        ns = new int[c];
        ns2 = new int[c];
        nstemp = new int[c];
        generateNumbers();
        ns2 = ns.clone();
        nstemp = ns.clone();
        long time = 0;
        long startTime;
        for (int i = 0; i < 9; i++) {
            ns = nstemp.clone();
            startTime = System.nanoTime();
            iSortWrap(ns, 0, c);
            itimes.add(time = System.nanoTime() - startTime);
        }
        Collections.sort(itimes);
        System.out.println("mean: " + (itimes.get(4) / 1000000.0) + "ms");
        for (int i = 0; i < 9; i++) {
            ns2 = nstemp.clone();
            startTime = System.nanoTime();
            Arrays.sort(ns2);
            atimes.add(time = System.nanoTime() - startTime);
        }
        Collections.sort(atimes);
        System.out.println("mean: " + (atimes.get(4) / 1000000.0) + "ms");
        for (int n = 49; n >= 0; n--) {
            if (ns[n] != ns2[c - 1 - n]) System.out.println(false);
        }
		for (int i = 0; i < 9; i++) {
			ns = nstemp.clone();
			startTime = System.nanoTime();
			iSortPar(ns);
			ptimes.add(time = System.nanoTime() - startTime);
		}
		Collections.sort(ptimes);
        System.out.println("mean: " + (ptimes.get(4) / 1000000.0) + "ms");
        System.out.print("Speedup S: ");
        System.out.println(
                String.format("%2.02f", (float)itimes.get(4)/ptimes.get(4)));
    }
void generateNumbers() {
    for (int i = 0; i < c; i++) {
        ns[i] = randomg.nextInt(MAX_VALUE);
    }
} //end generateNumbers
/**
 * Sorterer de 50 første verdiene i arrayen a.
 */
void iSortWrap(int[] a, int l, int r) {
    iSortSeq(a, l, r);
    iSortRest(a, l, r);
}
void iSortSeq(int[] a, int l, int r) {
    int j;
    int t;
    for (int i = l + 49; i >= l; i--) {
        j = i;
        t = a[i];
        while(j < l + 49 && t < a[j + 1]) {
            a[j] = a[j + 1];
            j++;
        } // end j
        a[j] = t;
    } // end i
} // end iSortSeq
void iSortRest(int[] a, int l, int r) {
    int t, j;
    for (int i = l + 50; i < r; i++) {
        if (a[i] > a[l + 49]) {
            t = a[i]; a[i] = a[l + 49]; j = l + 48;
            while(j >= l && t > a[j]) {
                a[j+1] = a[j];
                j--;
            }
        a[j+1] = t;
        }
    }
} // end iSortRest
void iSortPar(int[] a) {
	b = new CyclicBarrier(q + 1);
	for (int i = 0; i < q; i++) 
		new Thread(new SortWorker(i)).start();
    try {
        b.await();
    } catch (Exception e) {return;}

} // end iSortPar

class SortWorker implements Runnable {
	int index;
	SortWorker(int index) {
		this.index = index;
	}
	public void run() {
		iSortWrap(ns, c/q*index, (c/q*(index + 1))-1);
        try {
            b.await();
        } catch (Exception e) {return;}
	}

} // end class SortWorker
} // end class Oblig1