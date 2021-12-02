public class IDChecker extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    public IDChecker (int id) {
        this.id = id;
        setName("IDChecker"+id);
    }

    @Override
    public void run () {

    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
