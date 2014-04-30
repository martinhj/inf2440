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
  Random randomg = new Random(54544);
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
    String s, nl = "\n";
    s = "***Test***" + nl;
    s += "Testing with an array with _" + numberCount + "_ random numbers ";
    s += "and doing it _" + testCount + "_ times " + nl;
    pln(s);
    seq = runSeqTest(m, testCount);
    s = "Seq: " + seq;
    pln(s);
    par = runParTest(m, testCount);
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
			n = populate(numberCount);
      startTime = System.nanoTime();
      radix2(n);
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
    for (int i = 0; i < t.length; i++) {
			n = populate(numberCount);
      startTime = System.nanoTime();
      radix2Par(n);
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
	 * Accumulate sums of the frequency count.
	 * Skritt C.
	 */
	void accumulateFreqCount(int index, int [][] allCount) {
		int n, acumVal = 0;
		for (int i = 0; i < allCount[index].length; i++) {
      n = allCount[q-1][i];
      allCount[index][i] = acumVal; acumVal += n;
		}
	}




	/**
	 * Acumulate per value.
	 * Del av skritt C.
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
			// lagrer verdien i den siste trådens plass i allCount
			allCount[q-1][i] = acumVal;
		}
	}




	/**
	 * Move values from one array to another.
	 * Skritt D
	 */
	void moveNumbers(int index, int startpoint, int endpoint, int [] fromArray, int []
			toArray, int [] count, int [][] allAcumCount, int shift, int mask) {


		int number, offset; 
		for (int i = startpoint; i <= endpoint; i++) {
			number = (fromArray[i]>>shift) & mask;
			offset = allAcumCount[index][number]++;
			toArray[count[number]+offset] = fromArray[i];
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
      t[i] = new RadixRunner2(i, a, b, allCount, allAcumCount, startpoint, endpoint, bit1, 0, bit2);
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
			bwait.await();




			bwait.await();
			starttime = System.nanoTime(); // starter å telle her for å se hvor lang tid det tar uten
			// oppstart av trådene osv. for å se sammenligning av algoritme.
			if (debug) pln(Thread.currentThread().getName() + " waiting");
			bwait.await();
			pln("b (para count values): " + (System.nanoTime() - starttime)/1000000.0 + "ms.");
			bwait.await();
			bwait.await();
			bwait.await();
			



			if (debug) pln(Thread.currentThread().getName() + " running");
		} catch (Exception e) {return;}
  }




	/**
	 * Help class to start radix sort in parallel.
	 */
  class RadixRunner2 implements Runnable {
    int index, startpoint, endpoint, maskLen, shift, mask, bit2;
    int a[], b[], count[], allCount[][], allAcumCount[][];
		RadixRunner2(int index, int [] a, int [] b, int [][] allCount, 
				int [][] allAcumCount, int startpoint, int endpoint, 
				int maskLen, int shift, int bit2) {
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
			this.bit2 = bit2;
    }


    public void run() {
 			try {
				bwait.await();
			} catch (Exception e) {return;}
			allCount[index] = frequencyCount(startpoint, endpoint, a, count, mask, shift);
			try {
				bwait.await();
			} catch (Exception e) {return;}
			// end b


			// C
			int startpointacu = allCount[0].length/q*index;
			int endpointacu;
			if (index != q-1) 
				endpointacu = (allCount[0].length/q)*(index+1) - 1;
			else
				endpointacu = allCount[0].length - 1;
			acumulatePerValueFreqCount(startpointacu, endpointacu, allCount, allAcumCount);

 			try {
				bwait.await();
			} catch (Exception e) {return;} 

			// C hoveddel, bruker tall fra 
			if (index == 0) accumulateFreqCount(index, allCount);



 			try {
				bwait.await();
			} catch (Exception e) {return;} 


			// D
			moveNumbers(index, startpoint, endpoint, a, b, allCount[0], allAcumCount, shift, mask);

 			try {
				bwait.await();
			} catch (Exception e) {return;} 



			// b igjen
			shift = maskLen;
			mask = (1 << bit2) - 1;

 			try {
				bwait.await();
			} catch (Exception e) {return;} 
			try {
				bwait.await();
			} catch (Exception e) {return;}
			allCount[index] = frequencyCount(startpoint, endpoint, b, count, mask, shift);
			try {
				bwait.await();
			} catch (Exception e) {return;}

			// C
			startpointacu = allCount[0].length/q*index;
			if (index != q-1) 
				endpointacu = (allCount[0].length/q)*(index+1) - 1;
			else
				endpointacu = allCount[0].length - 1;
			acumulatePerValueFreqCount(startpointacu, endpointacu, allCount, allAcumCount);
 			try {
				bwait.await();
			} catch (Exception e) {return;} 

			// C hoveddel, bruker tall fra 
			if (index == 0) accumulateFreqCount(index, allCount);



 			try {
				bwait.await();
			} catch (Exception e) {return;} 

			moveNumbers(index, startpoint, endpoint, b, a, allCount[0], allAcumCount, shift, mask);

 			try {
				bwait.await();
			} catch (Exception e) {return;} 

    }
  }



} // End Oblig3 class
//pln(Thread.currentThread().getName() + " running");