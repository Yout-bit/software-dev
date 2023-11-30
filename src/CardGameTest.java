import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    CardGame game;
    @BeforeEach
    public void setup() {
        game = new CardGame();
        for (int i = 0; i < 4; i++) {
            CardGame._decks.add(new Deck<>(i + 1));
        }
        for (int i = 0; i < 4; i++) {
            CardGame._players.add(new Player( CardGame._decks.get(i), CardGame._decks.get( Math.floorMod(i - 1, 4 ) ), i + 1 ));
        }
    }

    @AfterEach
    public void teardown() {
        CardGame._decks = new ArrayList<>();
        CardGame._players = new ArrayList<>();
        CardGame._pack = new Pack();
    }

    @Test
    public void testRealPack() {
        boolean validResult = CardGame.readPackFile("testpack.txt");

        assertTrue(validResult, "Valid file should return true");
    }

    @Test
    public void fakePack() {
        boolean invalidResult = CardGame.readPackFile("fakefile.txt");

        assertFalse(invalidResult, "Invalid file should return false");
    }

    @Test
    public void testShortPack() {
        CardGame.readPackFile("shortpack.txt");
        assertNull(CardGame._pack);
    }

    @Test
    public void testUnwinnablePack() {
        CardGame.readPackFile("unWinnablePack.txt");
        assertNull(CardGame._pack);
    }

    @Test
    public void testDeal() {
        CardGame.readPackFile("testpack.txt");
        CardGame.deal();

        for (int i = 0; i < 4; i++) {
            Deck<Card> tempdeck = CardGame._decks.get(i);
            Player player = CardGame._players.get(i);

            assert tempdeck.size() == 4 : "Deck size is not 4 for deck " + i;

            ArrayList<Card> handCards = player.getHand().getHandCards();
            assert handCards.size() == 4 : "Hand size is not 4 for player " + i;

            }
        }

    }

