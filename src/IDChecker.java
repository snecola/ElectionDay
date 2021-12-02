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
            if (!ElectionDay.idCheckerLine.isEmpty()) {
                Voter v = ElectionDay.idCheckerLine.get(0);
                v.waitForIdCheck.set(false);
                ElectionDay.idCheckerLine.remove(0);
                msg("Checked Voter"+v.id+" ID");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                msg("Interupted IDChecker Sleep");
            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
