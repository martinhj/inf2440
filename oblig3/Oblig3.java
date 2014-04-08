/*
 * Oblig3 i INF2440 Universitet i Oslo
 *
 * Radix sort parallelisert.
 *
 *
 */




/* ************************************
 * TODO::
 *
 * Trengs det noe utskrift?
 */




/*
 * import java.util.FileWriter
 */
import java.util.Random;



class Oblig3 {
  Random randomg = new Random();
  final static int MAX_VALUE = 1000000;




  /**
   * Global variables.
   */
  // boolean debug = false;
  // boolean filewrite = false;





  /**
   * Constructor.
   */
  Oblig3() {
    pln("***Test***");
    int n [] = populate(10);
    for (int i = 0; i < n.length; i++)
      pln("" + n[i]);
    radix2(n);
    pln("*****************");
    for (int nt : n)
      pln("" + nt);
  } // end constructor




  /**
   * main method. Kicking it all off.
   */
  public static void main (String [] args) {
    new Oblig3();
  }




  /**
   * Print to screen and/or file.
   */
  void pln(String s) {
    System.out.println(s);
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




  /** 
   * Radix sort with two digits.
   */
  static void radix2(int [] a) {
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




  /** 
   * Sort a[] on one digit ; number of bits = maskLen, shiftet up ‘shift’ bits.
   */ 
  static void radixSort ( int [] a, int [] b, int maskLen, int shift){
    int acumVal = 0, j, n = a.length;
    int mask = (1<<maskLen) -1;
    int [] count = new int [mask+1];

    // b) count=the frequency of each radix value in a 
    for (int i = 0; i < n; i++) {
      count[(a[i]>> shift) & mask]++; }

    // c) Add up in 'count' - accumulated values 
    for (int i = 0; i <= mask; i++) {
      j = count[i];
      count[i] = acumVal; acumVal += j;
    }

    // d) move numbers in sorted order a to b 
    for (int i = 0; i < n; i++) {
      b[count[(a[i]>>shift) & mask]++] = a[i]; }
  }// end radixSort




} // End Oblig3 class