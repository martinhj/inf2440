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
  boolean debug = false;
  byte [] bitArr;
  // all primes in this bit-array is <= maxNum
  int  maxNum;
  long faNum;

  static int numberOfTests = 1;

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
  System.out.println("Number of primes seq: " + countAllPrimes());
  printAllPrimes();
  //for (long l: factorize(50)) System.out.print(l + " * ");
  //for (long l: factorize(1999999998)) System.out.print(l + " * ");
  System.out.print("Faktorisering sekvensielt: ");
  System.out.println(runFacSeqTest(numberOfTests));
  setAllPrime();
  splitArray();
  System.out.println("Number of primes para: " + countAllPrimes());
  printAllPrimes();
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
    this.generatePrimesByEratosthenes();
    times[i] = System.nanoTime() - starttime;
    if (debug) System.out.println(times[i]/1000000.0);
    Arrays.sort(times);
    if (debug) this.printAllPrimes();
  }
  // return dette
  //System.out.println("Time used: " + times[numberOfTests/2]/1000000.0);
  return times[numberOfTests/2]/1000000.0;
}




/**
 * Runs the para Era tests.
 */
double runEraParTest() {
  return -1;
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
void splitArray () {
  int q = Runtime.getRuntime().availableProcessors();

  // -   0   0   0   0   0   0   0
  //(0)  1   3   5   7   9  11  13(14)
  //(1) 15  17  19  21  23  25  27(28)
  //(2) 29  31  33  35  37  39  41(42)
  //(3) 43  45  47  49  51  53  55(56)
  //(4) 57  59  61  63  65  67  69(70)
  //(5) 71  73  75  77  79  81  83(84)
  //(6) 85  87  89  91  93  95  97(98)
  //(7) 99 101 103 105 107 109 111(112)
  //(8)113 115 117 119 121 123 125(126)
  //(9)127 129 131 133 135 137 139(140)
// (10)141 143 145 147 149 151 153(154)
  //fra 1 - 13 (14) i første byte (0).


double lastNumber = Math.sqrt(maxNum);
String r = "";
String nl = "\n";
r += "###########################################" + nl;
r += "#             Let's start over.           #" + nl;
r += "###########################################" + nl;
System.out.println(r);
r = "Finds primes up to including " + maxNum + nl;
r += "Now we need to find all primes up to the square root of this number." + nl;
r += "Sqrt this num: " + lastNumber + nl;
r += "###########################################" + nl + nl;
r += "But we are working with bytes, lets try to find out if we need to fix the" + nl;
r += "rest of this particualry byte." + nl;
r += "Let's print out a table of the odd numbers in each byte we got: " + nl;
System.out.println(r);
r = "";
for (int i = 0; i < bitArr.length; i++) {
  r += "(" + i +") : " ;
  for (int j = 1; j < 14; j+=2) {
    r += (int) i * 14 + j + " ";
  }
  r += "("+ (1 + i)*14 +")" ;
  System.out.println(r);
  r ="";
}
r += "That was the table. And now for something completly different." + nl;
r += "###########################################" + nl + nl;
System.out.println(r);
double restOfByte = 14 - lastNumber % 14;
double lastNumWR = lastNumber + restOfByte;
r = "Now we are going to try to find the precise breakpoint. " + nl;
r += "We got the last number from earlier: " + lastNumber + nl;
r += "And we need to find out if there was a rest of the byte: " + nl;
r += "And now we found " + restOfByte 
  + " as the rest to fill a byte (check the table)" + nl;
r += "And that leaves us with " +  lastNumWR 
  + ", which should be the last number in a byte." + nl;
r += "                        ====" + nl;
r += "Now, let's find out what byte that is the last number of: " + nl;
int breakByte = (int)lastNumber / 14;
r += "Now we got this byte as the last byte: " + breakByte + ". Is it correct?" + nl;
r += "                                       =" + nl;
r += "It seems it is." + nl;
r += "So let's conclude: " + nl;
r += (int)lastNumWR + " is the last number in byte " + (int)breakByte + nl;
r += "(" + breakByte + ") ";
for (int j = 1; j < 14; j+=2)
  r += (14 * (int) breakByte + j) + " ";
r += "(" + (1 + breakByte) * 14 + ")" + nl;
r += "                          ==" + nl;
r += "Now let's kick off the first crossing out non-primes: " + nl;
generatePrimesByEratosthenesPara(-1, 0, breakByte);
r += "###########################################" + nl + nl;
System.out.println(r);
r = "Now we're going to find the first odd number of the next byte." + nl;
r += "In how we have put this together it should be lastNumWR + 1: " + nl;
r += (int) (lastNumWR + 1) + nl;
r += "And that is in the first byte to check.";
r += "That should be our last found byte + 1: " + nl;
int firstByteA = (int) breakByte + 1;
r += firstByteA + nl;
r += "Let's print out that particular byte: " + nl;
r += "(" + firstByteA + ") ";
for (int j = 1; j < 14; j+=2) 
  r += (14 * firstByteA + j) + " ";
r += "(" + (1 + firstByteA) * 14 + ")" + nl;
r += "    ==" + nl;
r += "###########################################" + nl + nl;
System.out.println(r);
r = "Maybe we now should check if our last byte covers our last number and"
    + nl;
r += "that it contains it." + nl;
r += "Our last number is : " + maxNum + nl;
r += "And out last byte should be the length of our byte array - 1: " 
     + bitArr.length + " - 1" + nl;
int lastByte = bitArr.length - 1;
r += "Which results in: " + lastByte + nl;
r += "                  ==" + nl;
r += "(" + lastByte + ") ";
for (int j = 1; j < 14; j+=2) 
  r += (14 * lastByte + j) + " ";
r += "(" + (1 + lastByte) * 14 + ")" + nl;
r += "###########################################" + nl + nl;
System.out.println(r);
r = "And now we should have a range to split up: " + nl;
r += "First byte to check: " + firstByteA + nl;
r += "Last byte to check: " + lastByte + nl;
r += "Which makes the range: " + firstByteA + " -> " + lastByte + nl;
r += "With the basis of this range: " + nl; 
int bfirstByte = 0;
r += bfirstByte + " -> " + breakByte + nl;
r += "Now, let's break up the range: " + nl;
r += "I wonder if we need to subtract the last byte from the basis or the" + nl;
r += " or the first from our range." + nl;
r += "Two results: " + nl;
r += (lastByte - firstByteA);
int rangeSize = lastByte - breakByte;
r += " or " + rangeSize + nl;
r += "Let's go for the last one!" + nl;
for (int i = 0; i < q; i++) {
  int rangeStart = (i * rangeSize / q) + firstByteA;
  int rangeStop = ((i + 1) * rangeSize / q) + firstByteA - 1;
  if (i == q - 1) rangeStop = lastByte;
  r += "(" + i + "): " + rangeStart + " -> " + rangeStop
    + "[" + (rangeStop - rangeStart + 1) + "]" + nl; 

  generatePrimesByEratosthenesPara(i, rangeStart, rangeStop);
}

System.out.println(r);




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
  if (index == - 1) {
  crossOut(1);      // 1 is not a prime
  // sjekker ikke partall
  for (int i = 3; i <= Math.sqrt(maxNum); i+=2) {
    // Tester isPrime for å slippe å krysse ut noe som allerede er krysset ut.
    // Teste uten og sjekke om det blir like mange unike utkryssinger.
    if (checkPrime(i) && isPrime(i))
      for (int j = i; j <= maxNum / i; j+=2) crossOut(i*j);
  }
  } else {
  //crossOut(1);      // 1 is not a prime
  int firstNum = startByte * 14 + 1;
  int lastNum = endByte * 14 + 14;
  String s = "";
  String nl = "\n";
  s = "Jeg er tråd: " + index + nl;
  s += " Jeg tar meg av byter mellom " + startByte + " og " + endByte + nl;
  s += " Det vil si at jeg tar ansvaret for: " + firstNum + " => " + lastNum + nl;
  System.out.println(s);


  // denne skal ikke være nødvendig lenger. Gå gjennom primes som får produkt
  // som er mellom firstNum og lastNum.
  //
    //if (debug) System.out.print("sjekker " + i);
    //if (debug) System.out.println("   ..." + checkPrime(i));
    
  // vi starter
  s = "Let's start with the first number that has a product in our range." + nl;
  s += "(" + startByte + " - " + endByte + ") / _(" + firstNum + " - " + lastNum + ")_" + nl;
  System.out.println(s);
  int prime = nextPrime(2);
  while (prime <= Math.sqrt(maxNum)) {
  s = "p: " + prime + nl;
  int multiplier = (int)Math.ceil((double)firstNum / prime);
  if (multiplier % 2 == 0) multiplier++;
  s += "firstFactor(" + firstNum + "/" + prime + "): " + multiplier + nl;
  while(prime * multiplier <= lastNum) {
      s += "firstFactor * prime " + multiplier + " * " + prime + ": "
        + (multiplier * prime) + nl;
      crossOut(prime*multiplier);
      multiplier += 2;
  }
  prime = nextPrime(prime);
  if (prime == -1) break;
  System.out.println(s);
  }
  }
} // end generatePrimesByEratosthenes





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
  public void run() {
  }
}




} // end class EratosthenesSil