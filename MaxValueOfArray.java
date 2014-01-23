// rapportinfo i bånn av fila.
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
int n = 10000000; // number of array elements
int numberContainer[];
int largest;
CyclicBarrier b;
Random ranGen = new Random();
public int cq = Runtime.getRuntime().availableProcessors();
public static void main (String [] args) {
    new MaxValueOfArray();
}

MaxValueOfArray() {
    numberContainer = new int[n];
    generateNumbers();
    results.add(findLargestA());
    results.add(findLargestB1());
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
    String report = "Seq largest " + largest + ". ";
    report += "Time used: " + time;
    return report;
}
String findLargestB1() {
    // mat ut nye arrays til tråder
    b = new CyclicBarrier(cq + 1);
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    // kode kjøres her.
    int rest = numberContainer.length % cq;
    int l = numberContainer.length/cq;
    Thread runners[] = new Thread[cq];
    for (int i = 0; i < cq-1; i++) {
        runners[i] = 
        new Thread(new RB1(Arrays.copyOfRange(numberContainer, i*l,i*l+l)));
    }
    runners[cq-1] = 
    new Thread(new RB1(Arrays.copyOfRange(numberContainer, (cq-1)*l,(cq-1)*l+l+rest)));
    for (int i = 0; i < runners.length; i++)
        runners[i].start();



    time = System.nanoTime() - startTime;
    // en cyclicbarrier som sjekker at alle trådene er ferdige
    try {
        b.await();
    } catch (Exception e) {return null;}
    String report = "ParB1 largest " + largest + ". ";
    report += "Time used: " + time;
    return report;
}

String findLargestB2() {
    // mat ut hvor den skal jobbe på arrayen til trådene
    // hvis metoden i tråden kan få en index å jobbe ut fra - ok.
    // egen klasse for dette.
    // Deretter deler vi arrayen a[] slik at tråd 0 tester element: 0, k-1,
    // 2k-1, 3k-1,..osv. Tråd 1 tester element nr. 1, k, 2k, .... Tråd 2 tester
    // element 2, k+1, 2k+1,...
    largest = 0;
    long time = 0;
    long startTime = System.nanoTime();
    // kode her.. starte tråder med en index. Tråden finner ut hva utfra index. 
    time = System.nanoTime() - startTime;
    String report = "ParB2 largest " + largest + ". ";
    report += "Time used: " + time;
    // en cyclicbarrier som sjekker at alle trådene er ferdige
    return report;
}
String findLargestB3() {
    // globalMax eller noe.
    //Bruk en synchronized metode som alle trådene kaller for hvert element de
    //har og som oppdaterer globalMax hvis det nye elementet er større enn
    //gammel verdi av globalMax.
    largest = 0;
    long time = 0;
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
    String report = "ParB4 largest " + largest + ". ";
    report += "Time used: " + time;
    // en cyclicbarrier som sjekker at alle trådene er ferdige
    return report;
}
void generateNumbers() {
    for (int i = 0; i < n; i++) {
        numberContainer[i] = ranGen.nextInt(n);
    }
}

synchronized void checkLarger(int n) {
    if (n > largest) largest = n;
}

class Runner implements Runnable {
    Runner() {
    }
    void findLargest() {}
    public void run() {
        findLargest();
        try {
            b.await();
        } catch (Exception e) {return;}
    }
}
class RB1 extends Runner {
    // denne er feil siden den jobber på en felles variabel uten at den vet om
    // andre jobber på den samme.
    int numberContainer[];
    RB1(int [] nc) {
        numberContainer = nc;
    }
    void findLargest() {
        for (int i = 0; i < numberContainer.length; i++)
            if (numberContainer[i] > largest) largest = numberContainer[i];
    }
}
class RB2 extends Runner {
    // denne er feil siden den jobber på en felles variabel uten at den vet om
    // andre jobber på den samme.
    //
    // Deretter deler vi arrayen a[] slik at tråd 0 tester element: 0, k-1,
    // 2k-1, 3k-1,..osv. Tråd 1 tester element nr. 1, k, 2k, .... Tråd 2 tester
    // element 2, k+1, 2k+1,...
    int index;
    RB2(int i) {
        index = i;
    }
    void findLargest() {
        // Gå gjennom array etter mønster
        // Hvordan skal den ta seg av de siste?
        // f.eks: hvis index == cq, fra siste i system til
        // numberContainer.length
    }
}
}
// Lag en liten rapport om tidsforbruket med de 4 mulige varianter av
// algoritmen + tidsforbruket til den sekvensielle algoritmen hvor du også
// regner ut speedup S. Skriv også om det synes mulig å få S >1 for noen av
// disse fire alternativene. Kommenter hvorfor noen av algoritmene evt. er
// spesielt raske eller spesielt trege.