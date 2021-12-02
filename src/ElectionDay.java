import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ElectionDay {

    // Default Values for run
    static int num_voters = 20;
    static int num_ID_checkers = 3;
    static int num_k = 3;
    static int num_sm = 4;

    // Array of Voter Threads
    static volatile Voter[] voterThreads;
    // Counter for exited Threads
    static volatile AtomicInteger exitedThreads = new AtomicInteger(0);

    // Line for the ID Checkers
    static volatile Vector<Voter> idCheckerLine;
    // Array containing the Threads for IDCheckers
    static volatile Thread[] idCheckerThreads;
    // Array containing boolean values if IDChecker is available
    static volatile AtomicBoolean[] idCheckerBusy;
    static volatile AtomicBoolean idCheckerLock = new AtomicBoolean(false);
    static volatile AtomicInteger voterCounter = new AtomicInteger(1);

    // Thread for KioskHelper
    static Thread kioskHelper;
    // Lock to make sure voters enter the kioskHelperLine one by one
    static volatile AtomicBoolean kioskHelperLock = new AtomicBoolean(false);
    // Array containing the Queues for each Kiosk
    static volatile Vector<Vector<Voter>> kioskQueues;
    // Lock to make sure queue sizes are accurate
    static volatile AtomicBoolean shortestQueueLock = new AtomicBoolean(false);
    static volatile AtomicInteger shortestQueue = new AtomicInteger(0);
    // Line for Kiosk Helper to assist Voters in the order they finish checking IDs
    static volatile Vector<Voter> kioskHelperLine;

    // Thread for ScanningHelper
    static Thread scanningHelper;
    // Lock to make sure voters enter the scanningHelperLine one by one
    static volatile AtomicBoolean scanningHelperLock = new AtomicBoolean(false);
    // Line for Scanning Helper to assist Voters in the order they finish using Kiosks
    static volatile Vector<Voter> scanningHelperLine;
    // Current Capacity of Scanning room
    static volatile AtomicInteger scanningRoomCap = new AtomicInteger(0);

    // Signals all helpers they can exit and leave
    static volatile AtomicBoolean votersDone = new AtomicBoolean(false);

    public ElectionDay () {
        // Initialize the array of Voter Threads
        voterThreads= new Voter[num_voters];
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
        scanningHelperLine = new Vector<>();
    }


    public static void main (String[] args) {
        if (args.length>0){
            try{
                num_voters = Integer.parseInt(args[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        ElectionDay E = new ElectionDay();

        // Create the IDCheckers, start their threads
        for (int i=0;i<num_ID_checkers;i++){
            idCheckerThreads[i]=new IDChecker(i);
            idCheckerThreads[i].start();
        }

        // Start the KioskHelper
        kioskHelper = new KioskHelper(0);
        kioskHelper.start();

        // Create the Voters, start their threads
        for (int i=0; i<num_voters;i++){
            voterThreads[i]= new Voter(i);
            voterThreads[i].start();
        }

        scanningHelper = new ScanningHelper(0);
        scanningHelper.start();


    }
}
