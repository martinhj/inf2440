// Kunne ha gjort:
// Effektivisert ved å bruke opp igjen trådene. Men hadde resultert i mer
// rotete kode.  Ved å sette to trådklasser for å løse oppgaven får vi også
// målt forskjellen mellom a og b,c og d hver for seg selv om vi er klar over
// at det er et større forbedringspotensiale.


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
 * b) count values.
 * c)
 * d)
 */




/*
 * import java.util.FileWriter
 */
import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;



class Oblig3 {




  /**
   * Global variables.
   */
  boolean debug = false;
  private final int MAX_VALUE;
	final static int q = Runtime.getRuntime().availableProcessors();
  int numBits = 10;
  int numSif;
  static int testCount = 1;
  static int numberCount = 500;
  Random randomg = new Random();
	CyclicBarrier bwait, bfinish;
  // boolean filewrite = false;


  /*
   * findmax global variables.
   */
  int [] maxValues;
	static int [] gCount;





  /**
   * Constructor.
   */
  Oblig3() {
     maxValues = new int [q];
     numSif = (int) Math.pow(2.0, (double) numBits);
     MAX_VALUE = numSif;
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
    int m [] = populate(numberCount);
    int b [] = m.clone();
    String s, nl = "\n";
    s = "***Test***" + nl;
    s += "Testing with an array with _" + numberCount + "_ random numbers ";
    s += "and doing it _" + testCount + "_ times " + nl;
    pln(s);
    seq = runSeqTest(b, testCount);
    s = "Seq: " + seq;
    pln(s);
    b = m.clone();
    par = runParTest(b, testCount);
    s = "Par: " + par;
    pln(s);
  }




