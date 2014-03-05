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
  boolean debug = false;
  byte [] bitArr;
  long removes = 0;
  // all primes in this bit-array is <= maxNum
  int  maxNum;
  long faNum;

  static int numberOfTests = 9;

  // kanskje trenge du disse
  final int [] bitMask = {1,2,4,8,16,32,64};
  final int [] bitMask2 = {255-1,255-2,255-4,255-8,255-16,255-32,255-64};




/** 
 * Kicking it all off.
 */
public static void main (String [] args) {
  int maxNum = 50;
  String s;
  s = "Use as $ java ErastosthenesSil n, where n = max num";
  if (args.length == 0) System.out.println(s);
  if (args.length > 0) maxNum = Integer.parseInt(args[0]);
  EratosthenesSil es = new EratosthenesSil(maxNum);
  es.runTest(numberOfTests);
} // end main




/**
 * Constructor.
 */
EratosthenesSil (int maxNum) {
  this.maxNum = maxNum;
  this.faNum = (long) maxNum * maxNum;
  bitArr = new byte [(maxNum/14)+1];
  setAllPrime();
} // end konstruktor EratostenesSil




/**
 * Runs the tests.
 */
void runTest(int numberOfTests) {
  long time, starttime;
  double [] seqEra = new double[numberOfTests];
  double [] paraEra = new double[numberOfTests];
  double [] seqFac = new double[numberOfTests];
  double [] paraFac = new double[numberOfTests];
  for (int i = 0; i < numberOfTests; i++) {
    this.setAllPrime();
    starttime = System.nanoTime();
    this.generatePrimesByEratosthenes();
    seqEra[i] = System.nanoTime() - starttime;
  Arrays.sort(seqEra);
  if (debug) this.printAllPrimes();
  }
  System.out.println("Time used: " + seqEra[numberOfTests/2]/1000000.0);
  for (int i = 0; i < numberOfTests; i++) {
    for (long j = (long)maxNum * maxNum - 99; j <= (long)maxNum * maxNum; j++) {
      factorize(j);
      if (debug) {
        System.out.println(j);
        for (long l: factorize(j)) System.out.print(l + " * ");
        System.out.println();
      }
    }
  }
}



/**
 * Sets all numbers as primes making them ready to be unchecked again.
 */
void setAllPrime() {
  for (int i = 0; i < bitArr.length; i++) bitArr[i] = (byte)127;
}




/** Sets the number i in the prime array to zero - not a prime.
 * set as not prime- cross out (set to 0)  bit represening 'int i'
 * @param i what integer to uncheck.
 */
void crossOut(int i) {
  removes++;
  bitArr[i/14] = (byte)(bitArr[i/14] & ~(1 << i%14/2));
  if (debug) System.out.println("removing " + i);
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




/**
 * Checks if a number is a prime.
 */
boolean checkPrime (int n) {
  for (int i = 2; i < Math.sqrt(n); i++)
    if (n % i == 0)
      return false;
  return true;
}




/**
 * Finds the next prime.
 * @return next prime after number @param i
 */
int nextPrime(int i) {
  for (int j = i+1; j <= maxNum; j++) if (isPrime(j)) return j;
  return  -1;
} // end nextTrue




/**
 * Counts how many primes found with Eratosthenes method.
 */
int countAllPrimes() {
  int c = 0;
  for (int i = 2; i <= maxNum; i++)
    if (isPrime(i)) c++;
  return c;
}




/**
 * Prints out all numbers that is presented as primes in the bitArr array.
 */
void printAllPrimes(){
  for ( int i = 2; i <= maxNum; i++)
    if (isPrime(i)) System.out.println(" "+i);
}




/**
 * krysser av alle  oddetall i 'bitArr[]' som ikke er primtall (setter de
 * =0).
 * kryss ut multipla av alle primtall <= sqrt(maxNum) * og start avkryssingen
 * av neste primtall p med p*p>
 */
void generatePrimesByEratosthenes() {
  crossOut(1);      // 1 is not a prime
  // sjekker ikke partall
  for (int i = 3; i <= Math.sqrt(maxNum); i+=2) {
    if (debug) System.out.print("sjekker " + i);
    if (debug) System.out.println("   ..." + checkPrime(i));
    if (checkPrime(i) && isPrime(i))
      for (int j = i; j <= maxNum / i; j+=2) {
        if (debug) System.out.println("::: " + i + "*" +j + " = " + i*j);
        crossOut(i*j);
      }
  }

} // end generatePrimesByEratosthenes




/**
 * @return an arraylist with the multiples in a factorized number @param num
 */
ArrayList<Long> factorize (long num) {
  ArrayList <Long> fakt = new ArrayList <Long>();
  // <Ukeoppgave i Uke 7: din kode her>
  int cp;
  int n = nextPrime(0);
  long facNum = num;
  while (n < Math.sqrt(num) && facNum != 1) {
    if (n == -1) {
      fakt.add(facNum);
      if (debug) System.out.println("N == " + n + "facNum == " + facNum +"\n breaking...");
      break;
    }
    if (debug) System.out.println("N : " + n);
    if (debug) System.out.println("facNum : " + facNum);
    if (facNum % n == 0) {
      fakt.add((long) n);
      facNum = facNum / n;
      if (debug) System.out.println("adding " + n);
      if (debug) System.out.println("num = " + facNum);
    } else {
      if (debug) System.out.println("prime is now " + nextPrime(n));
      n = nextPrime(n);
    }
  }
  if (debug) System.out.println(n);
  if (debug) System.out.println(":"+facNum);
  return fakt;
} // end factorize




} // end class EratosthenesSil