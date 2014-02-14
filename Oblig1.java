// TODO:
// Merge
// Sammenligne med arrays.sort
// Kjøre alle tallene i en run, største først => jit-kompilering for de små tallene.
// Ta tiden på merge!

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CyclicBarrier;
class Oblig1 {
    //Random randomg = new Random(12341234);
    Random randomg = new Random();
	CyclicBarrier bwait, bfinish;
    static int c = 1000000;
    static int exit = 0;
	static int q = Runtime.getRuntime().availableProcessors();
    final static int MAX_VALUE = 1000000;
    ArrayList<Long> itimes = new ArrayList<Long>();
    ArrayList<Long> atimes = new ArrayList<Long>();
    ArrayList<Long> ptimes = new ArrayList<Long>();
    int ns[], ns2[], nstemp[];
    public static void main(String [] args) {
		if (args.length == 0) {
			String m;
			m = "Run with java Oblig1 n (numbercount) m (number of threads).\n";
			m += "Running with standard values:\n";
			m += "n = 1000000, m = number of CPU cores on this machine.";
			System.out.println(m);
		}
        if (args.length > 0) c = Integer.parseInt(args[0]);
		if (args.length > 1) q = Integer.parseInt(args[1]);
        new Oblig1();
    }
    Oblig1() {
        ns = new int[c];
        ns2 = new int[c];
        nstemp = new int[c];
        generateNumbers();
        ns2 = ns.clone();
        nstemp = ns.clone();
		String result;
        long time = 0;
        long startTime;
        for (int i = 0; i < 9; i++) {
            ns = nstemp.clone();
            startTime = System.nanoTime();
            iSortWrap(ns, 0, c);
            itimes.add(time = System.nanoTime() - startTime);
        }
        Collections.sort(itimes);
		result = "Sequential mean: \n" + (itimes.get(4) / 1000000.0) + "ms";
        System.out.println(result);
        for (int i = 0; i < 9; i++) {
            ns2 = nstemp.clone();
            startTime = System.nanoTime();
            Arrays.sort(ns2);
            atimes.add(time = System.nanoTime() - startTime);
        }
        Collections.sort(atimes);
		result = "Arrays.sort() mean: \n" + (atimes.get(4) / 1000000.0) + "ms";
        System.out.println(result);
		for (int i = 0; i < 9; i++) {
			ns = nstemp.clone();
			startTime = System.nanoTime();
			iSortPar(ns);
			ptimes.add(time = System.nanoTime() - startTime);
            compare();
		}
    	Collections.sort(ptimes);
    	result = "Parallel mean: \n" + (ptimes.get(4) / 1000000.0) + "ms";
        System.out.println(result);
        System.out.print("Speedup S: ");
    	result = String.format("%2.02f", (float)itimes.get(4)/ptimes.get(4));
        System.out.println(result);
        System.out.println(exit);
        System.exit(exit);
    }
void compare() {
    boolean debug = false;
    if (debug)
    //for (int n = 49; n >= 0; n--) 
      //System.out.println(":: " + n +": " + ns[n] + " <> " + (c-1-n) + ": " + ns2[c-1-n]);
    for (int n = 49; n >= 0; n--) {
        if (ns[n] != ns2[c - 1 - n]) {
            if (debug) System.out.println("!= " + n +": " + ns[n] + " != " + (c-1-n) + ": " + ns2[c-1-n]);
            exit = 1;
        }
    }
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
    boolean debug = false;
	bwait = new CyclicBarrier(q + 1);
	bfinish = new CyclicBarrier(q + 1);
	for (int i = 0; i < q; i++) 
		new Thread(new SortWorker(i,a)).start();
    try {
        bwait.await();
        if (debug) System.out.println("Mventer2");
        bfinish.await();
        if (debug) System.out.println("Mferdig");
    } catch (Exception e) {return;}

} // end iSortPar
void merge(int[] a) {
    int t, j;
	for (int n = c/q; (n + 49) < c; n += c/q) {
        for (int i = n+49; i >= n; i--) {
            if (a[i] > a[49]) {
                t = a[i]; a[i] = a[49]; j = 48;
                while(j >= 0 && t > a[j]) {
                    a[j+1] = a[j];
                    j--;
                }
                a[j+1] = t;
            }
        }
    }
} // end merge
class SortWorker implements Runnable {
	int index;
	int [] a;
	SortWorker(int index, int [] a) {
		this.index = index;
		this.a = a;
	}
	public void run() {
        boolean debug = false;
		if (index != q - 1) iSortWrap(a, c/q*index, (c/q*(index + 1))-1);
		else iSortWrap(a, c/q*index, c-1);
        try {
            if (debug) System.out.println(Thread.currentThread().getName() + "Tventer");
            bwait.await();
            if (index == q - 1) {
                if (debug) System.out.println(Thread.currentThread().getName() + "merge");
                merge(a);
            }
            if (debug) System.out.println(Thread.currentThread().getName() + "Tventer2");
            bfinish.await();
            if (debug) System.out.println("Tferdig");
        } catch (Exception e) {return;}
	}
} // end class SortWorker
} // end class Oblig1