import java.util.ArrayList;
import java.util.List;

public class Player extends Thread {
    
    private Hand _hand = new Hand();
    private Deck<Card> _leftDeck;
    private Deck<Card> _rightDeck;
    private int _preferredCardValue;
    private volatile int _winner = -1;
    private StringBuilderPlus _log = new StringBuilderPlus();
    
    public Player(Deck<Card> leftDeck, Deck<Card> rightDeck, int preferredCardValue) {
        _leftDeck = leftDeck;
        _rightDeck = rightDeck;
        _preferredCardValue = preferredCardValue;
    }

    public void giveCard(Card card) {
        _hand.add(card);
    }

    public Hand getHand() {
        return _hand;
    }

    public int getPreferredCard(){
        return _preferredCardValue;
    }

    public Deck<Card> get_leftDeck () {
        return _leftDeck;
    }

    public Deck<Card> get_rightDeck () {
        return _rightDeck;
    }

    // This will be the loop that is tun for all players simultaneously (threaded) until the someone wins
    public void run() {
        _log =  new StringBuilderPlus();
        _log.appendLine("player " +  _preferredCardValue + " initial hand " + _hand.toString());
        
        //Main while loop, check game over value, and if deck that it pulls from is of size 4 or greater, has a turn else wait
        while (_winner == -1) {
            if (_leftDeck.size() < 4) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                playTurn();
            }
        }
        
        //End of the game
        if (_winner != _preferredCardValue) {
            _log.appendLine("player " + _winner + " has informed player " + _preferredCardValue + " that player " + _winner + " has won");
        } else { 
            _log.appendLine("player " + _preferredCardValue + " wins");
        }
        _log.appendLine("player " +  _preferredCardValue + " exits");
        _log.appendLine("player " +  _preferredCardValue + " final hand: " + _hand.toString());
        _log.saveToFile("player" + _preferredCardValue + "_output.txt");
    }

    /*
     * Checks if the player has a winning hand.
     * Then pulls a card from the players left deck and adds it to their hand.
     * Selects a card from the 5 in their hand to play and removes it from the hand 
     * and adds it to the right deck.
     */
    public void playTurn() {
        if (_hand.allEqual()) {
            if (_winner == -1) {
                _winner = _preferredCardValue;
            }
            return;
        }

        _rightDeck.add( _hand.delete(_hand.add(_leftDeck.remove()).selectCard()));
        _log.appendLine("player " + _preferredCardValue + " current hand is " + _hand);


    }

    public int getWinner() {
        return _winner;
    }

    public void setWinner(int winner) {
        _winner = winner;
    }

    public boolean getWon(){
        return _winner == _preferredCardValue;
    }

    class Hand {
        private ArrayList<Card> _cards = new ArrayList<>();

        public String toString() {
            String str= "";
            for (Card card : _cards) {
                str += card + " ";
            }
            return str;
        }

        public ArrayList<Card> getHandCards() {
            return _cards;
        }

        public Card delete(Card card) {
            return remove(_cards.indexOf(card));
        }

        public Card remove(int index) {
            Card card = _cards.remove(index);
            _log.appendLine("player " + _preferredCardValue + " discards " + card.getValue() + " to deck " + _rightDeck._name);
            return card;
        }

        public Hand add(Card card) {
            _log.appendLine("player " + _preferredCardValue + " draws " + card.getValue() + " from deck " + _leftDeck._name);
            _cards.add(card);
            return this;
        }
        
        public boolean allEqual() {
            for (Card card : _cards) {
                if (card.getValue() != _cards.get(0).getValue()) {
                    return false;
                }
            }
            return true;
        }

        /*
         * Chooses a card to play
         */
        public Card selectCard() {
            List<Card> selectableCards = new ArrayList<>();
            for (Card card : _cards) {
                if (card.getValue() != _preferredCardValue) {
                    selectableCards.add(card);
                }
            }
            return selectableCards.get( (int) Math.floor( Math.random() * selectableCards.size() ) ); 
        }
        
    }
}