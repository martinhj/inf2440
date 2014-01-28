// rapportinfo i bånn av fila.
// obs på hvor en måler hvor lang tid det tar!
// RB1:Test å sette runners[i].start(); i samme forløkka som trådene startes opp
// RB2: Er feil. Første tråd overlapper et tall med andre, så det blir da tall
// som ikke blir regnet med på slutten.
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;
import java.util.Collections;

class MaxValueOfArray {
// b1: dele opp arrayen mellom trådene, jobber mot samme variabel
// b2: jobbe opp mot samme array, forskjellige posisjoner, jobber mot samme var
// b3: synchronized metode som oppdaterer globalMax
// b4: localMax, synchronized
// passe på at alle testene er gjort før man går til neste! hvis en venter med
// return til de er helt ferdige (f.eks. si ifra at alle er ferdige til slutt).
//
// syclic barrier
ArrayList<String> results = new ArrayList<String>();
ArrayList<Long> times = new ArrayList<Long>();
ArrayList<Integer> findings = new ArrayList<Integer>();
// number of array elements
int n = 100000000; 
/* int n = 32; */
int numberContainer[];
int largest;
int rest, pl, l;
CyclicBarrier b;
Random ranGen = new Random();
public int cq = Runtime.getRuntime().availableProcessors();
public static void main (String [] args) {
    new MaxValueOfArray();
}

MaxValueOfArray() {
    numberContainer = new int[n];
    generateNumbers();
    // denne trenger utskriftsfunksjonalitet
    results.add("B2r parallel algo.");
    for (int i = 0; i < 9; i++) 
        findLargestB2r();
    printResult();
    /* for (int i = 0; i < 10; i++) { */
    /*     results.add(findLargestB3()); */
    /* } */
    results.add("A sequential algo.");
    for (int i = 0; i < 9; i++) {
        findLargestA();
    }
    printResult();
    results.add("B1 parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestB1();
    }
    printResult();
    results.add("B2 parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestB2();
    }
    printResult();
    results.add("B4 parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestB4();
    }
    printResult();
    results.add("B4r parallel algo.");
    for (int i = 0; i < 9; i++) {
        findLargestB4r();
    }
    printResult();
}

void printResult() {
    long sum = 0;
    Collections.sort(times);
    //System.out.println(report);
    for (String s: results)
        System.out.println(s);
    System.out.println("mean: " + (times.get(4)/1000000.0) + "ms");
    System.out.println("Results:");
    for (int i: findings)
        System.out.print(i + " ");
    times = new ArrayList<Long>();
    results = new ArrayList<String>();
    findings = new ArrayList<Integer>();
}

String findLargestA() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    for (int i = 0; i < numberContainer.length; i++)
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
    rest = numberContainer.length % cq;
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

/**
 * Denne er feil. En liten overlapp og en manglende int på slutten. Mangler
 * rest.
 */
String findLargestB2() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    b = new CyclicBarrier(cq + 1);
    for (int i = -1; i < cq -1; i++) {
        new Thread(new RB2(i)).start();
    }
    try {
        b.await();
    } catch (Exception e) {return null;}
    time = System.nanoTime() - startTime;
    String report = "ParB2 largest " + largest + ". ";
    report += "Time used: " + time;
    times.add(time);
    findings.add(largest);
    return report;
}

String findLargestB2r() {
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    b = new CyclicBarrier(cq + 1);
    new Thread(new RB2(-1)).start();
    new Thread(new RB2(0)).start();
    new Thread(new RB2(1)).start();
    new Thread(new RB2(2)).start();
    new Thread(new RB2(3)).start();
    new Thread(new RB2(4)).start();
    new Thread(new RB2(5)).start();
    new Thread(new RB2(6)).start();
    try {
        b.await();
    } catch (Exception e) {return null;}
    time = System.nanoTime() - startTime;
    String report = "ParB2 largest " + largest + ". ";
    report += "Time used: " + time;
    times.add(time);
    findings.add(largest);
    return report;
}

