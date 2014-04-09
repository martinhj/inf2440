/*
 * Oblig3 i INF2440 Universitet i Oslo
 *
 * Radix sort parallelisert.
 *
 *
 */




/* ************************************
 * TODO:
 *
 * Trengs det noe utskrift?
 * Ta tiden på de forskjellige punktene i radixsort?
 *
 * Parallellisere.
 * a) findMax
 * b)
 * c)
 * d)
 */




/*
 * import java.util.FileWriter
 */
import java.util.Random;
import java.util.Arrays;




class Oblig3 {




  /**
   * Global variables.
   */
  boolean debug = false;
  final static int MAX_VALUE = 1000000;
	final static int q = Runtime.getRuntime().availableProcessors();
  static int testCount = 1;
  static int numberCount = 500;
  Random randomg = new Random();
  // boolean filewrite = false;





  /**
   * Constructor.
   */
  Oblig3() {
  } // end constructor




  /**
   * main method. Kicking it all off.
   */
  public static void main (String [] args) {
    if (args.length > 0) numberCount = Integer.parseInt(args[0]);
    if (args.length > 1) testCount = Integer.parseInt(args[1]);
    new Oblig3().runTest(testCount, numberCount);
  }




  /**
   * Run timing tests.
   */
  void runTest(int testCount, int numberCount) {
    double seq, par;
    int n [] = populate(numberCount);
    String s, nl = "\n";
    s = "***Test***" + nl;
    s += "Testing with an array with _" + numberCount + "_ random numbers ";
    s += "and doing it _" + testCount + "_ times " + nl;
    pln(s);
    seq = runSeqTest(n, testCount);
    s = "Seq: " + seq;
    pln(s);
    par = runParTest(n, testCount);
    s = "Par: " + par;
    pln(s);
  }




  /**
   * Run radix sort sequential.
   */
  double runSeqTest(int [] n, int testCount) {
    long [] t = new long [testCount];
    long startTime;
    for (int i = 0; i < t.length; i++) {
      startTime = System.nanoTime();
      radix2(n);
      t[i] = System.nanoTime() - startTime;
    }
    if (debug) {
      for (int i = 0; i < 10; i++)
        pln("[" + i + "]: " + n[i]);
      for (int i = n.length - 1; i >= n.length - 10; i--)
        pln("[" + i + "]: " + n[i]);
    }
    Arrays.sort(t);
    return t[testCount/2]/1000000.0;
  }




  /**
   * Run radix sort in parallel.
   */
  double runParTest(int [] n, int testCount) {
    //a
    /* FindMax */
    //b
    //c
    //d
    long [] t = new long [testCount];
    long startTime;
    for (int i = 0; i < t.length; i++) {
      startTime = System.nanoTime();
      radix2Par(n);
      t[i] = System.nanoTime() - startTime;
    }
    if (debug) {
      for (int i = 0; i < 10; i++)
        pln("[" + i + "]: " + n[i]);
      for (int i = n.length - 1; i >= n.length - 10; i--)
        pln("[" + i + "]: " + n[i]);
    }
    Arrays.sort(t);
    return t[testCount/2]/1000000.0;
  }




  /**
   * Print to screen and/or file.
   */
  static void pln(String s) {
    p(s + "\n");
  }




  /**
   * Print to screen and/or file.
   */
  static void pln() {
    p("\n");
  }




  /**
   * Print to screen and/or file.
   */
  static void p(String s) {
    System.out.print(s);
    //if (filewrite) file.
  }




  /**
   * Populate an array with random numbers.
   */
  int[] populate(int l) {
    int [] ns = new int [l];
    for (int i = ns.length - 1; i >= 0; i--) {
      ns[i] = randomg.nextInt(MAX_VALUE);
    }
    return ns;
  }





  static int findMaxPar(int [] n) {
    int l;
    l = n.length;
    for (int i = 0; i < q; i++) {
      if (i != q - 1 ) findMax(n, l/q*i, l/q*(i+1)-1);
      else findMax(n, l/q*i, l-1);
    }
    return -1;
  }

