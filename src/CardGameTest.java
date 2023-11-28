import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

public class CardGameTest {

    CardGame game;
    @BeforeEach
    public void setup() {
        game = new CardGame();
        for (int i = 0; i < 4; i++) {
            CardGame._decks.add(new Deck<Card>(i+1));
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
    public void testReadPack() {
        CardGame.readPackFile("testpack.txt");

    }
}
