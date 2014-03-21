/* *****************************************************************************\
 *          Rapport                                                            *
 * Jeg ser at den sekvensielle faktoriseringen min er langt tregere enn den i  *
 * fasit i oppgavedokumentet. Og den parallelle er langt tregere enn denne
 * igjen. Jeg er usikker på hvorfor det er slik og har ikke hatt tid til å se
 * kompleksiteten i den sekvensielle algoritmen, men om du som retter ser noe
 * hadde det vært veldig fint å få tilbakemelding på.
 *
 * Årsaken til at den parallelle er langt tregere kan være på grunn av at jeg
 * forsøkte å dele opp primtallene i mer like størrelser. Jeg tok antall
 * primtall og delte dette på antall tråder. For å få delt opp med riktige
 * primtall i et område må jeg bruke nextPrime() fram til jeg er innenfor
 * dette området. Dette bruker jeg nok en stor del av tiden på i store tall
 * hvor det blir mange hopp.
 * 
 * I denne oppgaven mangler det noe presisjon. Det vil kunne skje at programmet
 * regner ut flere tall en nødvendig. Dette er for at den forholder seg til
 * hele byter som det lagres i og at det siste tallet som er oppgitt ikke
 * trenger å være det siste i en byte. Det virker ikke som det skulle gå noe
 * raskere å sjekke for hvilke tall det ikke skulle regnes ut i den byten som
 * inneholder det siste tallet.
 *
 * Kommentar til målingene.
 * Her ser det ut til at størrelsen på målingene følger < 10 for
 * faktoriseringen og litt > 10 for erastothenes sil for de sekvensielle ved
 * ganging med 10 for N. De parallelle algoritmene følger ikke helt det samme
 * mønsteret, men dette ser jeg i sammenheng med økt kompleksialitet i å 
 * starte opp de nye trådene og regne ut start og sluttpunkt.
 *
 *
 * Målinger:
 * ========
 *
 *
 * TABELL:
 * .------------------------------------------------------------------------.
 * |     N      | erseq | erapara | espeedup| facseq | facpara | facspeedup |
 * | 2000000000 | 26192 |   8337  |   3.13  | 560151 | 1748373 |  0.32      |
 * |  200000000 |  2062 |    681  |   3.03  |  60232 |  181708 |  0.33      |
 * |   20000000 |   154 |     38  |   4.05  |   7833 |   19891 |  0.39      |
 * |    2000000 |    24 |     16  |   1.45  |    774 |    2383 |  0.33      |
 * `------------------------------------------------------------------------´
 *
 *
 * N = 2000000000, faktoriserer fra 4000000000000000000
 * (Denne er kjørt bare en gang)
 * Erastothenes sil sekvensielt: 26102.65ms
 * Erastothenes sil parallelt:    8337.80ms
 *                  speedup:      3.130
 *
 * Faktorisering sekvensielt:    560151.21ms
 * Faktorisering parallelt:     1748373.96ms
 *               speedup:         0.320
 *
 *
 *
 * SEKVENSIELT:
 * 100: 3999999999999999900 : 2 * 2 * 3 * 5 * 5 * 89 * 1447 * 1553 * 66666667
 * 99: 3999999999999999901 : 19 * 2897 * 72670457642207
 * 98: 3999999999999999902 : 2 * 49965473 * 40027640687
 * 97: 3999999999999999903 : 3 * 101 * 241 * 54777261958561
 * 96: 3999999999999999904 : 2 * 2 * 2 * 2 * 2 * 1061 * 117813383600377
 * 4: 3999999999999999996 : 
 *        2 * 2 * 3 * 3 * 3 * 3 * 7 * 11 * 13 * 19 * 37 * 52579 * 333667
 * 3: 3999999999999999997 : 421 * 9501187648456057
 * 2: 3999999999999999998 : 2 * 432809599 * 4620969601
 * 1: 3999999999999999999 : 3 * 31 * 64516129 * 666666667
 * 0: 4000000000000000000 :
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2
 *        * 2 * 2 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 *
 *        5 * 5 * 5
 *
 * PARALLELT:
 * 100: 3999999999999999900 : 2 * 2 * 3 * 5 * 5 * 89 * 1447 * 1553 * 66666667
 * 99: 3999999999999999901 : 19 * 2897 * 72670457642207
 * 98: 3999999999999999902 : 2 * 49965473 * 40027640687
 * 97: 3999999999999999903 : 3 * 101 * 241 * 54777261958561
 * 96: 3999999999999999904 : 2 * 2 * 2 * 2 * 2 * 1061 * 117813383600377
 * 4: 3999999999999999996 : 
 *        2 * 2 * 3 * 3 * 3 * 3 * 7 * 11 * 13 * 19 * 37 * 52579 * 333667
 * 3: 3999999999999999997 : 421 * 9501187648456057
 * 2: 3999999999999999998 : 2 * 432809599 * 4620969601
 * 1: 3999999999999999999 : 3 * 31 * 64516129 * 666666667
 * 0: 4000000000000000000 : 
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2
 *        * 2 * 2 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 *
 *        5 * 5 * 5 
 *
 * 
 * 
 * 
 * N = 200000000, faktoriserer fra 40000000000000000
 * Erastothenes sil sekvensielt: 2062.63ms
 * Erastothenes sil parallelt:    681.68ms
 *                  speedup:      3.026      
 *
 * Faktorisering sekvensielt:    60232.96ms
 * Faktorisering parallelt:     181708.08ms
 *                  speedup:      0.331
 *
 * SEKVENSIELT:
 * 100: 39999999999999900 : 2 * 2 * 3 * 5 * 5 * 7 * 952381 * 19999999
 * 99: 39999999999999901 : 181 * 229 * 229 * 47237 * 89213
 * 98: 39999999999999902 : 2 * 16103 * 1242004595417
 * 97: 39999999999999903 : 3 * 191 * 69808027923211
 * 96: 39999999999999904 : 2 * 2 * 2 * 2 * 2 * 47 * 7867 * 19843 * 170371
 * 4: 39999999999999996 : 2 * 2 * 3 * 3 * 11 * 17 * 73 * 101 * 137 * 5882353
 * 3: 39999999999999997 : 37 * 1081081081081081
 * 2: 39999999999999998 : 2 * 7 * 47 * 281081 * 216273151
 * 1: 39999999999999999 : 3 * 89 * 1447 * 1553 * 66666667
 * 0: 40000000000000000 :
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2
 *        * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5
 *
 * PARALLELT:
 * 100: 39999999999999900 : 2 * 2 * 3 * 5 * 5 * 7 * 952381 * 19999999
 * 99: 39999999999999901 : 181 * 229 * 229 * 47237 * 89213
 * 98: 39999999999999902 : 2 * 16103 * 1242004595417
 * 97: 39999999999999903 : 3 * 191 * 69808027923211
 * 96: 39999999999999904 : 2 * 2 * 2 * 2 * 2 * 47 * 7867 * 19843 * 170371
 * 5: 39999999999999995 : 5 * 199999 * 40000200001
 * 4: 39999999999999996 : 2 * 2 * 3 * 3 * 11 * 17 * 73 * 101 * 137 * 5882353
 * 3: 39999999999999997 : 37 * 1081081081081081
 * 2: 39999999999999998 : 2 * 7 * 47 * 281081 * 216273151
 * 1: 39999999999999999 : 3 * 89 * 1447 * 1553 * 66666667
 * 0: 40000000000000000 : 
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2
 *        * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5
 * 
 *
 *
 *     
 * N = 20000000, faktoriserer fra 400000000000000
 * Erastothenes sil sekvensielt: 154.95ms
 * Erastothenes sil parallelt:    38.26ms
 *                  speedup:      4.050      
 *
 * Faktorisering sekvensielt:    7833.09ms
 * Faktorisering parallelt:     19891.42ms
 *                  speedup:      0.394
 *
 * SEKVENSIELT:
 * 100: 399999999999900 : 2 * 2 * 3 * 5 * 5 * 17 * 71 * 1657 * 666667
 * 99: 399999999999901 : 7 * 577 * 99034414459
 * 98: 399999999999902 : 2 * 5849089 * 34193359
 * 97: 399999999999903 : 3 * 101 * 1320132013201
 * 96: 399999999999904 : 2 * 2 * 2 * 2 * 2 * 241 * 379 * 136852823
 * 4: 399999999999996 : 2 * 2 * 3 * 3 * 11 * 239 * 4649 * 909091
 * 3: 399999999999997 : 399999999999997
 * 2: 399999999999998 : 2 * 23 * 8695652173913
 * 1: 399999999999999 : 3 * 7 * 952381 * 19999999
 * 0: 400000000000000 :
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 5 * 5
 *        * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5
 *
 * PARALLELT:
 * 100: 399999999999900 : 2 * 2 * 3 * 5 * 5 * 17 * 71 * 1657 * 666667
 * 99: 399999999999901 : 7 * 577 * 99034414459
 * 98: 399999999999902 : 2 * 5849089 * 34193359
 * 97: 399999999999903 : 3 * 101 * 1320132013201
 * 96: 399999999999904 : 2 * 2 * 2 * 2 * 2 * 241 * 379 * 136852823
 * 4: 399999999999996 : 2 * 2 * 3 * 3 * 11 * 239 * 4649 * 909091
 * 3: 399999999999997 : 399999999999997
 * 2: 399999999999998 : 2 * 23 * 8695652173913
 * 1: 399999999999999 : 3 * 7 * 952381 * 19999999
 * 0: 400000000000000 :
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 5 * 5
 *        * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5
 * 
 *
 *
 *
 * N = 2000000, faktoriserer fra 4000000000000
 * Erastothenes sil sekvensielt: 23.79ms
 * Erastothenes sil parallelt:    16.37ms
 *                  speedup:      1.454      
 *
 * Faktorisering sekvensielt:    774.56ms
 * Faktorisering parallelt:     2383.58ms
 *                  speedup:      0.325
 *
 * SEKVENSIELT:
 * 100: 3999999999900 : 2 * 2 * 3 * 5 * 5 * 163 * 409 * 199999
 * 99: 3999999999901 : 491 * 2207 * 3691273
 * 98: 3999999999902 : 2 * 23 * 1697 * 1847 * 27743
 * 97: 3999999999903 : 3 * 1333333333301
 * 96: 3999999999904 : 2 * 2 * 2 * 2 * 2 * 124999999997
 * 4: 3999999999996 : 2 * 2 * 3 * 3 * 3 * 7 * 11 * 13 * 37 * 101 * 9901
 * 3: 3999999999997 : 3877 * 1031725561
 * 2: 3999999999998 : 2 * 3833 * 521784503
 * 1: 3999999999999 : 3 * 17 * 71 * 1657 * 666667
 * 0: 4000000000000 :
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 5 * 5 * 5 * 5
 *        * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5
 *
 * PARALLELT:
 * 100: 3999999999900 : 2 * 2 * 3 * 5 * 5 * 163 * 409 * 199999
 * 99: 3999999999901 : 491 * 2207 * 3691273
 * 98: 3999999999902 : 2 * 23 * 1697 * 1847 * 27743
 * 97: 3999999999903 : 3 * 1333333333301
 * 96: 3999999999904 : 2 * 2 * 2 * 2 * 2 * 124999999997
 * 4: 3999999999996 : 2 * 2 * 3 * 3 * 3 * 7 * 11 * 13 * 37 * 101 * 9901
 * 3: 3999999999997 : 3877 * 1031725561
 * 2: 3999999999998 : 2 * 3833 * 521784503
 * 1: 3999999999999 : 3 * 17 * 71 * 1657 * 666667
 * 0: 4000000000000 :
 *        2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 5 * 5 * 5 * 5
 *        * 5 * 5 * 5 * 5 * 5 * 5 * 5 * 5
 *
 *
 *
 *


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

  static int numberOfTests = 1;

  final static int q = Runtime.getRuntime().availableProcessors();

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
  String s;
  String nl = "\n";
  double eraSeq, eraPara, facSeq, facPara;
  //System.out.println("Ran with " + numberOfTests + " tests.");
  System.out.print("Erastosthenes Sil sekvensielt: ");
  System.out.println((eraSeq = runEraSeqTest(numberOfTests)) + "ms.");
  System.out.print("Number of primes seq: ");
  System.out.println(countAllPrimes());
  System.out.print("Erastosthenes Sil parallelt: ");
  System.out.println((eraPara = runEraParTest(numberOfTests)) + "ms.");
  System.out.print("Number of primes para: ");
  //printAllPrimes();
  System.out.println(countAllPrimes());
  //for (long l: factorize(50)) System.out.print(l + " * ");
  //for (long l: factorize(1999999998)) System.out.print(l + " * ");
  System.out.print("Faktorisering sekvensielt: ");
  System.out.println((facSeq = runFacSeqTest(numberOfTests)) + "ms.");
  System.out.println();
  System.out.print("Faktorisering parallelt: ");
  System.out.println((facPara = runFacParTest(numberOfTests)) + "ms.");

  s = "Tider brukt: " + nl;
  s += "Sekvensiell Erast. sil: " + eraSeq + nl;
  s += "Parallell Erast. sil: " + eraPara + nl;
  s += "Sekvensiell faktorisering: " + facSeq + nl;
  s += "Parallell faktoriserign: " + facPara + nl;
  s += "Med N på " + maxNum + nl;
  s += "og faktoriserer " + (long) maxNum * maxNum + nl;
  s += "Speedup for parallellisert Erastosthenes sil: " + nl;
  s += eraSeq / eraPara + nl;
  s += "Speedup for parallellisert faktorisering: " + nl;
  s += facSeq / facPara + nl;
  s += "Snitt tid sekvensiell faktorisering: " + nl + (facSeq / 100);
  s += "Snitt tid parallell faktorisering: " + (facPara / 100);
  System.out.println(s);
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
    setAllPrime();
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
  boolean debug = true;
  if (debug) System.out.println();
  long time, starttime;
  long [] times = new long [n];
  for (int i = 0; i < n; i++) {
    starttime = System.nanoTime();
    for (long j = (long)maxNum * maxNum - 100; j <= (long)maxNum * maxNum; j++)
    {
      if (!debug) factorize(j);
      if (debug) {
        System.out.print(((long)maxNum*maxNum) - j + ": " + j + " : ");
        for (long l: factorize(j)) System.out.print(l + " * ");
        System.out.println();
      }
    }
    times[i] = System.nanoTime() - starttime;
    Arrays.sort(times);
  }
  return times[numberOfTests/2]/1000000.0;
}




/**
 * Runs the para Fac tests.
 */
