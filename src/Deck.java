import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Deck {

    // BlockingQueue is thread safe
    BlockingQueue<Card> _deck;
    String _name;

    public Deck(Object name) {
        _deck = new LinkedBlockingDeque<>();
        _name = name.toString();
    }

    public void add(Card value) {
       _deck.add(value);
    }

    public Card remove() {
        return _deck.remove();
    }

    public int size() {
        return _deck.size();
    }

    public String toString() {
        String str= "";
        for (Card card : _deck) {
            str += card + " ";
        }
        return str;
    }
}