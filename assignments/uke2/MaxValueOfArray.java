// Warning: This code is written to test performance in parallel vs sequential
// algorithms. It's not precise as it will not go through all of the values if
// there is a remainder when the number of values are devided with number of
// threads.


// Lag utregning for speedup!

// rapportinfo i bånn av fila.
// obs på hvor en måler hvor lang tid det tar!
// RB1:Test å sette runners[i].start(); i samme forløkka som trådene startes opp
// RB2: Er feil. Første tråd overlapper et tall med andre, så det blir da tall
// som ikke blir regnet med på slutten.

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.Arrays;
import java.util.Collections;

class MaxValueOfArray {
ArrayList<String> results = new ArrayList<String>();
ArrayList<Long> times = new ArrayList<Long>();
ArrayList<Integer> findings = new ArrayList<Integer>();
static int n = 1000000;
int numberContainer[];
int largest;
int rest, pl, l;
CyclicBarrier b;
Semaphore s;
Random ranGen = new Random();
public static int cq;
int largestc[] = new int[cq];
Thread [] t = new Thread[cq];
public static void main (String [] args) {
    if (args.length > 0) cq = Integer.parseInt(args[0]);
    else cq = Runtime.getRuntime().availableProcessors();
    if (args.length > 1) n = Integer.parseInt(args[1]);
    System.out.println("Running with " + cq + " threads.");
    System.out.println("Testing " + n + " integers.");
    new MaxValueOfArray();
}

MaxValueOfArray() {
    numberContainer = new int[n];
    generateNumbers();
    results.add("A sequential algo.");
    for (int i = 0; i < 9; i++) {
        findLargestA();
    }
    printResult();
    
    results.add("Barrier parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestBarrier();
    }
    printResult();

    results.add("Semaphore parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestSemaphore();
    }
    printResult();

    results.add("Join parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestJoin();
    }
    printResult();
}

void printResult() {
    long sum = 0;
    Collections.sort(times);
    for (String s: results)
        System.out.println(s);
    System.out.println("mean: " + (times.get(4)/1000000.0) + "ms");
    System.out.println("Results:");
    for (int i: findings)
        System.out.print(i + " ");
    System.out.println();
    times = new ArrayList<Long>();
    results = new ArrayList<String>();
    findings = new ArrayList<Integer>();
}

String findLargestA() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    for (int i = 0; i < n; i++)
        if (numberContainer[i] > largest) largest = numberContainer[i];
    time = System.nanoTime() - startTime;
    String report = "Seq   largest " + largest + ". ";
    report += "Time used: " + time;
    times.add(time);
    findings.add(largest);
    return report;
}

String findLargestB1() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    b = new CyclicBarrier(cq + 1);
    pl = numberContainer.length/cq;
    l = numberContainer.length;
    for (int i = 0; i < cq; i++) {
        new Thread(new RB1(i)).start();
    }
    // denne ganmle får med seg rest, men gjør flere beregninger før den sender
    // ut til trådene.
    /* for (int i = 0; i < cq-1; i++) { */
    /*     new Thread(new RB1(i*pl,i*pl+pl)).start(); */
    /* } */
    /* new Thread(new RB1((cq-1)*pl,(cq-1)*pl+pl+rest)).start(); */
    try {
        b.await();
    } catch (Exception e) {return null;}
    time = System.nanoTime() - startTime;
    String report = "ParB1 largest " + largest + ". ";
    report += "Time used: " + time;
    times.add(time);
    findings.add(largest);
    return report;
}

void findLargestBarrier() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    b = new CyclicBarrier(cq + 1);
    rest = numberContainer.length % cq;
    pl = numberContainer.length/cq;
    l = numberContainer.length;
    for (int i = 0; i < cq; i++) {
        new Thread(new RB4r(i)).start();
    }
    try {
        b.await();
    } catch (Exception e) {return;}
    for (int n: largestc)
        if (n > largest) largest = n;
    time = System.nanoTime() - startTime;
    times.add(time);
    findings.add(largest);
    largestc = new int[cq];
}

// denne mangler at sammenligningen av de lokale store blir sammenlignet
// kan det gjøres i main (array med de store, finne største av disse etter join
void findLargestJoin() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    pl = numberContainer.length/cq;
    l = numberContainer.length;
    for (int i = 0; i < cq; i++) {
        (t[i] = new Thread(new RBJoin(i))).start();
    }
    for (int i = 0; i < cq; i++) {
        try {
            t[i].join();
        } catch (Exception e) {return;}
    }
    for (int n: largestc)
        if (n > largest) largest = n;
    time = System.nanoTime() - startTime;
    times.add(time);
    findings.add(largest);
    largestc = new int[cq];
}


