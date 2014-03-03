///--------------------------------------------------------
//
//     File: EratosthenesSil.java for INF2440
//     implements bit-array (Boolean) for prime numbers
//     written by:  Arne Maus , Univ of Oslo,
//
//--------------------------------------------------------
import java.util.*;
/**
* Implements the bitArray of length 'maxNum' [0..bitLen ]
*   1 - true (is prime number)
*   0 - false
*  can be used up to 2 G Bits (integer range)
*  Stores info on prime/not-prime in bits 0..6 in each byte
*  (does not touch the sign-bit - bit7)
*/

public class EratosthenesSil {
  // bitArr[0] represents the 7 integers:  1,3,5,...,13, and so on
  byte [] bitArr ;           
  // all primes in this bit-array is <= maxNum
  int  maxNum;
  // kanskje trenge du disse
  final int [] bitMask = {1,2,4,8,16,32,64};
  final int [] bitMask2 ={255-1,255-2,255-4,255-8,255-16,255-32,255-64};

  public static void main (String [] args) {
    int maxNum = 50;
    String s;
    s = "Use as $ java ErastosthenesSil n, where n = max num";
    System.out.println(s);
    if (args.length > 0) maxNum = Integer.parseInt(args[0]);
    EratosthenesSil es = new EratosthenesSil(maxNum);
  }

  EratosthenesSil (int maxNum) {
    this.maxNum = maxNum;
    bitArr = new byte [(maxNum/14)+1];
    setAllPrime();
    printAllPrimes();
    // do not use 'unsetSomePrimes()' while testing Eratosthenes method.
    unsetSomePrimes();
    unsetSomePrimes();
    //generatePrimesByEratosthenes();
    printAllPrimes();

  } // end konstruktor EratostenesSil

  // only for testing. 
  void unsetSomePrimes() {
    for (int i = 0; i < maxNum; i++) if (i % 10 == 3) crossOut(i);
  } // end unsetSomePrimes

  /**
    *
    */
  void setAllPrime() {
    for (int i = 0; i < bitArr.length; i++) bitArr[i] = (byte)127;
  }

  /** Sets the number i in the prime array to zero - not a prime.
    * @param i what integer to uncheck.
    */
  void crossOut(int i) {
    //
    // set as not prime- cross out (set to 0)  bit represening 'int i'
    // ** <din kode her>
    bitArr[i/14] = (byte)(bitArr[i/14] & ~(1 << i%14/2));
    boolean debug = false;
    if (debug) System.out.println("removing " + i);
    if (debug) System.out.println("arrPlass:" + (i/14));
    if (debug) System.out.println("plass:" + ((i - (14*(i/14))) /2));
    if (debug) System.out.println("plass:" + (i%14/2) );
  } // end crossOut

  /** Returns true if number i is represented with a positive number in the 
    * prime array or it is '2'.
    * @param i what integer to check.
    */
  boolean isPrime (int i) {
    if (i == 2) return true;
    if (i%2 == 0) return false;
    if ((bitArr[i / 14] >> i%14/2 & 1) == 1) return true;
    return false; 
  } // end isPrime

  ArrayList<Long> factorize (long num) {
    ArrayList <Long> fakt = new ArrayList <Long>();
    // <Ukeoppgave i Uke 7: din kode her>

    return null;
  } // end factorize


  int nextPrime(int i) {
    // returns next prime number after number 'i'
    // <din kode her>
    return  -1;
  } // end nextTrue


  void printAllPrimes(){
    for ( int i = 2; i <= maxNum; i++)
      if (isPrime(i)) System.out.println(" "+i);
  }

  void generatePrimesByEratosthenes() {
    // krysser av alle  oddetall i 'bitArr[]' som ikke er primtall (setter de =0)
    crossOut(1);      // 1 is not a prime

    // < din Kode her, kryss ut multipla av alle primtall <= sqrt(maxNum),
    // og start avkryssingen av neste primtall p med p*p>

  } // end generatePrimesByEratosthenes


} // end class Bool