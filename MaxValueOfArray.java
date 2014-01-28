// rapportinfo i bånn av fila.
// obs på hvor en måler hvor lang tid det tar!
// RB1:Test å sette runners[i].start(); i samme forløkka som trådene startes opp
// RB2: Er feil. Første tråd overlapper et tall med andre, så det blir da tall
// som ikke blir regnet med på slutten.
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;

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
int n = 100000000; // number of array elements
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
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    results.add(findLargestB2r());
    for (int i = 0; i < 10; i++) {
        results.add(findLargestB3());
    }
    for (int i = 0; i < 10; i++) {
        results.add(findLargestA());
    }
    for (int i = 0; i < 10; i++) {
        results.add(findLargestB1());
    }
    results.add(findLargestB4());
    for (String s: results)
        System.out.println(s);
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
    // en cyclicbarrier som sjekker at alle trådene er ferdige
    return report;
}
void generateNumbers() {
    for (int i = 0; i < n; i++) {
        numberContainer[i] = ranGen.nextInt(n);
    }
}

synchronized void findLargest() {
    System.out.println("NO!");
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
    int st;
    int sp;
    int i;
    RB1(int index) {
        i = index;
    }
    /* RB1(int start, int stop) { */
    /*     st = start; */
    /*     sp = stop; */

    /* } */
    void findLargest() {
        for (int j = i*pl; j < i*pl+pl; j++) {
            if (numberContainer[j] > largest)
                largest = numberContainer[j];
        }
    }
    void findLargestB() {
        for (int i = st; i < sp; i++) {
            if (numberContainer[i] > largest) largest = numberContainer[i];
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
    //
    // Deretter deler vi arrayen a[] slik at tråd 0 tester element: 0, k-1,
    // 2k-1, 3k-1,..osv. Tråd 1 tester element nr. 1, k, 2k, .... Tråd 2 tester
    // element 2, k+1, 2k+1,...
	// hva gjøres med rest?
	// k - antall tråder (4)
    // nc.lengde / k er her det som ganges med k
    // 10 % 4 = 2. To tall som ikke blir behandlet.
    // 10 / 4 = 2, 2 * 4 = 8: 10 - 8 er lik rest.
    // hvis sende med tall fra 2*4=8(cq*nc.lengde/cq) til nc.lengde-1 så er
    // også rest blitt behandlet.
	//  0   1   2   3   4   5   6   7   8   9
	// [3] [4] [7] [1] [3] [3] [5] [7] [0] [0]
	// T0 [0][k-1][2k-1][3k-1]
	//     0   3    7     11
	// T1 [1][k+0][2k+0][3k+0]
	//     1   4    8     12
	// T2 [2][k+1][2k+1][3k+1]
	//     2   5    9     13
	// T3 [3][k+2][2k+2][3k+2]
	//     3   6    10    14
	// kan vi starte med index -1?
	// altså k-index og så blir det 
	// -1:0 -1:1 -1:2
	// [1-1][k-1][2k-1]
	//  0:0  0:1  0:2
	// [ 1-0][k-0][2k-0]
	//  1:0  1:1  1:2
	// [ 1+1][k+1][2k+1]
	// du har index 0 - må forholde seg til n / k (9 / 4)
	// 1. 0   nc[0]: 3
	// 2. k-1 nc[3]:
	// 3. 2k-1nc[7]:
	// 4. 3k-1nc[X]: // null!
    // du har index 2
    //
    /*
    // oppretter tråden(må nok være i metoden som setter i gang denne) i gang
    // trådene og at de tar med seg index i
    for (int i = -1; i < cq -1; i++) {
        // dette kjøres i run() i tråden.
        for (int j = 0; j < nc.length / cq; i++) {
            // i er her det som er laget i forløkken over, men kanskje en
            // variabel i tråden (slik at det ikke blir tull når forløkken går
            // videre og får nye verdier av i;
            nc[j*cq+i] //denne stemmer for alle bortsett fra første posisjon og
            // rest
        }
    }
    */
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
}
// Lag en liten rapport om tidsforbruket med de 4 mulige varianter av
// algoritmen + tidsforbruket til den sekvensielle algoritmen hvor du også
// regner ut speedup S. Skriv også om det synes mulig å få S >1 for noen av
// disse fire alternativene. Kommenter hvorfor noen av algoritmene evt. er
// spesielt raske eller spesielt trege.