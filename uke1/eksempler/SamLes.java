import java.util.concurrent.*;


/** Start >java SamLes <ant traader>
Viser at lesing av elementer skrevet av andre går galt'*/

public class SamLes{
    int  a=0,b=0;               // Felles variable a,b
    CyclicBarrier sync, vent ;     // sikrer at begge starter 'samtidig' er ferdige før utskrift
    int antTraader =2, antGanger ;
    SkrivA aObj;
    SkrivB bObj;

    void utskrift() {
       int num =20,j=0, max=4, aIndex,bIndex =0;

       System.out.println("               SkrivA                            SkrivB");

       for (int i = 0; i < antGanger; i++) {
               // skip uinteressante tilfeller
               while (j < antGanger && (
                     aObj.mA[j] == 0 || aObj.mB[j] == 0||
                     bObj.mA[j] == 0 || bObj.mB[j] == 0)) j++;

               aIndex = j;
               while (bIndex < antGanger &&
                       bObj.mA[bIndex] != aObj.mA[aIndex]) bIndex++;

                   // skriv ut 10 interessante tilfeller
                   for (int k = 0; k < 10 && k+aIndex < antGanger && k+bIndex < antGanger; k++) {
                       System.out.println(
                         "a.mA[" + (k+aIndex) + "]= "+ aObj.mA[k+aIndex] +
                         "  a.mB["+  (k+aIndex)  +  "]= "+aObj.mB[k+aIndex] + "  | "+
                         " b.mA[" + (k+bIndex) + "]= "+ bObj.mA[k+bIndex] +
                         "  b.mB["+  (k+bIndex)  + "]= "+bObj.mB[k+bIndex] );

                   } // end k

               System.out.println("--");
               max--;
               if (max < 0 ) return;
               j = aIndex +10;
       } // end i
    } // end utskrift


    public static void main (String [] args) {
        if (args.length != 1) {
            System.out.println(" bruk: java <ant ganger oeke> ");
          } else {
             int antKjerner = Runtime.getRuntime().availableProcessors();
             System.out.println("Maskinen har "+ antKjerner + " prosessorkjerner.\n");
             SamLes p =  new SamLes();
             p.antGanger  = Integer.parseInt(args[0]);
             p.utfor();
         }
    } // end main

    void  utfor () {
        vent = new CyclicBarrier((int)antTraader+1); // vent mellom algoritmer og tidtaging
        sync = new CyclicBarrier((int)antTraader);

        (aObj =  new SkrivA()).start();
        (bObj =  new SkrivB()).start();

         try{
           // main thread wenter p� aObj og bObj ferdige
           vent.await();
         } catch (Exception e) {return;}

          utskrift();

     } // utfor

     class SkrivA extends Thread{
           int [] mB = new int[antGanger],
                  mA = new int[antGanger];
           public void run() {
                  try {  // wait on the other thread
                      sync.await();
                  } catch (Exception e) {return;}
                  for (int j = 0; j<antGanger; j++) {
                      a++;
                      mA[j] =a;
                      mB[j] =b;
                   }
                   try {  // wait on all other threads + main
                       vent.await();
                   } catch (Exception e) {return;}
            } // end run A
     } // end class Para

      class SkrivB extends Thread{
           int [] mB = new int[antGanger],
                  mA = new int[antGanger];
           public void run() {
                   try {  // wait on the other thread
                        sync.await();
                   } catch (Exception e) {return;}
                  for (int j = 0; j<antGanger; j++) {
                      b++;
                      mA[j] =a;
                      mB[j] =b;
                   }
                   try {  // wait on all other threads + main
                       vent.await();
                   } catch (Exception e) {return;}
            } // end run B
     } // end class Para


}// END class Parallell
