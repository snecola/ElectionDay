import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScanningHelper extends Thread{

    public static long time = System.currentTimeMillis();
    int id;

    public static volatile Vector<Voter> groupVoters;

    public ScanningHelper(int id) {
        this.id = id;
        setName("ScanningHelper"+id);
        groupVoters = new Vector<>();
    }

//    @Override
//    public void run () {
//        int scanningRoomCounter=0;
//        while(true){
//            if(!ElectionDay.scanningHelperLine.isEmpty() && scanningRoomCounter<ElectionDay.num_sm){
//                groupVoters.add(ElectionDay.scanningHelperLine.get(0));
//                msg("Voter"+ElectionDay.scanningHelperLine.get(0).id +" Entered the room");
//                ElectionDay.scanningHelperLine.remove(0);
//                scanningRoomCounter++;
//            } else if (scanningRoomCounter==ElectionDay.num_sm-1){
//                scanningRoomCounter=0;
//                Voter highestIdVoter=groupVoters.get(0);
//                int highestId=0;
//                for (Voter v : groupVoters) {
//                    if (v.id > highestId) {
//                        highestId=v.id;
//                        highestIdVoter=v;
//                    }
//                }
//                msg("Highest Id voter for this group: Voter" +highestId);
//                for (Voter v : groupVoters) {
//                    if (!v.equals(highestIdVoter)){
//                        v.waitForScanningMachine.set(false);
//                        continue;
//                    }
//                    v.leaveWithVoter = highestIdVoter;
//                    v.waitForScanningMachine.set(false);
//                }
//                groupVoters.removeAllElements();
//            }
//        }
//    }

    //Second attempt
//    @Override
//    public void run() {
//        while (true) {
//            while(groupVoters.size()<4) {
//                if(!ElectionDay.scanningHelperLine.isEmpty()){
//                    try {
//                        Voter v = ElectionDay.scanningHelperLine.get(0);
//                        msg("Voter"+v.id+ " entered the scanning room.");
//                        groupVoters.add(v);
//                        ElectionDay.scanningHelperLine.remove(0);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            Voter highestIdVoter=groupVoters.get(0);
//            int highestId=0;
//            for (Voter v : groupVoters) {
//                if (v.id > highestId) {
//                    highestId=v.id;
//                    highestIdVoter=v;
//                }
//            }
//            msg("Group Leader with HighestId is Voter"+highestIdVoter.id);
//            for (Voter v : groupVoters) {
//                if (!v.equals(highestIdVoter)){
//                    v.waitForScanningMachine.set(false);
//                    continue;
//                }
//                v.leaveWithVoter = highestIdVoter;
//                v.waitForScanningMachine.set(false);
//            }
//            groupVoters.removeAllElements();
//        }
//    }

    @Override
    public void run() {
        // While voters are still here
        while(!ElectionDay.votersDone.get()){
            // Look for Voters waiting to enter the scanning room
            for (Voter v : ElectionDay.voterThreads) {
                // If the scanning room is full break this loop
                if (ElectionDay.voterCounter.get()==ElectionDay.num_sm-1){
                    for (Voter voter : groupVoters) {
                        voter.readyToLeave.set(true);
                    }
                    groupVoters.removeAllElements();
                    ElectionDay.voterCounter.set(0);
                    break;
                }
                // If the voter is waiting to enter the scanning room, add them to the group
                // Let them in, increment the group by 1.
                if (v.waitForScanningMachine.get()){
                    v.waitForScanningMachine.set(false);
                    v.readyToLeave.set(true);
                    groupVoters.add(v);
                    ElectionDay.voterCounter.incrementAndGet();
                }
            }
        }
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
