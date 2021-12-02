import java.util.Vector;

public class KioskHelper extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

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
                        int shortestQueue=0;
                        for (int i=0; i<ElectionDay.num_k;i++) {
                            if (ElectionDay.kioskQueues.get(shortestQueue).size()> ElectionDay.kioskQueues.get(i).size())
                                shortestQueue=i;
                        }
                        ElectionDay.kioskQueues.get(shortestQueue).add(v);
                        msg("Added Voter"+v.id+" to Kiosk" +shortestQueue);
                    } catch (Exception e) {
                        msg("Unable to check Voter"+v.id);
                    } finally {
                        v.waitForKioskHelper.set(false);
                        v.beingAssisted.set(false);
                    }
                }
            }
        }
    }

//    private int findShortestKioskQueue () {
//        int shortestQueue = 0;
//        for (int i=1; i<ElectionDay.kioskQueues.length;i++) {
//            if (ElectionDay.kioskQueues[shortestQueue].size()>ElectionDay.kioskQueues[i].size())
//                shortestQueue=i;
//        }
//        return shortestQueue;
//    }

    private void completeBallot () {
        for(int i=0;i<ElectionDay.kioskQueues.size();i++){
            if(!ElectionDay.kioskQueues.get(i).isEmpty()) {
                Voter voterAtKiosk = ElectionDay.kioskQueues.get(i).get(0);
                if (voterAtKiosk.beingAssisted.compareAndSet(false,true)){
                    try{
                        ElectionDay.kioskQueues.get(i).remove(0);
                        msg("Allowed Voter"+voterAtKiosk.id+" to use Kiosk"+i);
                    } catch (Exception e) {
                        msg("Voter"+voterAtKiosk.id+" unable to use Kiosk");
                    } finally {
                        voterAtKiosk.completedBallot.set(true);
                        voterAtKiosk.beingAssisted.set(false);
                    }
                }
            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
