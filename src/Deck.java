import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Deck<T> {

    volatile BlockingQueue<T> _deck;
    String _name;

    public Deck(Object name) {
        _deck = new LinkedBlockingQueue<>();
        _name = name.toString();
    }

    public void add(T value) {
       _deck.add(value);
    }

    public T remove() {
        return _deck.remove();
    }

    public int size() {
        return _deck.size();
    }

    public String toString() {
        String str= "";
        for (T card : _deck) {
            str += card + " ";
        }
        return str;
    }
}