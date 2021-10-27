
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
  static boolean gamePlayable = false;
  Scanner sc = new Scanner(System.in);

  public void startMenu() throws Exception{

    System.out.println("Please enter the number of players:");
    String key = "";
    try{
      key = sc.nextLine();
    }
    catch(Exception e){
      throw new Exception("Invalid Input!\n");
    }
    currentBags = new HashMap<>();

    if (key.equalsIgnoreCase("e")) System.exit(1);

    try {
      playerCount = Integer.parseInt(key);
      if (playerCount <= 0) {
        throw new Exception("You can't play with zero players!\n");
      }
    } catch (Exception e) {
      //Invalid so we restart
      throw new Exception("Please input a number!\n");
    }

    int bagCount = 0;
    int currentPebbleCount = 0;

    // Set up 3 Black bags containing pebbles given file inputs
    while (bagCount < 3) {
      System.out.println(
        "Please enter location of bag number " +
        (bagCount) +
        " to load"
      );

      String filePath = "";
      try{
       filePath = sc.nextLine();
      }
      catch(Exception e){
        System.out.println("Invalid input!\n");
        continue;
       }

      File currentFile = new File(filePath);

      if (!currentFile.exists()) System.out.println(
        "The given file doesn't exist, try again!"
      ); else {
        Bag newBlackBag = new Bag(currentFile, BagType.BLACK);
        Bag newWhiteBag = new Bag(BagType.WHITE);


        try {
          newBlackBag.load();
        } catch (Exception e) {
          System.out.println(e.toString() + "\n");
          continue;
        }

        currentBags.put(newBlackBag, newWhiteBag);
        
        bagCount++;
        currentPebbleCount += newBlackBag.contents.size();
      }
    }

    //Check the bag count is above 11 per player
    int minCount = playerCount * 11;

    if(minCount > currentPebbleCount){
      throw new Exception("\nYou must have at least 11 pebbles per player, you were "+ (minCount - currentPebbleCount) +" pebbles short!\nPlease try again!\n");
    }
  }

  class Player extends Thread{

    private List<Integer> hand;
    private Bag blackBag;
    private Bag whiteBag;
  
    Player() {}
  
    // Setters, Getters and other methods to handle each player
    public List<Integer> getHand() {
      return hand;
    }

    public void setHand(List<Integer> hand) {
      this.hand = hand;
    }

    public int handSize() {
      return hand.size();
    }

    public int sum() {
      return hand.stream().reduce(0, Integer::sum);
    }

    public Bag getBlackBag() {
      return blackBag;
    }
    public void setBlackBag(Bag blackBag) {
      this.blackBag = blackBag;
    }
    public Bag getWhiteBag() {
      return whiteBag;
    }
    public void setWhiteBag(Bag whiteBag) {
      this.whiteBag = whiteBag;
    }

    // This being volatile should mean that when one thread has one all the threads stop.
    private volatile boolean done = false;

    @Override
    public void run (){
		  while (!done){
        if (hand.size() < 10) {
          try {
            hand.add(blackBag.pick());
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        if (((PebbleGame.Player) hand).sum() >= 100) {
          stopThread();
          System.out.print("Player " + Thread.currentThread() + " has won!!");
        } else {
          int index = random.nextInt(10);
          hand.remove(index);
        }
 	 	  }
    }

    public void stopThread() {
      this.done = true;
    }
  }
  
  /**
   * Method to create a random number accessible by all functions in the class
   */
  static Random random = new Random();

  /**
   * Method to start the game, creating a list of players.
   * Then assigning bags to those players.
   */
  public void createGame() {
    for(int i = 0; i < playerCount; i++) {
      players.add(new Player());
    }

    for(Player p : players) {
      int randomNum = random.nextInt(currentBags.size());
      Bag indexBag = currentBags.keySet().toArray(new Bag[currentBags.size()])[randomNum];
      p.setBlackBag(indexBag);
      p.setWhiteBag(currentBags.get(indexBag));
    }
  }
  
  public void play() {
    System.out.println(
      """
        Welcome to the PebbleGame!!
        You will be asked to enter the number of players.
        and then for the location of the three files in turn containing comma separated integer values for the pebble weights.
        The integer values must be strictly positive
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