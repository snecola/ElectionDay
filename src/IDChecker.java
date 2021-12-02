import java.util.concurrent.atomic.AtomicInteger;

public class IDChecker extends Thread{

    public static long time = System.currentTimeMillis();
    int id;


    public IDChecker (int id) {
        this.id = id;
        setName("IDChecker"+id);
    }

    @Override
    public void run () {
        while(!ElectionDay.votersDone.get()){
            if (ElectionDay.voterCounter.get()==ElectionDay.num_voters){
                break;
            }
            if (!ElectionDay.idCheckerLine.isEmpty()) {
                try {
                    Voter v = ElectionDay.idCheckerLine.get(0);
                    //If the voter is already being assisted
                    if (v.beingAssisted.compareAndSet(false,true)){
                        try{
                            ElectionDay.idCheckerLine.remove(0);
                            msg("Checked Voter"+v.id+" ID");
                            ElectionDay.voterCounter.incrementAndGet();
                        } catch (Exception e) {
                            msg("Unable to check Voter"+v.id);
                        } finally {
                            v.waitForKioskHelper.set(true);
                            v.waitForIdCheck.set(false);
                            v.beingAssisted.set(false);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
