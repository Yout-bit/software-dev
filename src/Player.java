import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Thread {
    
    Hand _hand = new Hand();
    Deck<Card> _leftDeck;
    Deck<Card> _rightDeck;
    int _preferredCardValue;
    volatile int _winner = -1;
    StringBuilderPlus _log = new StringBuilderPlus();
    
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
     * Increments the age of all cards in the hand.
     */
    private void playTurn() {
        
        if (_hand.allEqual()) {
            if (_winner == -1) {
                _winner = _preferredCardValue;
            }
            return;
        }
        _rightDeck.add( _hand.delete(_hand.add(_leftDeck.remove()).selectCard())); 
        _hand.incrementAge();
        _log.appendLine("player " + _preferredCardValue + " current hand is " + _hand);
    }

    public int getWinner() {
        return _winner;
    }

    public void setWinner(int winner) {
        _winner = winner;
    }

    public boolean getWon(){
        if (_winner == _preferredCardValue) {
            return true;
        } else {
            return false;
        }
    }

    class Hand {
        int PREFERRED_CARD_AGE = 10;   

        private List<Card> _cards = new ArrayList<Card>();
        private Map<Card, Integer> _cardAgeMap = new HashMap<Card, Integer>();

        public String toString() {
            String str= "";
            for (Card card : _cards) {
                str += card + " ";
            }
            return str;
        }

        public Card delete(Card card) {
            return remove(_cards.indexOf(card));
        }

        public Card remove(int index) {
            Card card = _cards.remove(index);
            _cardAgeMap.remove(card);
            _log.appendLine("player " + _preferredCardValue + " discards " + card.getValue() + " to deck " + _rightDeck._name);
            return card;
        }

        public Hand add(Card card) {
            _log.appendLine("player " + _preferredCardValue + " draws " + card.getValue() + " from deck " + _leftDeck._name);
            _cards.add(card);
            _cardAgeMap.put(card, 0);
            return this;
        }
        
        public Hand incrementAge() {
            for (Card card : _cards) {
                _cardAgeMap.put(card, _cardAgeMap.get(card) + 1);
            }
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
         * Chooses a card to play based on the conditions:
         * Selects the first preferred card has been in the players hand for 10 turns or more.
         * Else selects a random cards from the none preferred remaining.
         */
        public Card selectCard() {
            List<Card> selectableCards = new ArrayList<Card>();
            for (Card card : _cards) {
                if (card.getValue() == _preferredCardValue) {
                    if ( _cardAgeMap.get(card) >= PREFERRED_CARD_AGE) {
                        return card;
                    }
                } else {
                    selectableCards.add(card);
                }
            }
            return selectableCards.get( (int) Math.floor( Math.random() * selectableCards.size() ) ); 
        }
        
    }
}