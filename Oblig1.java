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
//import java.util.Integer;
// Bedre enn S > 1
class Oblig1 {
    Random randomg = new Random();
    static int c = 1000000;
    final static int MAX_VALUE = 1000;
    int ns[], ns2[];
    public static void main(String [] args) {
        if (args.length > 0) c = Integer.parseInt(args[0]);
        new Oblig1();
    }
    Oblig1() {
        ns = new int[c];
        ns2 = new int[c];
        generateNumbers();
        /* ns = new int[12]; */
        /* ns[0] = 3; ns[1] = 4; ns[2] = 1; ns[3] = 5; */
        /* ns[4] = 3; ns[5] = 4; ns[6] = 1; ns[7] = 5; */
        /* ns[8] = 3; ns[9] = 4; ns[10] = 1; ns[11] = 5; */
        ns2 = ns.clone();
        iSortSeq(ns, 0, 0);
        iSortRest(ns);
        Arrays.sort(ns2);
        for (int n = 49; n >= 0; n--) {
            System.out.println(n + " : " + (ns[n] == ns2[c - 1 - n]));
        }
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