// Bedre enn S > 1
class Oblig1 {
    public static void main(String [] args) {
    }
    Oblig1() {
    }
}



/*

innstikkssortering, stigende rekkefølge
skal være synkende rekkefølge (0..49)
void insertSort (int[] a, int v, int h) {
    int i, t;
    for (int k=v; k < h; k++) {
        t = a[k+1];
        i = k;
        while (i > v && a[i] > t) {
            a[i+1] = a[i];
            i--;
        }
        a[i+1] = t;
    } // end for k
} // end insertSort
*/