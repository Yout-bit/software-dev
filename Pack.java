package Prototype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


// A pack does not need to be thread safe where a deck does
public class Pack extends Deck{    

    public Pack() {
        super("pack");
    }


// NOT CURRENTLY USED
    /*
     * Copies the pack into an ArrayList.
     * May not preserve order.
     */
    public ArrayList<Card> toArray() {
        return new ArrayList<Card>(_deck);
    } 

    public int drainTo(Collection <?super Card> c) {
        return _deck.drainTo(c);
    }

    public void add(int value) {
        _deck.add(new Card(value));
    }

    public void shuffle() {
        ArrayList<Card> list = new ArrayList<>();
        drainTo(list);
        Collections.shuffle(list);
        for (Card card : list) {
            _deck.add(card);
        }
    }
}