  /**
   * Run radix sort sequential.
   */
  double runSeqTest(int [] n, int testCount) {
    long [] t = new long [testCount];
    long startTime;
    int b [];
    for (int i = 0; i < t.length; i++) {
      b = n.clone();
      startTime = System.nanoTime();
      radix2(b);
      t[i] = System.nanoTime() - startTime;
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
    int b [];
    for (int i = 0; i < t.length; i++) {
      b = n.clone(); 
      startTime = System.nanoTime();
      radix2Par(b);
      t[i] = System.nanoTime() - startTime;
    }
    Arrays.sort(t);
    return t[testCount/2]/1000000.0;
  }




  /**
   * Print to screen and/or file.
   */
  synchronized static void pln(int s) {
    p("" + s + "\n");
  }




  /**
   * Print to screen and/or file.
   */
  synchronized static void pln(String s) {
    p(s + "\n");
  }




  /**
   * Print to screen and/or file.
   */
  synchronized static void pln() {
    p("\n");
  }




  /**
   * Print to screen and/or file.
   */
  synchronized static void p(int s) {
    p("" + s);
  }




  /**
   * Print to screen and/or file.
   */
  synchronized static void p(String s) {
    System.out.print(s);
    //if (filewrite) file.
  }

	synchronized static void printArray(int [][] allCount, int index) {
		if (index == 0)
			p("count  : ");
			for (int j = 0; j < allCount[0].length; j++)
				p(j + "; ");
			pln();
			for (int i = 0; i < q; i++) {
				p("count " + i + ": ");
				for (int j = 0; j < allCount[index].length; j++)
					p("" + allCount[i][j] + "; ");
				pln();
			}
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
   * Finds the largest value in the range between {@code startpoint}
   * and {@code endpoint} in an int array {@code n}.
   * @param n
   * @param startpoint
   * @param endpoint
   */
  int findMax(int [] n, int startpoint, int endpoint) {
    if (debug) pln("In array n we got: " + startpoint + " as a startpoint and "
        + endpoint + " as a endpoint.");
    int max = 0;
    for (int i = startpoint; i <= endpoint; i++)
      if (n[i] > max) max = n[i];
    return max;
  }




  /** 
   * Parallel version of radix sort with two digits.
   */
  void radix2Par(int [] a) {
    int l;
    int [][] allCount = new int[q][];
		int [][] allAcumCount = new int[q][];
    int [] sumCount;
    int [] b = new int [a.length];
    long starttime = System.nanoTime();
    bwait = new CyclicBarrier(q + 1);
    bfinish = new CyclicBarrier(q + 1);
    Runnable [] t = new Runnable [q];
    l = a.length;
    int max;
    int numBit = 2;



    // a, find maxValue.
    for (int i = 0; i < q; i++) {
      int startpoint = l/q*i;
      int endpoint;
      if (i != q - 1 )
        endpoint = l/q*(i+1)-1;
      else
        endpoint = l-1;
      t[i] = new MaxValueRunner(i, a, startpoint, endpoint);
      new Thread(t[i]).start();
    }
    
    try {
      bwait.await();
    } catch (Exception e) {return;}
    try {
      bwait.await();
      //sets maxValues[0] as max
      max = maxValues[0];
    } catch (Exception e) {return;}

    pln("a1 (para findmax): " + (System.nanoTime() - starttime)/1000000.0);
    System.out.println("par max: " + maxValues[0]);


    // end find maxValue.




		// Gjøres i en av trådene..
		while (max >= (1<<numBit)) numBit++; // antall siffer i max

		
		

    // b, count values
    bwait = new CyclicBarrier(q + 1);
    bfinish = new CyclicBarrier(q + 1);

    int bit1 = numBit/2,
        bit2 = numBit-bit1;

    int mask = (1<<bit1) -1;

		sumCount = new int[mask + 1];

    for (int i = 0; i < q; i++) {
      int startpoint = l/q*i;
      int endpoint;
      if (i != q - 1 )
        endpoint = l/q*(i+1)-1;
      else
        endpoint = l-1;
      //radixSort(a, b, bit1, 0); // første siffer fra a[] til b[]
      //radixSort(b, a, bit2, bit1);// andre siffer, tilbake fra b[] til a[]
      allCount[i] = new int [mask+1];
			allAcumCount[i] = new int [mask + 1];
      t[i] = new RadixRunner2(i, a, b, allCount, allAcumCount, startpoint, endpoint, bit1, 0);
      new Thread(t[i]).start();
    }
		try {
			bwait.await();
			starttime = System.nanoTime(); // starter å telle her for å se hvor lang tid det tar uten
			// oppstart av trådene osv. for å se sammenligning av algoritme.
			if (debug) pln(Thread.currentThread().getName() + " waiting");
			bwait.await();
			pln("b (para count values): " + (System.nanoTime() - starttime)/1000000.0 + "ms.");
			bwait.await();
			bwait.await();
			bwait.await();
			/* for (int j = 0; j < b.length; j++) { */
			/* 	pln(j + ": " + b[j] + " - " + Integer.toBinaryString(b[j])); */
			/* } */
			if (debug) pln(Thread.currentThread().getName() + " running");
		} catch (Exception e) {return;}

		/* try { */
		/* 	if (debug) pln(Thread.currentThread().getName() + " waiting again"); */
		/* 	bfinish.await(); */
		/* 	if (debug) pln(Thread.currentThread().getName() + " finished"); */
		/* } catch (Exception e) {return;} */

		// summerer opp count.
			/* for (int i = 0; i < allCount.length; i++) { */
			/* 	for (int j = 0; j < sumCount.length; j++) { */
			/* 		sumCount[j] += allCount[i][j]; */
			/* 	} */
			/* } */
		//new Thread(t[i] = new MaxValueRunner(i, a, startpoint, endpoint, bit2, bit1)).start();
		//t[i] = new RadixRunner(i, a, allCount[i], startpoint, endpoint, bit1, 0);
		//new Thread(t[i]).start();





    // c
    // d

  }




  /**
   * Count the frequency of each digit.
	 * Skritt B
   */
  int [] frequencyCount(int startpoint, int endpoint, int [] array,
                      int [] _localCount, int mask, int shift) {
		int [] localCount = new int [_localCount.length];
    for (int i = startpoint; i <= endpoint; i++) {
      localCount[(array[i]>> shift) & mask]++; }
		return localCount;
  }



	/**
	 * Sum up frequency count of the different threads.
	 * Eventuelt i skritt B.
	 * Trengs denne?
	 */
	void sumFrequencyCount(int [][] allCount) {
		// eller jobbe mot en globalcount?
		//int [] count = new int [/* ??? */];
		// sette sammen hvor mange 
		// return count;
	}



	/**
	 * Accumulate sums of the frequency count.
	 * Skritt C.
	 */
	// eller skal endpoint byttes ut med length?
	void accumulateFreqCount(int index, int [][] allCount) {
		// Men hvordan skal den klare å akkumulere i tråder, når vi er avhengige 
		// av all data til venstre i denne prosessen.
		//
		// => Sekvensielt gå gjennom i = fra 0 til count.length
		// (allCount[0].length) og legge sammen allCount[q-1][i] og
		// allAccumCount[q-1][i]. 
		//
		// Legger sammen akkumlerte fra siste akkumulert per tråd / per verdi og
		// siste verdi allCount (siste tråds allCount).
		// Kanskje parallelisere akkumuleringsdata for per tråd og så kjøre
		// akkumuleringen sekvensielt?
		//
		//	for (j = fra 0 til antall tråder - 1)
		//		count[i] = 
		//
		int n, acumVal = 0;
		for (int i = 0; i < allCount[index].length; i++) {
      n = allCount[q-1][i];
      allCount[index][i] = acumVal; acumVal += n;
		}
	}




	/**
	 * Acumulate per value.
	 * Del av skritt C.
	 * Hver tråd får en offset på hvor den skal sette inn verdiene
	 */
	void acumulatePerValueFreqCount(int startpoint, int endpoint, 
			int [][] allCount, int [][] allAcumCount) {
		int acumVal, n;
		for (int i = startpoint; i <= endpoint; i++) {
			acumVal = 0;
			for (int j = 0; j < q; j++) {
				allAcumCount[j][i] = acumVal;
				acumVal += allCount[j][i];
			}
			// her kan en lagre verdien som skal akumuleres opp i count. Den er
			// allerede utregnet.
			//	 sumCount[q-1] = acumVal;
			allCount[q-1][i] = acumVal;
		}
	}




	/**
	 * Gjennomføre D.
	 */
	void moveNumbers(int index, int startpoint, int endpoint, int [] fromArray, int []
			toArray, int [] count, int [][] allAcumCount, int shift, int mask) {


		int number, offset; // regner med at offset fjernes av jit og derfor ikke 
		//pln("Skriver ut med start: " + startpoint + " og slutt " + endpoint);
		// utfordrer ytelsen i antall kjøringer som er aktuelt her (husk flest først,
		// færrest til sist (milliarder og millioner før tusner - jit).
		String s;
		for (int i = startpoint; i <= endpoint; i++) {
			number = (fromArray[i]>>shift) & mask;
			// feil i offset!!!
			offset = allAcumCount[index][number]++;
			s = Thread.currentThread().getName();
			//pln(i + " i " + s + " putter inn " + fromArray[i] + " - " + Integer.toBinaryString(fromArray[i]) + " på (" + count[number] + " + " + offset + ")" + (count[number] + offset));
			toArray[count[number]+offset] = fromArray[i];
			//count[number]++;
			//			^								^  trengs disse parantesene?

			// count[(fromArray[i]>>shift) & mask]++ <= Denne (++) oppdaterer pointer
			// på hvor neste tall skal legges inn.
			// Her må også allAcumCount (offsett) tas med. allAcumCount[index] for å
			// hente ut for riktig tråd.
			// Og allAcumCount[][(fromArray[i]>>shift) & mask] for å få ut offsett
			// for riktig tall.
			// toArray[count[(fromArray[i]>>shift) & mask]++] = fromArray[i];
		}
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
    pln("seq max: " + max);


    if (debug) stoptime = System.nanoTime();
    if (debug) p("a1: " + ((stoptime - starttime)/1000000.0)+ "ms\n");

		
		//pln(">> " + numBit);

		while (max >= (1<<numBit)) numBit++; // antall siffer i max

		//pln(">> " + numBit);


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
			//pln(a[i] + "(" + Integer.toBinaryString(a[i]) + ") sorteres til: " + ((a[i]>> shift) & mask));
      count[(a[i]>> shift) & mask]++; }


    if (debug) stoptime = System.nanoTime();
    if (debug) p("b: " + ((stoptime - starttime)/1000000.0)+ "ms\n");
		//for (int i = 0; i < count.length; i++)
		//	pln("count[" + i + "]: " + count[i]);
		
		
	

    // c) Add up in 'count' - accumulated values 
		// for at tallene skal bli plassert riktig sted i punkt d.
		//if (debug) pln("mask: " + mask);
    if (debug) starttime = System.nanoTime();


    for (int i = 0; i <= mask; i++) {
      j = count[i];
      count[i] = acumVal; acumVal += j;
    }


    if (debug) stoptime = System.nanoTime();
    if (debug) p("c: " + ((stoptime - starttime)) + "ns\n");
    if (debug) pln("sjekk hvorfor 0.0!"); // fjern / 1000000.0




    // d) move numbers in sorted order a to b 
    if (debug) starttime = System.nanoTime();


    for (int i = 0; i < n; i++) {
      b[count[(a[i]>>shift) & mask]++] = a[i]; 
		}
		/* for (int i = 0; i < n; i++) { */
		/* 	pln(b[i] + ": " + Integer.toBinaryString(b[i])); */
		/* } */


    if (debug) stoptime = System.nanoTime();
    if (debug) p("d: " + ((stoptime - starttime)/1000000.0) + "ms\n");
  }// end radixSort





  /**
   * This class is a help class to start code in the different threads.
   * It will reuse the threads for each step in the process.
   * @param index         What thread.
   * @param a             What array.
   * @param startpoint    First value which the thread got responsibility for.
   * @param endpoint      Last value which the thread got responsibility for.
   */
  class MaxValueRunner implements Runnable {
    final int index, startpoint, endpoint;
    final int [] a;
    MaxValueRunner(int index, int [] a, int startpoint, int endpoint) {
      this.index = index;
      this.startpoint = startpoint;
      this.endpoint = endpoint;
      this.a = a;
    }
    public void run() {
      pfindMax();
      try {
        bwait.await();
      } catch (Exception e) {return;}
      // sets the maxValues[0] as max.
      if (index == 0)
        mergeMaxValues();
      try {
        bwait.await();
      } catch (Exception e) {return;}
    }



    /* a */
    void pfindMax() {
      maxValues[index] = findMax(a, startpoint, endpoint);
    }

    
    
    void mergeMaxValues() {
      for (int i = 1; i < maxValues.length; i++)
        if (maxValues[i] > maxValues[0]) maxValues[0] = maxValues[i];
    }
    /*b*/
    /*c*/
    /*d*/
  } // End Class MaxValueRunner




	/**
	 * Help class to start radix sort in parallel.
	 */
  class RadixRunner2 implements Runnable {
    int index, startpoint, endpoint, maskLen, shift, mask;
    int a[], b[], count[], allCount[][], allAcumCount[][];
		// ta inn bit1 og bit2 her, slik at vi kan kjøre gjennom begge turene uten
		// å måtte starte opp igjen ny tråder.
		// Bytte ut maskLen med bit1 og shift med bit2 så bruke disse som maskLen
		// og shift den ene og andre veien.
		RadixRunner2(int index, int [] a, int [] b, int [][] allCount, 
				int [][] allAcumCount, int startpoint, int endpoint, int maskLen,
				int shift) {
      this.index = index;
      this.startpoint = startpoint;
      this.endpoint = endpoint;
      this.maskLen = maskLen;
      this.shift = shift;
      this.a = a;
      this.b = b;
			this.allCount = allCount;
			this.allAcumCount = allAcumCount;
      this.count = allCount[index];
			this.mask = (1 << maskLen) - 1;
    }


    public void run() {
 			try {
				bwait.await();
			} catch (Exception e) {return;}
     // telle antall verdier i hvert sitt område.
			// legge inn en wait som gjør at alle er ferdige med oppstarten før
			// tidtakning.
			allCount[index] = frequencyCount(startpoint, endpoint, a, count, mask, shift);
			try {
				bwait.await();
				// summere opp counts fra parallel opptelling her.
				// sumParallelCounts(allCount);
			} catch (Exception e) {return;}
				
			// Telle opp hvor mange de tidligere trådene trenger av plass for en 
			// konkret verdi, så d (i samme operasjon).


			// C
			//
			//
			// C - finne offset for hvor hver tråd skal sette inn sine verdier.
			// hvilket sp og ep?
			// Trengere allAcum og allCount, nå har tråden kun sine counts.
			// Dele opp count i like store deler:
			// sp: allCount[0].length / q * i
			// ep: allCount[0].length / (q*i+1) - 1
			// og siste endpoint:
			// allCount[0].length - 1
			//
			int startpointacu = allCount[0].length/q*index;
			int endpointacu;
			if (index != q-1) 
				endpointacu = (allCount[0].length/q)*(index+1) - 1;
			else
				endpointacu = allCount[0].length - 1;
			// sørge for at allAcumCount er initiert her før metoden kjøres.
			/* for (int i = 0; i < allCount.length; i++) { */
			/* 	p("count " + i + ": "); */
			/* 	for (int j = 0; j < allCount[i].length; j++) */
			/* 		p("" + allCount[i][j] + "; "); */
			/* 	pln(); */
			/* } */
			acumulatePerValueFreqCount(startpointacu, endpointacu, allCount, allAcumCount);
			/* if (index == 0) for (int j = 0; j < allAcumCount[0].length; j++) { */
			/* 	p("acum " + j + ": "); */
			/* 	for (int i = 0; i < allAcumCount.length; i++) { */
			/* 		p("" + allAcumCount[i][j] + "; "); */
			/* 	} */
			/* 	pln(); */
			/* } */

			//vente på at alle trådene er ferdig.
 			try {
				bwait.await();
			} catch (Exception e) {return;} 
			// if (index == 0) printArray(allAcumCount, index);

			// C hoveddel, bruker tall fra 
			// if (index == 0) sekvensielt akumulere (eller la alle trådene legge
			// sammen, sparer en barrier før skritt D.
			if (index == 0) accumulateFreqCount(index, allCount);
			//printArray(allCount, index);



 			try {
				bwait.await();
			} catch (Exception e) {return;} 


			// D
			// Hvordan skal  moveNumbers() kalles? Her trengs en fraArray og en
			// tilArray. maskLen og shift er tilgjenglig. 
			// index, a fraArray, b tilArray, mask, shift
			// samme start og endpoint?
			// hvilken count-variabel?
			// trenger også acumAll-eller hva det var for noe.
			moveNumbers(index, startpoint, endpoint, a, b, allCount[0], allAcumCount, shift, mask);

 			try {
				bwait.await();
			} catch (Exception e) {return;} 





			/* try { */
			/* 	if (debug) pln(Thread.currentThread().getName() + " waiting again"); */
			/* 	bfinish.await(); */
			/* 	if (debug) pln(Thread.currentThread().getName() + " waiting finished"); */
			/* } catch (Exception e) {return;} */

    }
  }



} // End Oblig3 class
//pln(Thread.currentThread().getName() + " running");