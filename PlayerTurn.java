import java.util.List;
public class PlayerTurn extends Thread {
    private volatile boolean done = false;
    private Player p;
    private List<Integer> hand;
	public PlayerTurn (){
        hand = p.getHand();
    }
	@Override
	public void run (){
		while (!done){
            while (p.handSize() < 10) {
                hand.add(p.getBlackBag().pick());
            }
 			//if(hand.sum() => 100) {
            //    stopThread();
            //}
 			
 	 	}
    }
	// Method to stop the threads running.
 	public void stopThread() {
 		System.out.println(this.getName() + " : flag is being set......") ;
 		this.done = true;
 	}
}