// denne mangler at sammenligningen av de lokale store blir sammenlignet
// kan det gjøres i main (array med de store, finne største av disse etter join
void findLargestSemaphore() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    s = new Semaphore(-cq + 1);
    pl = numberContainer.length/cq;
    l = numberContainer.length;
    for (int i = 0; i < cq; i++) {
        new Thread(new RBSem(i)).start();
    }
    try {
        s.acquire();
        // sammenligning av største trenger ikke å gi den største her!
    } catch (Exception e) {return;}
    for (int n: largestc)
        if (n > largest) largest = n;
    time = System.nanoTime() - startTime;
    times.add(time);
    findings.add(largest);
    largestc = new int[cq];
}

void generateNumbers() {
    for (int i = 0; i < n; i++) {
        numberContainer[i] = ranGen.nextInt(n);
    }
}

synchronized void findLargestSync(int n) {
    if (n > largest) largest = n;
}

class Runner implements Runnable {
    Runner() {
    }
    public void run() {}
}

class RB1 extends Runner {
    // denne er feil siden den jobber på en felles variabel uten at den vet om
    // andre jobber på den samme.
    // tar ikke med seg rest, se kommentar (!!) i RB2 for en mulig løsning på
    // dette.
    int i;
    RB1(int index) {
        i = index;
    }
    void findLargest() {
        for (int j = i*pl; j < i*pl+pl; j++) {
            if (numberContainer[j] > largest)
                largest = numberContainer[j];
        }
    }

    public void run() {
        findLargest();
        try {
            b.await();
        } catch (Exception e) {return;}
    }
}

class RB4r extends Runner {
    // tar ikke med seg rest, se kommentar (!!) i RB2 for en mulig løsning på
    // dette.
    // den dummer seg ut i sammenligningen selv om den bruker en synchronized
    // metode for å sammenligne verdiene.
    int i;
    int largestL = 0;
    RB4r(int index) {
        i = index;
    }
    /* RB1(int start, int stop) { */
    /*     st = start; */
    /*     sp = stop; */

    /* } */
    void findLargest() {
        for (int j = i*pl; j < i*pl+pl; j++) {
            if (numberContainer[j] > largestL)
                largestL = numberContainer[j];
        }
        largestc[i] = largestL;
    }
    void findLargestGlobal() {
        findLargestSync(largestL);
    }

    public void run() {
        findLargest();
        try {
            b.await();
            /* findLargestGlobal(); */
            /* b.await(); */
        } catch (Exception e) {return;}
    }
}

class RBJoin extends Runner {
    // tar ikke med seg rest, se kommentar (!!) i RB2 for en mulig løsning på
    // dette.
    // den dummer seg ut i sammenligningen selv om den bruker en synchronized
    // metode for å sammenligne verdiene.
    int i;
    int largestL = 0;
    RBJoin(int index) {
        i = index;
    }
    /* RB1(int start, int stop) { */
    /*     st = start; */
    /*     sp = stop; */

    /* } */
    void findLargest() {
        for (int j = i*pl; j < i*pl+pl; j++) {
            if (numberContainer[j] > largestL)
                largestL = numberContainer[j];
        }
        largestc[i] = largestL;
    }

    public void run() {
        findLargest();
    }
}

class RBSem extends Runner {
    // tar ikke med seg rest, se kommentar (!!) i RB2 for en mulig løsning på
    // dette.
    // den dummer seg ut i sammenligningen selv om den bruker en synchronized
    // metode for å sammenligne verdiene.
    int i;
    int largestL = 0;
    RBSem(int index) {
        i = index;
    }
    /* RB1(int start, int stop) { */
    /*     st = start; */
    /*     sp = stop; */

    /* } */
    void findLargest() {
        for (int j = i*pl; j < i*pl+pl; j++) {
            if (numberContainer[j] > largestL)
                largestL = numberContainer[j];
        }
        // Dette løser presisjonsproblemet. Ser ikke ut som det har så mye å si
        // for kjøretiden (en test per tråd med noen uthentinger + igangkjøring
        // og testing på rest hvis denne finnes).
        if (i == cq - 1) 
            for (int j = i*pl+pl; j < n; j++) {
                largestL = numberContainer[j];
                System.out.println(j);
            }
        largestc[i] = largestL;
    }
    void findLargestGlobal() {
        findLargestSync(largestL);
    }

    public void run() {
        findLargest();
        try {
            s.release();
        } catch (Exception e) {return;}
    }
}
}
// Lag en liten rapport om tidsforbruket med de 4 mulige varianter av
// algoritmen + tidsforbruket til den sekvensielle algoritmen hvor du også
// regner ut speedup S. Skriv også om det synes mulig å få S >1 for noen av
// disse fire alternativene. Kommenter hvorfor noen av algoritmene evt. er
// spesielt raske eller spesielt trege.