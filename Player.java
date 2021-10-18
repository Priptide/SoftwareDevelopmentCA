import java.util.List;
public class Player {
    private List<Integer> hand;
    private Bag blackBag;
    private Bag whiteBag;

    Player() {}

    public List<Integer> getHand() {
        return hand;
    }
    public void setHand(List<Integer> hand) {
        this.hand = hand;
    }

    public Bag getBlackBag() {
        return blackBag;
    }
    public void setBlackBag(Bag blackBag) {
        this.blackBag = blackBag;
    }
    public Bag getWhiteBag() {
        return whiteBag;
    }
    public void setWhiteBag(Bag whiteBag) {
        this.whiteBag = whiteBag;
    }
}
