import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class Voter extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    volatile AtomicBoolean waitForIdCheck = new AtomicBoolean(true);
    volatile AtomicBoolean waitForKioskHelper = new AtomicBoolean(false);
    volatile AtomicBoolean beingAssisted = new AtomicBoolean(false);
    volatile AtomicBoolean completedBallot = new AtomicBoolean(false);

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
        if(ElectionDay.idCheckerLock.compareAndSet(false, true)){
            try{
                ElectionDay.idCheckerLine.add(this);
                //msg("Added voter to IDChecker line");
            } catch (Exception e) {
                msg("Unable to add to IDChecker line");
            } finally {
                ElectionDay.idCheckerLock.set(false);
            }
        }
        //Busy wait for IDChecker
        while(waitForIdCheck.get()) {}
        msg("Passed IDChecker");
        //Find the shortest kiosk queue
        if(ElectionDay.kioskHelperLock.compareAndSet(false, true)){
            try{
                ElectionDay.kioskHelperLine.add(this);
                msg("Added voter to KioskHelper line");
            } catch (Exception e) {
                msg("Unable to add to KioskHelper line");
            } finally {
                ElectionDay.kioskHelperLock.set(false);
            }
        }
        //Busy wait for the Kiosk Helper to put the Voter in the shortest kiosk line
        while(waitForKioskHelper.get()){}

        // Sleep to complete ballot
        try {
            sleep(Math.abs(random.nextInt(4000)));
        } catch (InterruptedException e) {
            msg("Interrupted while completing ballot");
        }

        while(!completedBallot.get()){}

    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

}
