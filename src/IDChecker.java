public class IDChecker extends Thread{

    public static long time = System.currentTimeMillis();
    int id;


    public IDChecker (int id) {
        this.id = id;
        setName("IDChecker"+id);
    }

    @Override
    public void run () {
        while(true){
            if (ElectionDay.idCheckerLine.isEmpty()) {
//                Voter v=null;
//                try {
//                    v = ElectionDay.idCheckerLine.get(0);
//                } catch (Exception e) {}
//                //If the voter is already being assisted
//                if (v.beingAssisted.compareAndSet(false,true)){
//                    try{
//                        ElectionDay.idCheckerLine.remove(0);
//                        msg("Checked Voter"+v.id+" ID");
//                    } catch (Exception e) {
//                        msg("Unable to check Voter"+v.id);
//                    } finally {
//                        v.waitForKioskHelper.set(true);
//                        v.waitForIdCheck.set(false);
//                        v.beingAssisted.set(false);
//                    }
//                }
            } else {
                Voter v=null;
                try {
                    v = ElectionDay.idCheckerLine.get(0);
                } catch (Exception e) {
                    continue;
                }
                //If the voter is already being assisted
                if (v.beingAssisted.compareAndSet(false,true)){
                    try{
                        ElectionDay.idCheckerLine.remove(0);
                        msg("Checked Voter"+v.id+" ID");
                    } catch (Exception e) {
                        msg("Unable to check Voter"+v.id);
                    } finally {
                        v.waitForKioskHelper.set(true);
                        v.waitForIdCheck.set(false);
                        v.beingAssisted.set(false);
                    }
                }
            }
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                msg("Interupted IDChecker Sleep");
//            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
