// I denne oppgaven mangler det noe presisjon. Det vil kunne skje at programmet
// regner ut flere tall en nødvendig. Dette er for at den forholder seg til hele
// byter som det lagres i og at det siste tallet som er oppgitt ikke trenger å 
// være det siste i en byte. Det virker ikke som det skulle gå noe raskere å
// sjekke for hvilke tall det ikke skulle regnes ut i den byten som inneholder 
// det siste tallet.
//
///--------------------------------------------------------
//
//     File: EratosthenesSil.java for INF2440
//     implements bit-array (Boolean) for prime numbers
//     written by:  Arne Maus , Univ of Oslo,
//
//--------------------------------------------------------
import java.util.*;
import java.util.concurrent.CyclicBarrier;

// i = (j - (p*p))/p (og + (j-(p*p))%/p




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
	CyclicBarrier bwait, bfinish;
  boolean debug = false;
  byte [] bitArr;
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
  //System.out.println("Ran with " + numberOfTests + " tests.");
  System.out.print("Erastosthenes Sil sekvensielt: ");
  System.out.println(runEraSeqTest(numberOfTests));
  System.out.print("Number of primes seq: ");
  System.out.println(countAllPrimes());
  System.out.print("Erastosthenes Sil parallelt: ");
  System.out.println(runEraParTest(numberOfTests));
  System.out.print("Number of primes para: ");
  System.out.println(countAllPrimes());
  //for (long l: factorize(50)) System.out.print(l + " * ");
  //for (long l: factorize(1999999998)) System.out.print(l + " * ");
  System.out.print("Faktorisering sekvensielt: ");
  //System.out.println(runFacSeqTest(numberOfTests));
}




/**
 * Runs the seq Era tests.
 */
double runEraSeqTest(int n) {
  long time, starttime;
  long [] times = new long [n];
  for (int i = 0; i < numberOfTests; i++)
  {
    this.setAllPrime();
    starttime = System.nanoTime();
    generatePrimesByEratosthenes();
    times[i] = System.nanoTime() - starttime;
    if (debug) System.out.println(times[i]/1000000.0);
    Arrays.sort(times);
    if (debug) this.printAllPrimes();
  }
  return times[numberOfTests/2]/1000000.0;
}




/**
 * Runs the para Era tests.
 */
double runEraParTest(int n) {
  long time, starttime;
  long [] times = new long [n];
  for (int i = 0; i < numberOfTests; i++)
  {
    this.setAllPrime();
    starttime = System.nanoTime();
    EratosthenesSieveRun();
    times[i] = System.nanoTime() - starttime;
    if (debug) System.out.println(times[i]/1000000.0);
    Arrays.sort(times);
    if (debug) this.printAllPrimes();
  }
  return times[numberOfTests/2]/1000000.0;
}




/**
 * Runs the seq Fac tests.
 */
double runFacSeqTest(int n) {
  boolean debug = false;
  long time, starttime;
  long [] times = new long [n];
  for (int i = 0; i < n; i++) {
    starttime = System.nanoTime();
    for (long j = (long)maxNum * maxNum - 0; j <= (long)maxNum * maxNum; j++)
    {
      if (!debug) factorize(j);
      if (debug) {
        System.out.print(((long)maxNum*maxNum) - j + ": " + j + " : ");
        for (long l: factorize(j)) System.out.print(l + " * ");
        System.out.println();
      }
    }
    times[i] = System.nanoTime() - starttime;
    //System.out.println(times[i]/1000000.0);
    Arrays.sort(times);
  }
  return times[numberOfTests/2]/1000000.0;
}




/**
 * Runs the para Fac tests.
 */
