// TODO:
// Timing.
// Sammenligne med Array.sort - eller noe...
//
//
//
//
//  Arne Maus implementasjon:
    /* int i, t; */
    /* for (int k=v; k < h; k++) { */
    /*     t = a[k+1]; */
    /*     i = k; */
    /*     while (i > v && a[i] > t) { */
    /*         a[i+1] = a[i]; */
    /*         i--; */
    /*     } */
    /*     a[i+1] = t; */
    /* } // end for k */
// generere random tall til array
// starte med en array på 100 (sekvensiell, blir for lite å parallellisere)
// sortere 50 første sekvensielt
// sette inn de som er større i resten av arrayen på de 50 første plassene.
//

// parallellisering:
// flette sammen to og to? så fort to er ferdig bruke den ene av disse trådene
// til å begynne å flette.
// teste mot sekvensielt å flette sammen alle på en gang.

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Integer;
// Bedre enn S > 1
class Oblig1 {
    Random randomg = new Random();
    static int c = 1000;
    final static int MAX_VALUE = 1000;
    ArrayList<Long> itimes = new ArrayList<Long>();
    ArrayList<Long> atimes = new ArrayList<Long>();
    int ns[], ns2[], nstemp[];
    public static void main(String [] args) {
        if (args.length > 0) c = Integer.parseInt(args[0]);
        new Oblig1();
    }
    Oblig1() {
        ns = new int[c];
        ns2 = new int[c];
        nstemp = new int[c];
        generateNumbers();
        ns2 = ns.clone();
        nstemp = ns.clone();
        long time = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < 9; i++) {
            ns = nstemp.clone();
            startTime = System.nanoTime();
            iSortSeq(ns, 0, 0);
            iSortRest(ns);
            itimes.add(time = System.nanoTime() - startTime);
        }
        Collections.sort(itimes);
        System.out.println("mean: " + (itimes.get(4) / 1000000.0) + "ms");
        for (int i = 0; i < 9; i++) {
            ns2 = nstemp.clone();
            startTime = System.nanoTime();
            Arrays.sort(ns2);
            atimes.add(time = System.nanoTime() - startTime);
        }
        Collections.sort(atimes);
        System.out.println("mean: " + (atimes.get(4) / 1000000.0) + "ms");
        for (int n = 49; n >= 0; n--) {
            if (ns[n] != ns2[c - 1 - n]) System.out.println(false);
        }
        System.out.print("Speedup S: ");
        System.out.println(
                String.format("%2.02f", (float)atimes.get(4)/itimes.get(4)));
    }
void generateNumbers() {
    for (int i = 0; i < c; i++) {
        ns[i] = randomg.nextInt(MAX_VALUE);
    }
} //end generateNumbers
/**
 * Sorterer de 50 første verdiene i arrayen a.
 */
void iSortSeq(int[] a, int v, int h) {
// v og h brukes ikke nå i denne implementasjonen.
    /* int i, t; */
    /* for (int k=v; k < h; k++) { */
    /*     t = a[k+1]; */
    /*     i = k; */
    /*     while (i > v && a[i] > t) { */
    /*         a[i+1] = a[i]; */
    /*         i--; */
    /*     } */
    /*     a[i+1] = t; */
    /* } // end for k */
    int j;
    int temp;
    for (int i = 49; i >= 0; i--) {
        j = i;
        temp = a[i];
        while(j < 49 && temp < a[j + 1]) {
            a[j] = a[j + 1];
            j++;
        } // end j
        a[j] = temp;
    } // end i
} // end insertSort
void iSortRest(int[] a) {
    int temp, j;
    for (int i = 50; i < c; i++) {
        if (a[i] > a[49]) {
            temp = a[i]; a[i] = a[49]; j = 48;
            while(j >= 0 && temp > a[j]) {
                a[j+1] = a[j];
                j--;
            }
        a[j+1] = temp;
        }
    }
}
} // end class Oblig1


// en tråd får en indeks, utfra denne regnes hvilket område som skal jobbes på i array.
// Skrive om metodene slik at de tar imot argumenter for start og stopp.
// Ta inn argumenter til programmet for å styre antall tråder og antall tall.
// iSort tar inn argument for hvor den skal starte, så skal den sortere 50 derfra.
// iSortRest tar inn samme argument for hvor den skal jobbe i arrayen og jobber mot de
// 50 første.