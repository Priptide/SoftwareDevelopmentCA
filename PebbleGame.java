
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.Random;

public class PebbleGame {

  static int playerCount;
  static List<Player> players;
  static Map<Bag, Bag> bagMap;
  static List<Bag> currentBags;
  static boolean gamePlayable = false;
  Scanner sc = new Scanner(System.in);
  // This being volatile should mean that when one thread has one all the threads stop.
  public volatile boolean done = false;

  /**
   * This method is used to run through the initial start of the game to acquire the player number and load all three black bags into the game.
   * @throws Exception For any errors that may occur when trying to load a new game
   */
  public void startMenu() throws Exception{

    //Load player numbers
    System.out.println("Please enter the number of players:");

    //We setup as a blank string to help with error checking
    String key = "";

    //Try and load the number of players from the user input.
    try{
      key = sc.nextLine();
    }
    catch(Exception e){
      //If we somehow have invalid input parse it as an empty string.
      key = "";
    }

    //If the user presses E or e we quit.
    if (key.equalsIgnoreCase("e")) System.exit(0);

    //Check if we can parse the input as a number
    try {
      playerCount = Integer.parseInt(key);
    } catch (Exception e) {
      //Invalid input so we restart
      throw new Exception("Please input a number!\n");
    }

    //Check the player number is greater than zero.
    if (playerCount <= 0) {
      throw new Exception("You can't play with less than one player!\n");
    }

    //Create new bag and pebble counts
    int bagCount = 0;
    int currentPebbleCount = 0;

    //Reset the current bags map of black bags to white
    bagMap = new HashMap<>();
    currentBags = new ArrayList<>();

    //Set up our 3 black bags and their corresponding white bags.
    while (bagCount < 3) {

      System.out.println(
        "Please enter location of bag number " +
        (bagCount) +
        " to load"
      );


      String filePath = "";

      //Attempt to get the next line of input as our filepath.
      try{
       filePath = sc.nextLine();
      }
      catch(Exception e){
        System.out.println("Invalid input!\n");
        continue;
      }

      //Check again for the exit key
      if (key.equalsIgnoreCase("e")) System.exit(0);

      //Setup a new file with the given path
      File currentFile = new File(filePath);

      //If the file doesn't exist we restart the loop
      if (!currentFile.exists()) 
        System.out.println("The given file doesn't exist, try again!"); 
      else 
      {

        //Create two new bags, an empty black and empty white one
        Bag newBlackBag = new Bag(currentFile, BagType.BLACK);
        Bag newWhiteBag = new Bag(BagType.WHITE);

        //Try to load the bag from the given file
        try {
          newBlackBag.load();
        } catch (Exception e) {
          System.out.println(e.toString() + "\n");
          continue;
        }

        //Set the bag names
        newBlackBag.setName(String.valueOf((char)('X' + bagCount)));
        newWhiteBag.setName(String.valueOf((char)('A' + bagCount)));

        //If we can load the bag, map the black bag as a key for the white one.
        bagMap.put(newBlackBag, newWhiteBag);
        currentBags.add(newBlackBag);
        
        //We then add too the bag and pebble count
        bagCount++;
        currentPebbleCount += newBlackBag.pebbleCount();
      }
    }

    //Check the bag count is above 11 per player if not throw an error
    int minCount = playerCount * 11;
    if(minCount > currentPebbleCount){
      throw new Exception("\nYou must have at least 11 pebbles per player, you were "+ (minCount - currentPebbleCount) +" pebbles short!\nPlease try again!\n");
    }
  }



  class Player extends Thread{

    private List<Integer> hand;

    private Bag previousBag;
  
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

    @Override
    public void run (){

      //Create new hand
      hand = new ArrayList<>();

      //Create file or load output file
      File outputFile = new File("logs/player"+ Thread.currentThread().getName()+ "_output.txt");

      //Create file writer.
      FileWriter fileWriter = null;


      //Create a new file if one doesn't exist.
      try {
        outputFile.createNewFile();
        fileWriter = new FileWriter(outputFile);
      } catch (Exception e) {
        System.out.println("Critical error: " + e.getMessage());
        System.exit(1);
      }



		  while (!done){

        //If the hand size is greater than 10 we remove a value at random
        if (hand.size() >= 10) {
            int index = random.nextInt(10);
            int pebbleTemp = hand.get(index);
            hand.remove(index);

            //Then place the value in the previous white bag
            bagMap.get(previousBag).addValueToBag(pebbleTemp);

            try{
              fileWriter.write("player" + Thread.currentThread().getName() + " has discarded a " + pebbleTemp + " to bag " + bagMap.get(previousBag).getName() + "\n");
              fileWriter.write("player" + Thread.currentThread().getName() + " hand is " + hand.toString() + "\n");
            }
            catch(Exception e)
            {
              System.out.println("Player "  + Thread.currentThread().getName() + " couldn't write to output!");
            }
        }

        //We then pick a new value to add too our hand
        int newBagIndex = random.nextInt(currentBags.size());
        Bag currentBag = previousBag = currentBags.get(newBagIndex);

        int currentPick = 0;
        try {
          currentPick = currentBag.pick();
        } catch (Exception e) {
          //Refill the empty bag causing the error
          currentBag.addListToBag(bagMap.get(currentBag).getContents());
          bagMap.get(currentBag).emptyBag();
          //If there is an error the bag is empty so we re-loop until the bag has pebbles
          continue;
        }
        
        //Add the value to the hand.
        hand.add(currentPick);

        //If the bag is now empty we refill it from its white bag.
        if(currentBag.pebbleCount() <= 0){
          //Move the pebbles from it's white bag into the black one.
          currentBag.addListToBag(bagMap.get(currentBag).getContents());
          //Empty the white bag.
          bagMap.get(currentBag).emptyBag();
        }
        
        try{
          fileWriter.write("player" + Thread.currentThread().getName() + " has drawn a " + currentPick + " from bag " + currentBag.getName() + "\n");
          fileWriter.write("player" + Thread.currentThread().getName() + " hand is " + hand.toString() + "\n");
          fileWriter.write("hand total: " + hand.stream().reduce(0, Integer::sum) + "\n");
        }
        catch(Exception e)
        {
          System.out.println("Player "  + Thread.currentThread().getName() + " couldn't write to output! Error: " + e.toString());
        }

        if (hand.stream().reduce(0, Integer::sum) == 100 && hand.size() == 10) {
          // System.out.print("Player " + Thread.currentThread().getName() + " has won!\n");
          // this.done = true;
          stopThread();
        }

        //Now we know the thread has made it through a move we can notify the other threads.

        //TODO Notify threads we are now done making our move
 	 	  }
      
    }

    public void stopThread() {
      System.out.print("Player " + Thread.currentThread().getName() + " has won!\n");
      done = true;
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

    //Create a new list of players
    players = new ArrayList<>();

    //Add as many threads as players there are
    for(int i = 0; i < playerCount; i++) {
      Player temp = new Player();
      temp.setName(""+i);
      players.add(temp);
    }

    //Start all threads
    for(Player p : players) {
      p.start();
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
        //Load the data about the game from the start menu.
        startMenu();
        //This ensure we keep looping here until we make it through the start menu or exit the game.
        gamePlayable = true;
      }
      catch(Exception e){
        //If we get an error print it and loop again.
        System.out.println(e.getMessage());
      }
    }

    createGame();

  }

}