double runFacParTest(int n) {
  boolean debug = true;
  long time, starttime;
  long [] times = new long [n];
  for (int i = 0; i < n; i++) {
    starttime = System.nanoTime();
    for (long j = (long)maxNum * maxNum - 100; j <= (long)maxNum * maxNum; j++)
    {
      if (!debug) factorizeParaRun(j);      
      if (debug) {
        System.out.print(((long)maxNum*maxNum) - j + ": " + j + " : ");
        for (long l: factorizeParaRun(j)) System.out.print(l + " * ");
        System.out.println();
      }
    }
    times[i] = System.nanoTime() - starttime;
    Arrays.sort(times);
  }
  return times[numberOfTests/2]/1000000.0;
}




/**
 * Split up array to factorize.
 */
ArrayList<Long> factorizeParaRun(long num) {
	bwait = new CyclicBarrier(q + 1);
	bfinish = new CyclicBarrier(q + 1);
  int numberOfPrimes = countAllPrimes();
  int startpoint, stoppoint;
  int primeSum = 0;
  ArrayList<Long> fac = new ArrayList<Long>();
  ArrayList<ArrayList<Long>> l = new ArrayList<ArrayList<Long>>(q);
  FactorizeRunner a [] = new FactorizeRunner [q];


  for (int i = 0; i < q; i++) {
    startpoint = numberOfPrimes / q * i;
    if (i == q - 1) stoppoint = numberOfPrimes;
    else stoppoint = (numberOfPrimes / q * (i + 1) - 1);
    new Thread(a[i] = new FactorizeRunner(num, startpoint, stoppoint)).start();
  } // end for i 


  try {
    bwait.await();
    for (FactorizeRunner ai: a)
      l.add(ai.getFactors());
    bfinish.await();
  } catch (Exception e) {return null;}


  long product = 1;
  for (ArrayList<Long> list : l) {
    for (long n : list) {
      fac.add(n);
      product *= n;
    }
  } // end for l

  if (product != num) {
    fac.add (num / product);
    product *= num / product;
  }


  return fac;
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
 * =0). Krysser ut alle oddetall fra og med @args startByte til og med @args
 * endByte. Kunne vært delt opp i to metoder slik at det som blir kjørt hvis
 * index == -1 kunne vært i en og det andre i en annen. Men få tester ( ==
 * antall tråder).
 */
