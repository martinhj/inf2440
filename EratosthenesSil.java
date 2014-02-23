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
	byte [] bitArr ;           // bitArr[0] represents the 7 integers:  1,3,5,...,13, and so on
	int  maxNum;               // all primes in this bit-array is <= maxNum
	final int [] bitMask = {1,2,4,8,16,32,64};  // kanskje trenger du denne
	final int [] bitMask2 ={255-1,255-2,255-4,255-8,255-16,255-32,255-64}; // kanskje trenger du denne


    EratosthenesSil (int maxNum) {
        this.maxNum = maxNum;
		bitArr = new byte [(maxNum/14)+1];
		setAllPrime();
        generatePrimesByEratosthenes();

      } // end konstruktor ErathostenesSil

	  void setAllPrime() {
		  for (int i = 0; i < bitArr.length; i++) {
		   bitArr[i] = (byte)127;
	      }
	   }

      void crossOut(int i) {
       // set as not prime- cross out (set to 0)  bit represening 'int i'
             // ** <din kode her>
	   } //

      boolean isPrime (int i) {
          // <din kode her, husk å teste særskilt for 2 (primtall) og andre partall (ikke)>

	 }

	  ArrayList<Long> factorize (long num) {
		  ArrayList <Long> fakt = new ArrayList <Long>();
          // <Ukeoppgave i Uke 7: din kode her>

	  } // end factorize


      int nextPrime(int i) {
	   // returns next prime number after number 'i'
          // <din kode her>
          return  ..;
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
