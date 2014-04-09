import java.util.*;
//import easyIO.*;
import java.util.concurrent.*;

/** Start >java Parallell <ant traader> <ant ganger i loop>
Viser at arrayelementer ikke affiseres av 'manglende synkronisering'*/

class ParaArray{
    int  []tall;              // Sum av at 'antTraader' traader teller opp denne
    CyclicBarrier b ;         // sikrer at alle er ferdige for vi tar tid og sum
    int antTraader, antGanger ; // Etter summering: riktig svar er  antTraader*antGanger

    void utskrift(double tid) {
       System.out.println("Tid "+antGanger+" kall * "+ antTraader+" Traader ="+
                            tid+ " sek,");
        for (int i = 0; i < antTraader; i++) { System.out.println(
           " sum:"+ tall[i] +", tap:"+ (antGanger -tall[i])+" = "+
            ((antGanger - tall[i])*100.0 /antGanger)+"%");
       }
    } // end utskrift

 //  synchronized void okTall(int i){ tall[i]++;}         // 1)
 //    void okTall(int i) { tall[i]++;}                 // 2)

    public static void main (String [] args) {
        if (args.length != 2) {
            System.out.println(" bruk: java <antTraader> <ant ganger oeke>");
        } else {
             int antKjerner = Runtime.getRuntime().availableProcessors();
             System.out.println("Maskinen har "+ antKjerner + " prosessorkjerner.");
             ParaArray p =  new ParaArray();
             p.antTraader = Integer.parseInt(args[0]);
             p.antGanger = Integer.parseInt(args[1]);
             p.utfor();
         }
     } // end main

    void  utfor () {
        tall = new int[antTraader];
        b = new CyclicBarrier((int)antTraader+1); //+1, ogsaa main
        long t = System.nanoTime();       // start klokke
        for (int i = 0; i< antTraader; i++)
          new Thread(new Para(i)).start();

        try{
           // main thread wenter
           b.await();
         } catch (Exception e) {return;}
         double tid = (System.nanoTime()-t)/1000000000.0;
         utskrift(tid);
     } // utfor

     class Para implements Runnable{
           int i;
           Para(int i) { this.i =i;}
           public void run() {
                  for (int j = 0; j< antGanger; j++) {
                      okTall(i);
                   }
                   try {  // wait on all other threads + main
                       b.await();
                   } catch (Exception e) {return;}
            } // end run

       void okTall(int i) { tall[i]++;}              // 3)
  //    synchronized void okTall(int i){ tall[i]++;}  // 4)
     } // end class Para
}// END class Parallell