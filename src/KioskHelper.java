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
        while(true){
            if (!ElectionDay.kioskHelperLine.isEmpty()) {
                Voter v = ElectionDay.kioskHelperLine.get(0);
                //If the voter is already being assisted
                if (v.beingAssisted.compareAndSet(false,true)){
                    try{
                        ElectionDay.kioskHelperLine.remove(0);
//                        for (int i=0; i<ElectionDay.num_k;i++) {
//                            if (ElectionDay.kioskQueues.get(shortestQueue).size()> ElectionDay.kioskQueues.get(i).size())
//                                shortestQueue=i;
//                        }
                        ElectionDay.kioskQueues.get(shortestQueue).add(v);
                        //msg("Added Voter"+v.id+" to Kiosk" +shortestQueue);
                        shortestQueue=(shortestQueue+1)%ElectionDay.num_k;
                    } catch (Exception e) {
                        msg("Unable to check Voter"+v.id);
                    } finally {
                        v.waitForKioskHelper.set(false);
                        v.beingAssisted.set(false);
                    }
                }
            }
            try {
                sleep(1000);
                completeBallot();
            } catch (InterruptedException e) {
                msg("Interupted while completing ballot");
            }
        }
    }

    private void completeBallot () {
        for (int i=0;i<ElectionDay.kioskQueues.size();i++) {
            Vector<Voter> v = ElectionDay.kioskQueues.get(i);
            if(!v.isEmpty()) {
                Voter voter = v.get(0);
                v.remove(0);
                msg("Voter"+voter.id+ " done using Kiosk"+i);
                voter.useKiosk.set(true);
            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
