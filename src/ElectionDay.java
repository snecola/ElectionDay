import java.util.concurrent.BlockingQueue;

public class ElectionDay {

    // Default Values for run
    static int num_voters = 20;
    static int num_ID_checkers = 3;
    static int num_k = 3;
    static int num_sm = 4;

    // Array of Voter Threads
    static Thread[] voterThreads;

    // Line for the ID Checkers
    static volatile BlockingQueue<Voter> idCheckerLine;

    // Array containing the Threads for IDCheckers
    static Thread[] idCheckerThreads;

    // Thread for KioskHelper
    static Thread kioskHelper;

    // Array containing the Queues for each Kiosk
    static volatile BlockingQueue<Voter>[] kioskQueues;

    //

    public static void main (String[] args) {
        // Initialize the array of IDChecker Threads
        idCheckerThreads = new Thread[num_ID_checkers];
        for (int i=0;i<num_ID_checkers;i++){
            idCheckerThreads[i]=new IDChecker(i);
            idCheckerThreads[i].start();
        }

        // Initialize the array of Voter Threads
        voterThreads= new Thread[num_voters];
        // Create the Voters, start their threads
        for (int i=0; i<num_voters;i++){
            voterThreads[i]= new Voter(i);
            voterThreads[i].start();
        }


    }
}
