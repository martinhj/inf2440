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
	CyclicBarrier bwait, bfinish;
    static int c = 1000000;
	static int q = Runtime.getRuntime().availableProcessors();
    final static int MAX_VALUE = 1000;
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
		for (int i = 0; i < 1; i++) {
			ns = nstemp.clone();
			startTime = System.nanoTime();
			iSortPar(ns);
			ptimes.add(time = System.nanoTime() - startTime);
            compare();
		}
////	Collections.sort(ptimes);
////	result = "Parallel mean: \n" + (ptimes.get(4) / 1000000.0) + "ms";
////    System.out.println(result);
////    System.out.print("Speedup S: ");
////	result = String.format("%2.02f", (float)itimes.get(4)/ptimes.get(4));
////    System.out.println(result);
    }
void compare() {
    for (int n = 49; n >= 0; n--) 
        if (ns[n] != ns2[c - 1 - n])
            System.out.println(":: " + n +": " + ns[n] + " != " + (c-1-n) + ": " + ns2[c-1-n]);
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
	bwait = new CyclicBarrier(q + 1);
	bfinish = new CyclicBarrier(q + 1);
	for (int i = 0; i < q; i++) 
		new Thread(new SortWorker(i,a)).start();
    try {
        bwait.await();
    } catch (Exception e) {return;}
    try {
        bfinish.await();
    } catch (Exception e) {return;}

} // end iSortPar
void merge(int[] a) {
    boolean debug = false;
    int t, j;
    for (int n = 0; n < c / 2; n++) {
        String m;
        m = n + ": " + a[n] + " : " + (n+50) + ": " + a[n+50];
        if (debug) System.out.println(m);
    }
	for (int n = c/q; n < c; n += c/q) {
        if (debug) System.out.println(n);
        for (int i = n+49; i >= n; i--) {
            if (debug) System.out.println(i + " | " + (i-n));
            if (debug) System.out.println(a[i] + " : " + a[i-n]);
            if (a[i] > a[49]) {
                if (debug) System.out.println("_" + a[i] + "_" + " [] " + a[i-n]);
                t = a[i]; a[i] = a[49]; j = 48;
                while(j >= 0 && t > a[j]) {
                    a[j+1] = a[j];
                    j--;
                }
                a[j+1] = t;
                if (debug) System.out.println(t);
            }
        }
    }
} // end iSortMerge
void iSortMerge(int[] a) {
    int [] b = new int[50];
    int i, j, k;
	for (int n = c/q; n < c; n += c/q) {
        i = 0;
        j = n;
        k = 0;
        while (i < 50 && j < n+50 && k < 50) {
            if (a[i] < a[j])
            {
                b[k] = a[j];
                j++;
            }
            else
            {
                b[k] = a[i];
                i++;
            }
            k++;
        }
        while (i < 50 && k < 50) {
            b[k] = a[i];
            i++;
            k++;
        }
        while (j < n+50 && k < 50) {
            b[k] = a[j];
            j++;
            k++;
        }
    }
    for (int n = 0; n < 50; n++)
        a[n] = b[n];
    /* System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>"); */
    /* for (int n = 0; n < a.length; n++) */
    /*     System.out.println(n + ": " + a[n]); */
} // end merge

class SortWorker implements Runnable {
	int index;
	int [] a;
	SortWorker(int index, int [] a) {
		this.index = index;
		this.a = a;
	}
	public void run() {
		if (index != q - 1) iSortWrap(ns, c/q*index, (c/q*(index + 1))-1);
		else iSortWrap(ns, c/q*index, c-q);
        try {
            bwait.await();
        } catch (Exception e) {return;}
		if (index == q - 1) merge(a);
        try {
            bfinish.await();
        } catch (Exception e) {return;}
        
	}
} // end class SortWorker
} // end class Oblig1