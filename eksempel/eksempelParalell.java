import java.util.*;
import easyIO.*;
import java.util.concurrent.*;

/** Viser at manglende synkronisering gir synkronisering'*/

public class Parallell{
    int tall;                 // Sum av at 'antTraader' traader teller opp denne
    CyclicBarrier b ;         // sikrer at alle er ferdige naar vi tar tid og sum
    int antTraader, antGanger ,svar; // Etter summering: riktig svar er:antTraader*antGanger

  //synchronized void inkrTall(){ tall++;}      // 1) OK, men treigt
    void inkrTall() { tall++;}                  // 2) Feil

    public static void main (String [] args) {
        if (args.length < 2) {
            System.out.println("bruk >java Parallell <antTraader> <n= antGanger>");
            }else{
                 int antKjerner = Runtime.getRuntime().availableProcessors();
                 System.out.println("Maskinen har "+ antKjerner + " prosessorkjerner.");
                 Parallell p =  new Parallell();
                 p.antTraader = Integer.parseInt(args[0]);
                 p.antGanger = Integer.parseInt(args[1]);
                 p.utfor();
             }
     } // end main

    void utskrift(double tid) {
       svar = antGanger*antTraader;
       System.out.println("Tid "+antGanger+" kall * "+ antTraader+" Traader ="+
                            Format.align(tid,9,1)+ " millisek,");
         System.out.println(" sum:"+ tall +", tap:"+ (svar -tall)+" = "+
           Format.align( ((svar - tall)*100.0 /svar),12,6)+"%");

     } // end utskrift

     void  utfor () {
        b = new CyclicBarrier((int)antTraader+1); //+1, ogsaa main
        long t = System.nanoTime();       // start klokke
        for (int j = 0; j< antTraader; j++)
          new Thread(new Para(j)).start();

        try{
           // main thread wenter
           b.await();
         } catch (Exception e) {return;}
         double tid = (System.nanoTime()-t)/1000000.0;
         utskrift(tid);
     } // utfor

     class Para implements Runnable{
           int ind;
           Para(int iind) { this.ind =ind;}
           public void run() {
                  for (int j = 0; j< antGanger; j++) {
                      inkrTall();
                   }
                   try {  // wait on all other threads + main
                       b.await();
                   } catch (Exception e) {return;}
            } // end run

        //   void inkrTall() { tall++;}                     // 3) FEIL
       //    synchronized void inkrTall(){ tall++;}         // 4) FEIL
     } // end class Para
}// END class Parallell
