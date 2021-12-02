import java.util.Vector;

public class KioskHelper extends Thread{

    public static long time = System.currentTimeMillis();
    int id;
    int shortestQueue=0;

    public KioskHelper (int id) {
        this.id = id;
        setName("KioskHelper"+id);
    }

    @Override
    public void run () {
        // While voters are still here
        while(!ElectionDay.votersDone.get()){
            // For each Kiosk's line allow the first person on line to use the kiosk
            for (Vector<Voter> v : ElectionDay.kioskQueues) {
                if (!v.isEmpty()){
                    Voter voter = v.get(0);
                    voter.useKiosk.set(true);
                    voter.waitForKioskHelper.set(false);
                    voter.interrupt();
                    msg("Voter" + voter.id + " used Kiosk");
                    v.remove(0);
                }
            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
