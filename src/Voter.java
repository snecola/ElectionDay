

public class Voter extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    public Voter(int id) {
        this.id = id;
        setName("Voter"+id);
    }

    @Override
    public void run () {

    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

}