  /**
   * Finds the largest value in the range between {@code startpoint}
   * and {@code endpoint} in an int array {@code n}.
   * @param n
   * @param startpoint
   * @param endpoint
   */
  static int findMax(int [] n, int startpoint, int endpoint) {
    pln("In array n we got: " + startpoint + " as a startpoint and " 
        + endpoint + " as a endpoint.");
    return -1;
  }




  /** 
   * Parallel version of radix sort with two digits.
   */
  static void radix2Par(int [] a) {
    findMaxPar(a);



    /*
    // 2 digit radixSort: a[]
    int max = a[0], numBit = 2;

    // a) finn max verdi i a[]
    for (int i = 1 ; i < a.length ; i++)
      if (a[i] > max) max = a[i];

    while (max >= (1<<numBit)) numBit++; // antall siffer i max

    // bestem antall bit i siffer1 og siffer2 
    int bit1 = numBit/2,
        bit2 = numBit-bit1;
    int[] b = new int [a.length];
    radixSort(a, b, bit1, 0); // første siffer fra a[] til b[]
    radixSort(b, a, bit2, bit1);// andre siffer, tilbake fra b[] til a[]

  } // end radix2
    */
  }




  /** 
   * Radix sort with two digits.
   */
  static void radix2(int [] a) {
    // debug
    long starttime = 0,
         stoptime = 0;
    boolean debug = true;
    // 2 digit radixSort: a[]
    int max = a[0], numBit = 2;

    // a) finn max verdi i a[]
    if (debug) starttime = System.nanoTime();
    for (int i = 1 ; i < a.length ; i++)
      if (a[i] > max) max = a[i];
    if (debug) stoptime = System.nanoTime();
    if (debug) p("a1: " + ((stoptime - starttime)/1000000.0)+ "ms\n");

    if (debug) starttime = System.nanoTime();
    if (debug) System.out.println("nb: " + numBit);
    while (max >= (1<<numBit)) numBit++; // antall siffer i max
    if (debug) System.out.println("nb: " + numBit);
    if (debug) stoptime = System.nanoTime();
    if (debug) p("a2: " + ((stoptime - starttime)/1000000.0)+ "ms\n");

    // bestem antall bit i siffer1 og siffer2 
    int bit1 = numBit/2,
        bit2 = numBit-bit1;
    int[] b = new int [a.length];
    radixSort(a, b, bit1, 0); // første siffer fra a[] til b[]
    radixSort(b, a, bit2, bit1);// andre siffer, tilbake fra b[] til a[]

  } // end radix2




  /** 
   * Sort a[] on one digit ; number of bits = maskLen, shiftet up ‘shift’ bits.
   */ 
  static void radixSort ( int [] a, int [] b, int maskLen, int shift){
    boolean debug = true;
    int acumVal = 0, j, n = a.length;
    int mask = (1<<maskLen) -1;
    int [] count = new int [mask+1];
    // debug
    long starttime = 0,
         stoptime = 0;

    // b) count=the frequency of each radix value in a 
    if (debug) starttime = System.nanoTime();
    for (int i = 0; i < n; i++) {
      count[(a[i]>> shift) & mask]++; }
    if (debug) stoptime = System.nanoTime();
    if (debug) p("b: " + ((stoptime - starttime)/1000000.0)+ "ms\n");

    // c) Add up in 'count' - accumulated values 
    if (debug) starttime = System.nanoTime();
    for (int i = 0; i <= mask; i++) {
      j = count[i];
      count[i] = acumVal; acumVal += j;
    }
    if (debug) stoptime = System.nanoTime();
    if (debug) p("c: " + ((stoptime - starttime)/1000000.0) + "ms\n");

    // d) move numbers in sorted order a to b 
    if (debug) starttime = System.nanoTime();
    for (int i = 0; i < n; i++) {
      b[count[(a[i]>>shift) & mask]++] = a[i]; }
    if (debug) stoptime = System.nanoTime();
    if (debug) p("d: " + ((stoptime - starttime)/1000000.0) + "ms\n");
  }// end radixSort









  /**
   * This class is a help class to start code in the different threads.
   * It will reuse the threads for each step in the process.
   */
  class RadixRunner {
    RadixRunner() {}
  } // End Class RadixRunner



} // End Oblig3 class