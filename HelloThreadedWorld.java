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
    System.out.println("Hello Threaded World.");
    System.out.println("Number of cores on your system: " + quantityCores);
    for (int i = 0; i < quantityCores; i++) {
        System.out.println("Kicking off thread " + (i + 1));
        new Thread(new Para(i + 1)).start();
    }
}
} // End HelloThreadedWorld


class Para implements Runnable {
static int numberOfThreads = 0;
private int ind;
Para(int ind) {
    this.ind = ind;
}
/**
 * New threads runable part.
 * (a)This method must print out it's thread number.
 * (b)This method must, in a try{..}catch block
 *      wait 1000ms, print out it's thread number and how long it waited
 *      (remember synchronize).
 */
synchronized public void run() {
    System.out.println("Traad nr: " + ind + " sier hei!");
    try { 
        wait(1000);
        System.out.println("Traad nr: " + ind + " sier hei etter å ha ventet ettsekund.");
    } catch (Exception e) {
        System.out.println(e.toString());
        return;}
}
}
