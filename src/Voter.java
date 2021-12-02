import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Voter extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    volatile AtomicBoolean waitForIdCheck = new AtomicBoolean(true);

    public Voter(int id) {
        this.id = id;
        setName("Voter"+id);
    }

    @Override
    public void run () {
        // Voters Arrive at the Voting Place
        Random random = new Random(System.currentTimeMillis()+id);
        try {
            sleep(Math.abs(random.nextInt(4000)));
        } catch (InterruptedException e) {
            msg("Voter took too long to arrive");
            return;
        }

        ElectionDay.idCheckerLine.add(this);
        msg("Added voter to IDChecker line");
        //Busy wait for IDChecker
        while(waitForIdCheck.get()) {
        }
        msg("Passed IDChecker");

    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

}
