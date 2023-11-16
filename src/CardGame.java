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
    static List<Deck> _decks = new ArrayList<>();
    static List<Player> _players = new ArrayList<>();

    public static void main(String[] args) {
        setup();
    }

    /*
     * Asks the user for the number of players and the pack filename.
     * Creates the players and decks, and loads the pack from the file
     * Deals the cards out to the players then the decks 
     */
    public static void setup() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter number of players   ");
        int playerCount = in.nextInt();
        System.out.print("Enter card pack name   ");
        String filename = in.next();
        in.close();
        System.out.println(playerCount);
        System.out.println(filename);

        for (int i = 0; i < playerCount; i++) { 
            _decks.add(new Deck(i+1));
        }
        for (int i = 0; i < playerCount; i++) { 
            _players.add(new Player( _decks.get(i), _decks.get( Math.floorMod(i - 1, 4 ) ), i + 1 ));
        }
        _pack = readPackFile(filename);
        deal();
        for (Player player : _players) {
            System.out.println("player" + player.getPreferredCard() + " has " + player.getHand());
        }
        for (Deck deck : _decks) {
            System.out.println(deck);
        }
    }

    // Might need to be changed to properly find the file, but when complied should work with just `new File(filename)` 
    /*
     * Load a file from the given filename and read it into a string array.
     * Then parses each element as an int, and checks the pack is valid
     */
    private static Pack readPackFile(String filename) {
        try {
            File file = new File("Prototype\\" + filename);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();
            String[] valueStr = new String(bytes).trim().split("\\s+");
            int[] intList = new int[valueStr.length];
            for (int i = 0; i < valueStr.length; i++) {
                intList[i] = Integer.parseInt(valueStr[i]);
            }
            return checkPack(intList);
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
        return null;
    }

    /*
     * Checks the pack if large enough and has a winning hand
     * Trims the pack to 8 times the number of players, trims from the back of the pack
     */
    private static Pack checkPack(int[] listPack) throws InvalidPackError{
        if (listPack.length < 8 * _players.size()) {
            throw new InvalidPackError("Pack too small for number of players");
        }

        if (mode(listPack) < 4) {
            throw new InvalidPackError("Pack has not winnable");
        }
        Pack pack = new Pack();
        for (int i = 0; i < 8 * _players.size() ; i++) {
            pack.add(listPack[i]);
        }
        return pack;
    }

    /*
     * Returns the mode value of the given array. 
     */
    public static int mode(int[] array) {
        int mode = array[0];
        int maxCount = 0;
        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            int count = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[j] == value) count++;
                if (count > maxCount) {
                    mode = value;
                    maxCount = count;
                    }
                }
        }
        if (maxCount > 1) {
            return mode;
        }
        return 0;
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
            for (Deck deck : _decks) {
                deck.add(_pack.remove());
            }
        }
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