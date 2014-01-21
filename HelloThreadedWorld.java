// wait må alltid være tilknyttet synchronized
/* int antKjerner = Runtime.getRuntime().availableProcessors(); */
 /*
  * SYNCHRONZIED(THIS) {
  *    try catch
  *        :
  *        :
  *        }
  */
/**
 * This class generates as many threads k as it is cores on the computer you are
 * sitting on (ie as many cores + 1(the main core).
 * 
 * Each thread must have a local variable 'ind' (only accessible for the current
 * thread).
 */
class HelloThreadedWorld {
public int quantityCores = Runtime.getRuntime().availableProcessors();

/**
 * This programs main method (starts here.)
 */
public static void main(String [] args) {
    new HelloThreadedWorld();
}

/**
 * Constructor for the HelloThreadedWorld class.
 */
HelloThreadedWorld() {
    System.out.println(quantityCores);
}
} // End HelloThreadedWorld


class Para implements Runnable {
static int numberOfThreads = 0;
private int ind;
/**
 * New threads runable part.
 * (a)This method must print out it's thread number.
 * (b)This method must, in a try{..}catch block
 *      wait 1000ms, print out it's thread number and how long it waited
 *      (remember synchronize).
 */
public void run() {
    System.out.println("Traad nr:" + ind+"sier hei");
    try { wait(1000);
    } catch (Exception e) {return;}
}
}
