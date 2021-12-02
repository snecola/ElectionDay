import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ElectionDay {

    // Default Values for run
    static int num_voters = 20;
    static int num_ID_checkers = 3;
    static int num_k = 3;
    static int num_sm = 4;

    // Array of Voter Threads
    static volatile Thread[] voterThreads;

    // Line for the ID Checkers
    static volatile Vector<Voter> idCheckerLine;

    // Array containing the Threads for IDCheckers
    static volatile Thread[] idCheckerThreads;
    // Array containing boolean values if IDChecker is available
    static volatile AtomicBoolean[] idCheckerBusy;
    static volatile AtomicBoolean idCheckerLock = new AtomicBoolean(false);

    // Thread for KioskHelper
    static Thread kioskHelper;
    static volatile AtomicBoolean kioskHelperLock = new AtomicBoolean(false);

    //Boolean for if a Voter is allowed to enter a queue.
    static volatile Vector<AtomicBoolean> enterShortestQueue;

    // Array containing the Queues for each Kiosk
    static volatile Vector<Vector<Voter>> kioskQueues;
    // Line for Kiosk Helper to assist Voters
    static volatile Vector<Voter> kioskHelperLine;

    // Signals all helpers they can exit and leave
    static volatile AtomicBoolean votersDone = new AtomicBoolean(false);


    public static void main (String[] args) {

        // Initialize the IDChecker Line
        idCheckerLine = new Vector<>();

        // Initialize the Kiosk queues
        kioskHelperLine = new Vector<>();
        kioskQueues = new Vector<>();
        for (int i=0;i<num_k;i++){
            kioskQueues.add(new Vector<Voter>());
        }




        // Initialize the array of IDChecker Threads
        idCheckerThreads = new Thread[num_ID_checkers];
        // Initialize the busy state of IDCheckers
        idCheckerBusy = new AtomicBoolean[num_ID_checkers];
//        for (AtomicBoolean atomicBoolean : idCheckerBusy) {
//            atomicBoolean.set(false);
//        }
        // Create the IDCheckers, start their threads
        for (int i=0;i<num_ID_checkers;i++){
            idCheckerThreads[i]=new IDChecker(i);
            idCheckerThreads[i].start();
        }

        // Initialize and Start the KioskHelper
        kioskHelper = new KioskHelper(0);
        kioskHelper.start();

        // Initialize the array of Voter Threads
        voterThreads= new Thread[num_voters];
        // Create the Voters, start their threads
        for (int i=0; i<num_voters;i++){
            voterThreads[i]= new Voter(i);
            voterThreads[i].start();
        }


    }
}