String findLargestB3() {
    // globalMax eller noe.
    //Bruk en synchronized metode som alle trådene kaller for hvert element de
    //har og som oppdaterer globalMax hvis det nye elementet er større enn
    //gammel verdi av globalMax.
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    b = new CyclicBarrier(cq + 1);
    new Thread(new RB3(-1)).start();
    new Thread(new RB3(0)).start();
    new Thread(new RB3(1)).start();
    new Thread(new RB3(2)).start();
    new Thread(new RB3(3)).start();
    new Thread(new RB3(4)).start();
    new Thread(new RB3(5)).start();
    new Thread(new RB3(6)).start();
    try {
        b.await();
    } catch (Exception e) {return null;}
    time = System.nanoTime() - startTime;
    String report = "ParB3 largest " + largest + ". ";
    report += "Time used: " + time;
    // en cyclicbarrier som sjekker at alle trådene er ferdige
    findings.add(largest);
    return report;
}
String findLargestB4() {
    // globalMax eller noe.
    //La alle trådene ha hver sin variabel localMax som de bruker til å finne
    //max i sin del av arrayen, Tilslutt kaller hver tråd da bare en gang på en
    //synchronized metode (samme for å sette golbalMax i b.3) som da finner ut
    //hvilken av trådenes lokale max som var størst.
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    b = new CyclicBarrier(cq + 1);
    new Thread(new RB4(-1)).start();
    new Thread(new RB4(0)).start();
    new Thread(new RB4(1)).start();
    new Thread(new RB4(2)).start();
    new Thread(new RB4(3)).start();
    new Thread(new RB4(4)).start();
    new Thread(new RB4(5)).start();
    new Thread(new RB4(6)).start();
    try {
        b.await();
    } catch (Exception e) {return null;}
    time = System.nanoTime() - startTime;
    String report = "ParB4 largest " + largest + ". ";
    report += "Time used: " + time;
    times.add(time);
    findings.add(largest);
    // en cyclicbarrier som sjekker at alle trådene er ferdige
    return report;
}

String findLargestB4r() {
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
class RB2 extends Runner {
    // denne er feil siden den jobber på en felles variabel uten at den vet om
    // andre jobber på den samme.
    int index;
    RB2(int i) {
        index = i;
    }
    void findLargest() {
        if (numberContainer[index+1] > largest) 
            largest = numberContainer[index+1];
        for (int j = 1; j < numberContainer.length / cq; j++) {
            if (numberContainer[j*cq+index] > largest)
                largest = numberContainer[j*cq+index];
        }
//!!    // Gå gjennom array etter mønster
//!!    // Hvordan skal den ta seg av de siste?
//!!    // f.eks: hvis index == cq, fra siste i system til
//!!    // numberContainer.length
    }
    public void run() {
        findLargest();
        try {
            b.await();
        } catch (Exception e) {return;}
    }
}
class RB3 extends Runner {
    // denne er feil siden den jobber på en felles variabel uten at den vet om
    // andre jobber på den samme.
    int index;
    RB3(int i) {
        index = i;
    }
    void findLargest() {
        findLargestSync(numberContainer[index+1]);
        for (int j = 1; j < numberContainer.length / cq; j++)
            findLargestSync(numberContainer[j*cq+index]);
        // Gå gjennom array etter mønster
        // Hvordan skal den ta seg av de siste?
        // f.eks: hvis index == cq, fra siste i system til
        // numberContainer.length
    }
    public void run() {
        findLargest();
        try {
            b.await();
        } catch (Exception e) {return;}
    }
}
class RB4 extends Runner {
    // denne er feil siden den jobber på en felles variabel uten at den vet om
    // andre jobber på den samme.
    int index;
    int largestL = 0;
    RB4(int i) {
        index = i;
    }
    void findLargest() {
        if (numberContainer[index+1] > largestL)
            largestL = numberContainer[index+1];
        for (int j = 1; j < numberContainer.length / cq; j++) {
            if (numberContainer[j*cq+index] > largestL)
                largestL = numberContainer[j*cq+index];
        }
    }
    void findLargestGlobal() {
        findLargestSync(largestL);
    }
    public void run() {
        findLargest();
        try {
            b.await();
        } catch (Exception e) {return;}
        findLargestGlobal();
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
    }
    void findLargestGlobal() {
        findLargestSync(largestL);
    }

    public void run() {
        findLargest();
        try {
            b.await();
        } catch (Exception e) {return;}
        findLargestGlobal();
    }
}
}
// Lag en liten rapport om tidsforbruket med de 4 mulige varianter av
// algoritmen + tidsforbruket til den sekvensielle algoritmen hvor du også
// regner ut speedup S. Skriv også om det synes mulig å få S >1 for noen av
// disse fire alternativene. Kommenter hvorfor noen av algoritmene evt. er
// spesielt raske eller spesielt trege.