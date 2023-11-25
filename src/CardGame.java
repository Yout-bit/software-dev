import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardGame {
    static Pack _pack;
    static List<Deck<Card>> _decks = new ArrayList<>();
    static List<Player> _players = new ArrayList<>();

    public static void main(String[] args) {
        playGame();
    }

    /*
     * Creates the players and decks, and loads the pack from the file.
     * Deals the cards to the players and starts play
     */
    public static void playGame() {
        setup();
        play();
    }

    private static void setup() {
        Scanner in = new Scanner(System.in);
        getPlayers(in);
        getPack(in);
        in.close();
        deal();
    }
    
    /*
    * Asks the user for the number of players and creates the players and decks accordingly 
     */
    private static void getPlayers(Scanner in) {
        System.out.print("Enter number of players   ");
        int playerCount = in.nextInt();

        for (int i = 0; i < playerCount; i++) { 
            _decks.add(new Deck<Card>(i+1));
        }
        for (int i = 0; i < playerCount; i++) { 
            _players.add(new Player( _decks.get(i), _decks.get( Math.floorMod(i - 1, 4 ) ), i + 1 ));
        }
    }

    /*
     * Asks the user for a filename and the pack is invalid, keeps asking.
     */
    private static void getPack(Scanner in) {
        String filename = "";
        do {
            System.out.print("Enter card pack name   ");
            filename = in.next();
        } while (!readPackFile(filename));
    }

    // Might need to be changed to properly find the file, but when complied should work with just `new File(filename)` 
    /*
     * Load a file from the given filename and read it into a string array.
     * Then parses each element as an int, and checks the pack is valid
     */
    private static boolean readPackFile(String filename) {
        try {
            File file = new File("src\\" + filename);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();
            String[] valueStr = new String(bytes).trim().split("\\s+");
            int[] intList = new int[valueStr.length];
            for (int i = 0; i < valueStr.length; i++) {
                intList[i] = Integer.parseInt(valueStr[i]);
            }
            _pack = checkPack(intList);
            return true;
        } catch (FileNotFoundException e ) {
            System.out.println("File could not be found");
        } catch (NumberFormatException e ) {
            System.out.println("File data in invalid format");
        } catch (IOException e ) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InvalidPackError e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /*
     * Checks the pack if large enough and has a winning hand
     * Trims the pack to 8 times the number of players, trims from the back of the pack
     */
    private static Pack checkPack(int[] listPack) throws InvalidPackError{
        if (listPack.length < 8 * _players.size()) {
            throw new InvalidPackError("Pack too small for number of players");
        }

        if (modeCount(listPack) < 4) {
            throw new InvalidPackError("Pack not winnable");
        }
        Pack pack = new Pack();
        for (int i = 0; i < 8 * _players.size() ; i++) {
            pack.add(listPack[i]);
        }
        return pack;
    }
    
    /*
     * Shuffles the pack and deals 4 cards to each player and deck
     * Supposes the pack is of the correct size.
     */
    public static void deal() {
        _pack.shuffle();
        for (int i = 0; i < 4 ; i ++) {
            for (Player player : _players) {
                player.giveCard(_pack.remove());
            }
            for (Deck<Card> deck : _decks) {
                deck.add(_pack.remove());
            }
        }
    }

    /*
     * Starts all the player threads then constantly checks whether each player has won.
     * If a player has won, it broadcasts that information to all other players.
     */
    public static void play() {
        for (Player player : _players) {
            player.start();
        }
        int winner = -1;
        while (winner == -1) {
            winner = checkWinner();
        }

        System.out.println("player " + winner + " has won");

        for (Player player : _players) {
            player.setWinner(winner);
        }
        logDecks();
    }
    
    private static int checkWinner() {
        for (int i = 0 ; i < _players.size() ; i++) {
            if (_players.get(i).getWon()) {
                    return i + 1;
               }
            }
        return -1;
    }

    /*
    * Returns the mode count of the given array. 
    */
    public static int modeCount(int[] array) {
        int maxCount = 0;
        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            int count = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[j] == value) count++;
                if (count > maxCount) {
                    maxCount = count;
                    }
                }
        }
        return maxCount;
    }

    /*
     * Provides log txt files for each of the decks
     */
    public static void logDecks() {
        for (int i = 0; i < _decks.size(); i ++) {
            try {
                File file = new File("deck" + i + "_output.txt");
                if (!file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                FileWriter fWriter = new FileWriter(file);
                fWriter.write("deck" + i + " contents: " + _decks.get(i).toString());
                fWriter.close();    
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }
}