void generatePrimesByEratosthenesPara(int index, int startByte, int endByte) {
  int firstNum = startByte * 14 + 1;
  int lastNum = endByte * 14 + 14;

  if (index == -1) {
    crossOut(1);      // 1 is not a prime
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




/**
 * @return an arraylist with the multiples in a factorized number @param num
 * Finds multiples between start and end.
 */
ArrayList<Long> factorizePara (long num, int start, int end) {
  ArrayList <Long> fac = new ArrayList <Long>();
  int prime = 0;
  int primeCount = 0;
  int primeRange = end - start + 1;
  long facNum = num;
  if (start == 0) primeRange -= 1;
  for (int j = 0; j <= start; j++) prime = nextPrime(prime);
  while (primeCount < primeRange) {
    // Mulig denne blir overflødig i den parallelliserte utgaven.
    /*   fac.add(facNum); */
    if (prime == -1) break;
    if (facNum % prime == 0) {
      fac.add((long) prime);
      facNum /= prime;
    } else {
      prime = nextPrime(prime);
      primeCount++;
    }
  }
  return fac;
} // end factorize




/**
 * Help class to figure out which numbers are primes by Eratosthenes Sieve in
 * parallel.
 */
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




class FactorizeRunner implements Runnable {
  ArrayList<Long> fac; //= new ArrayList<Long>();
  int start, end;
  long num;
  FactorizeRunner(long num, int start, int end) {
    this.start = start;
    this.end = end;
    this.num = num;
  }
  ArrayList<Long> getFactors() {
    return fac;
  }
  public void run() {
    fac = (factorizePara(num, start, end));
    try {
      bwait.await();
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