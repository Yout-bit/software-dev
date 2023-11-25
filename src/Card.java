public class Card implements Comparable<Card> {

    private int _value;

    public Card(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }

    @Override
    public int compareTo(Card otherCard) {
        return Integer.compare(this.getValue(), otherCard.getValue());
    }

    public String toString() {
        return String.valueOf(_value);
    }
}