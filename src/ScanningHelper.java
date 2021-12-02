import java.util.concurrent.atomic.AtomicBoolean;

public class ScanningHelper extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    public ScanningHelper(int id) {
        this.id = id;
        setName("ScanningHelper"+id);
    }

    @Override
    public void run () {
        while(true){

        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
