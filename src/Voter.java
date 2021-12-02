import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Voter extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    // Thread specific booleans to ensure order in execution.
    volatile AtomicBoolean waitForIdCheck = new AtomicBoolean(true);
    volatile AtomicBoolean waitForKioskHelper = new AtomicBoolean(false);
    volatile AtomicBoolean waitForScanningMachine = new AtomicBoolean(false);
    volatile AtomicBoolean beingAssisted = new AtomicBoolean(false);
    volatile AtomicBoolean useKiosk = new AtomicBoolean(false);
    volatile AtomicBoolean readyToLeave = new AtomicBoolean(false);

    // Meant to join with this voter to leave
    volatile Voter leaveWithVoter=null;

    public Voter(int id) {
        this.id = id;
        setName("Voter"+id);
    }

    @Override
    public void run () {
        // Random seed for each Voter
        Random random = new Random(System.currentTimeMillis()+id+id);
        msg("Voter thread created!");
        // Voters Arrive at the Voting Place
        try {
            sleep(Math.abs(random.nextInt(4000)));
        } catch (InterruptedException e) {
            msg("Voter took too long to arrive");
        }
        // Voters wait in IDCheckLine
        while(true){
            if(ElectionDay.idCheckerLock.compareAndSet(false, true)){
                try{
                    ElectionDay.idCheckerLine.add(this);
                    msg("Added voter to IDChecker line");
                    break;
                } catch (Exception e) {
                    msg("Unable to add to IDChecker line");
                } finally {
                    ElectionDay.idCheckerLock.set(false);
                }
            }
        }
        //Busy wait for IDChecker
        while(waitForIdCheck.get()) {}
        msg("Passed IDChecker");

        // Voters pick shortest kiosk queue
        if (ElectionDay.kioskHelperLock.compareAndSet(false, true)) {
            try {
                //Find the shortest kiosk queue
                ElectionDay.kioskQueues.get(ElectionDay.shortestQueue.get()).add(this);
                msg("Voter" + id + " joined KioskQueue"+ElectionDay.shortestQueue.get());
                ElectionDay.shortestQueue.set((ElectionDay.shortestQueue.get()+1)%ElectionDay.num_k);
            } catch (Exception e) {
                e.printStackTrace();
                msg("Unable to add to Queue");
            } finally {
                ElectionDay.kioskHelperLock.set(false);
                waitForKioskHelper.set(true);
            }
        }

        // Wait for your turn using the Kiosk
        if (waitForKioskHelper.get()) {
            try {
                sleep(16000);
                msg("This voter is waiting for their turn to use the Kiosk");
            } catch (InterruptedException e) {
                msg("This voter's turn to complete their ballot");
            }
        }
        // Use the kiosk, complete the ballot
        if (useKiosk.get()){
            try {
                sleep(Math.abs(random.nextInt(5000))+3000);
                msg("This voter has completed their ballot");
            } catch (InterruptedException e) {
                msg("This voter's turn to complete their ballot");
            }
        }

        // FINALLY THE LAST STEP
        // The voters will rush to the room with scanning machines

        this.setPriority(MAX_PRIORITY);
        try {
            sleep(Math.abs(random.nextInt(5000)));
            msg("Rushes to Scanning Machine");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setPriority(NORM_PRIORITY);

        // Enter the Scanning Machine Helper line
        if(ElectionDay.scanningHelperLock.compareAndSet(false,true)){
            try{
                ElectionDay.scanningHelperLine.add(this);
                msg("Entered the ScanningHelperLine");
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                ElectionDay.scanningHelperLock.set(false);
                waitForScanningMachine.set(true);
            }
        }
        // Wait for scanning machine to let these voters in
        while(waitForScanningMachine.get()){}
        // Slow Down
        this.yield();
        this.yield();
        // Scan ballot
        try {
            sleep(Math.abs(random.nextInt(5000)));
            msg("Scans Ballot");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Wait on group of 4 to leave
//        while(!readyToLeave.get()){}
        msg("Leaving with a very patriotic feeling, maybe tomorrow will be a better day.");

        if (ElectionDay.exitedThreads.incrementAndGet()==ElectionDay.num_voters){
            ElectionDay.votersDone.set(true);
        }
    }

    // Function to join threads before they leave
    // Couldn't figure it out :(
    public boolean joinGroup (Voter v) throws InterruptedException {
        if (v.isAlive()) {
            msg("Joined Voter"+ v.id);
            v.join();
            return true;
        } else return false;
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }


}
