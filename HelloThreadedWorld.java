// wait må alltid være tilknyttet synchronized
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
class HelloThreadedWorld implements Runnable{
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
