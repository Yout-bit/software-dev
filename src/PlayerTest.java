import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PlayerTest {

    Player player;
    private void addLeft(Deck<Card> _leftdeck) {
        _leftdeck.add(new Card(1));
        _leftdeck.add(new Card(1));
        _leftdeck.add(new Card(1));
        _leftdeck.add(new Card(1));
        _leftdeck.add(new Card(1));
    }
    @BeforeEach
    public void setup() {
        Deck<Card> _leftdeck = new Deck<>("1");
        Deck<Card> _rightdeck = new Deck<>("2");
        addLeft(_leftdeck);

        player = new Player(_leftdeck, _rightdeck, 1);

    }

    @AfterEach
    public void tearDown() {
        Deck<Card> _leftdeck = new Deck<>("1");
        Deck<Card> _rightdeck = new Deck<>("2");
        addLeft(_leftdeck);

        player = new Player(_leftdeck, _rightdeck, 1);
    }


    @Test
    public void testRun() {
        addLeft(player.get_leftDeck());
        addLeft(player.get_leftDeck());
        player.getHand().add(new Card(5));
        player.getHand().add(new Card(6));
        // Perform a turn
        player.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Handle the exception (e.g., log it or rethrow it)
            e.printStackTrace();
            Thread.currentThread().interrupt();  // Restore interrupted status
        }

        assertEquals(1, player.getWinner());


    }

    @Test
    public void testHandWin() {

        Player.Hand hand = player.getHand();

        Card card = new Card(1);

        hand.add(card);
        hand.add(card);
        hand.add(card);
        hand.add(card);

        assert(hand.allEqual());
    }

    @Test
    public void testPlayTurn () {
        player.getHand().add(new Card(5));
        player.getHand().add(new Card(6));
        player.getHand().add(new Card(7));
        player.getHand().add(new Card(8));

        player.playTurn();

        assert(player.get_rightDeck().size() == 1 && player.get_leftDeck().size() == 4 && player.getHand().getHandCards().size() == 4);


    }
}

