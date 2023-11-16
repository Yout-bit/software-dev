import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    
    Hand _hand = new Hand();
    Deck _leftDeck;
    Deck _rightDeck;
    int _preferredCardValue;
    boolean _gameOver = false;
    boolean _won = false;
    int _winner = -1;
    StringBuilderPlus _log;
    
    public Player(Deck leftDeck, Deck rightDeck, int preferredCardValue) {
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

    // This will be the loop that is tun for all players simultaneously (threaded) until the someone winns
    public void play() {
        _log.appendLine("player " +  _preferredCardValue + " initial hand " + _hand.toString());

        //Main while loop, check game over value, and if both
        while (!_gameOver) {
            while (_leftDeck.size() != 4 && _rightDeck.size() != 4) {
                //Thread.sleep(1000)
            }
            playTurn();
        }
        
        //End of the game
        if (!_won) {
            _log.appendLine("player " + _winner + " has informed player " + _preferredCardValue + " that player " + _winner + " has won");
        }
        _log.appendLine("player " +  _preferredCardValue + " exits");
        _log.appendLine("player " +  _preferredCardValue + " final hand: " + _hand.toString());
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
            _won = true;
            gameOver();
            _log.appendLine("player " + _preferredCardValue + " wins");
            return;
        }
        _rightDeck.add( _hand.delete(_hand.add(_leftDeck.remove()).selectCard())); 
        _hand.incrementAge();
    }

    public void gameOver() {
        _gameOver = true;
    }

    public boolean getWon() {
        return _won;
    }


    private class Hand {
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
            return card;
        }

        public Hand add(Card card) {
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

