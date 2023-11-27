import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck<Card> deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck<>("1");
    }

    @AfterEach
    public void tearDown() {
        deck = new Deck<>("1");
    }

    @Test
    public void testAdd() {
        deck.add(new Card(5));
        deck.add(new Card(4));

        assertEquals(2, deck.size());
    }

    @Test
    public void testRemove() {
        Card card1 = new Card(5);
        Card card2 = new Card(4);

        deck.add(card1);
        deck.add(card2);

        assertEquals(card1, deck.remove());
        assertEquals(1, deck.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, deck.size());

        deck.add(new Card(5));
        deck.add(new Card(4));

        assertEquals(2, deck.size());
    }

//    @Test
//    public void testToString() {
//        deck.add("Card1");
//        deck.add("Card2");
//
//        assertEquals("Card1 Card2 ", deck.toString());
//    }
}
