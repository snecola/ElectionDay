public class KioskHelper extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    public KioskHelper (int id) {
        this.id = id;
        setName("KioskHelper"+id);
    }

    @Override
    public void run () {

    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
