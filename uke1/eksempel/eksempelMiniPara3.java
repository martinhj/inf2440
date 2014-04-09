import java.util.concurrent.*;

class Problem {
      int [] data ; // dette er felles, delte data for alle trådene
      Thread[] t ;  // peker til alle de nye trådene
       public static void main(String [] args) {
                    new Problem().utfoer();
        }
        void utfoer () {
            t = new Thread [Runtime.getRuntime().availableProcessors()];
            for (int i =0; i < t.length;i++)
              ( t[i] = new Thread(new Arbeider())).start();
        }
        class Arbeider implements Runnable {
                int i,lokalData1; // dette er lokale data for hver tråd
                public void run() {
                        // denne kalles når en tråd er startet
                }
         } // end indre klasse Arbeider
} // end class Problem