double runFacParTest() {
  return -1;
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
 * Splits byte array before calculation in different threads and kicks off threads.
 * i > sqrt(N). i < sqrt(N) skal bli funnet i en egen tråd før resten av trådene
 *              ^^^^--  til i <= sqrt(N) + sqrt(N)%14 for å ha utelukkende hele
 *                      bytes.
 * sparkes i gang.
 */
void EratosthenesSieveRun() {
  int q = Runtime.getRuntime().availableProcessors();
	bwait = new CyclicBarrier(q + 1);
	bfinish = new CyclicBarrier(q + 1);

  int breakByte = (int)Math.sqrt(maxNum) / 14;
  int firstByteA = (int) breakByte + 1;
  int rangeSize = bitArr.length - 1 - breakByte;
  for (int i = 0; i < q; i++) {
    int rangeStart = (i * rangeSize / q) + firstByteA;
    int rangeStop = ((i + 1) * rangeSize / q) + firstByteA - 1;
    if (i == q - 1) rangeStop = bitArr.length - 1;
    new Thread(new SieveRunner(i,rangeStart, rangeStop)).start();
  }
  try {
    bwait.await();
    bfinish.await();
  } catch (Exception e) {return;}
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
    // Tester isPrime for å slippe å krysse ut noe som allerede er krysset ut.
    // Teste uten og sjekke om det blir like mange unike utkryssinger.
    if (checkPrime(i) && isPrime(i))
      for (int j = i; j <= maxNum / i; j+=2) crossOut(i*j);
  }

} // end generatePrimesByEratosthenes



/**
 * krysser av alle  oddetall i 'bitArr[]' som ikke er primtall (setter de
 * =0).
 * kryss ut multipla av alle primtall <= sqrt(maxNum) * og start avkryssingen
 * av neste primtall p med p*p>
 */
void generatePrimesByEratosthenesPara(int index, int startByte, int endByte) {
  boolean debug = true;
  int firstNum = startByte * 14 + 1;
  int lastNum = endByte * 14 + 14;

  if (index == - 1) {
  crossOut(1);      // 1 is not a prime
  // sjekker ikke partall
  int j;
  for (int i = 3; i <= Math.sqrt(maxNum); i+=2) {
    if (checkPrime(i) && isPrime(i)) {
      j = i;
      while (i * j <= lastNum) {
        crossOut(i*j);
        j+=2;
      }
    }
  }
  } else {

  int prime = nextPrime(2);
  while (prime <= Math.sqrt(maxNum)) {
  int multiplier = (int)Math.ceil((double)firstNum / prime);
  if (multiplier % 2 == 0) multiplier++;
  while(prime * multiplier <= lastNum) {
      crossOut(prime*multiplier);
      multiplier += 2;
  }
  prime = nextPrime(prime);
  if (prime == -1) break;
  }
  }
} // end generatePrimesByEratosthenesPara





/**
 * @return an arraylist with the multiples in a factorized number @param num
 */
ArrayList<Long> factorize (long num) {
  ArrayList <Long> fac = new ArrayList <Long>();
  int n = nextPrime(0);
  long facNum = num;
  //System.out.println(num);
  //System.out.println(facNum);
  while (n < Math.sqrt(num) && facNum != 1) {
    if (n == -1) {
      fac.add(facNum);
      break;
    }
    if (facNum % n == 0) {
      fac.add((long) n);
      facNum /= n;
    } else {
      n = nextPrime(n);
    }
  }
  return fac;
} // end factorize




class SieveRunner implements Runnable {
  int index, startByte, stopByte;
  SieveRunner(int index, int startByte, int stopByte) {
    this.index = index;
    this.startByte = startByte;
    this.stopByte = stopByte;
  }
  public void run() {
    if (index == 0) {
      generatePrimesByEratosthenesPara(-1, 0, startByte - 1);
    }
    try {
      bwait.await();
      generatePrimesByEratosthenesPara(index, startByte, stopByte);
      bfinish.await();
    } catch (Exception e) {return;}
  }
}




} // end class EratosthenesSil





// For å skrive ut oddetall slik de ligger i bitArr:
/*r = "";
for (int i = 0; i < bitArr.length; i++) {
  r += "(" + i +") : " ;
  for (int j = 1; j < 14; j+=2) {
    r += (int) i * 14 + j + " ";
  }
  r += "("+ (1 + i)*14 +")" ;
  System.out.println(r);
  r ="";
}
*/