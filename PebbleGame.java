
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class PebbleGame {

  static int playerCount;
  static List<Player> players;
  static Map<Bag, Bag> currentBags;
  public static boolean gamePlayable = false;

  public void startMenu() throws Exception{
    System.out.println("Please enter the number of players:");
    Scanner sc = new Scanner(System.in);
    String key = sc.nextLine();
    currentBags = new HashMap<>();

    if (key.equalsIgnoreCase("e")) System.exit(1);
    
    try {
      playerCount = Integer.parseInt(key);
      if (playerCount <= 0) {
        sc.close();
        throw new Exception("You can't play with no one!\n");
      }
    } catch (Exception e) {
      //Invalid so we restart
      sc.close();
      throw new Exception("Invalid Input!\n");
    }

    int bagCount = 0;

    while (bagCount < 2) {
      System.out.println(
        "Please enter location of bag number " +
        (bagCount) +
        " to load"
      );

      String filePath = sc.nextLine();

      File currentFile = new File(filePath);

      if (!currentFile.exists()) System.out.println(
        "The given file doesn't exist, try again!"
      ); else {
        Bag newBlackBag = new Bag(currentFile, BagType.BLACK);
        Bag newWhiteBag = new Bag(BagType.WHITE);


        try {
          newBlackBag.load();
        } catch (Exception e) {
          System.out.println(e.toString());
          break;
        }

        currentBags.put(newBlackBag, newWhiteBag);
        
        bagCount++;
      }
    }
    sc.close();
  }



  /**
   * Method to create a random number accessible by all functions in the class
   */
  static Random random = new Random();

  /**
   * Method to start the game, creating a list of players.
   * Then assigning bags to those players.
   */
  public static void createGame() {
    for(int i = 0; i < playerCount; i++) {
      players.add(new Player());
    }

    for(Player p : players) {
      int randomNum = random.nextInt(currentBags.size());
      p.setBlackBag(currentBags.get(randomNum));
    }

  }

  
  public void play() {

      System.out.println(
              """
                      Welcome to the PebbleGame!!
                      You will be asked to enter the number of players.
                      and then for the location of the three files in turn containing comma separated integer values for the pebble weights.
                      The integer values must be strictly positive.
                      The game will then be simulated, and output written to files in this directory.
                      """
      );

    while(!gamePlayable){
      try{
        startMenu();
        gamePlayable = true;
      }
      catch(Exception e){
        System.out.println(e.getMessage());
      }
    }
  }
}