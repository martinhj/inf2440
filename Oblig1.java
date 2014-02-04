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
//import java.util.Integer;
// Bedre enn S > 1
class Oblig1 {
    Random randomg = new Random();
    static int c = 100;
    final static int MAX_VALUE = 1000;
    int ns[];
    public static void main(String [] args) {
        if (args.length > 0) c = Integer.parseInt(args[0]);
        new Oblig1();
    }
    Oblig1() {
        ns = new int[c];
        generateNumbers();
        iSortSeq(ns, 0, 99);
        for (int n: ns)
            System.out.println(n);
    }

void generateNumbers() {
    for (int i = 0; i < c; i++) {
        ns[i] = randomg.nextInt(MAX_VALUE);
    }
} //end generateNumbers

void iSortSeq(int[] a, int v, int h) {
// 50 første
// motsatt rekkefølge som eksempelet
// putt inn eventuelle større verdier inn i de øverste 50

// innstikkssortering, stigende rekkefølge skal være synkende rekkefølge
// (0..49)
    int i, t;
    for (int k=v; k < h; k++) {
        t = a[k+1];
        i = k;
        while (i > v && a[i] > t) {
            a[i+1] = a[i];
            i--;
        }
        a[i+1] = t;
    } // end for k
} // end insertSort

} // end class Oblig1