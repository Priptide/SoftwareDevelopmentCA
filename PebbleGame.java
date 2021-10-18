import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PebbleGame {

  public static void startMenu() {
    System.out.println("Please enter the number of players:");

    Scanner sc = new Scanner(System.in);

    String key = sc.nextLine();

    if (key.equalsIgnoreCase("e")) System.exit(1);

    int playerCount = 0;

    int playerCountHold = 0;

    List<Bag> currentBags = new ArrayList<>();
    
    try {
      playerCount = playerCountHold = Integer.parseInt(key);
      if (playerCount <= 0) {
        System.out.println("You can't play with no one you freak!\n");
        startMenu();
      }
    } catch (Exception e) {
      //Invalid so we restart
      System.out.println("Invalid Input!\n");
      startMenu();
    }

    while (playerCount > 0) {
      System.out.println(
        "Please enter location of bag number " +
        (playerCountHold - playerCount) +
        " to load"
      );

      String filePath = sc.nextLine();

      File currentFile = new File(filePath);

      if (!currentFile.exists()) System.out.println(
        "The given file doesn't exist, try again!"
      ); else {
        playerCount--;

        Bag newBag = new Bag(currentFile, BagType.BLACK);

        try {
          newBag.load();
        } catch (Exception e) {
          System.out.println(e.toString());
          break;
        }

        currentBags.add(newBag);
      }
    }
    sc.close();
  }

  public static void main(String[] args) {
    System.out.println(
            """
                    Welcome to the PebbleGame!!
                    You will be asked to enter the number of players.
                    and then for the location of the three files in turn containing comma separated integer values for the pebble weights.
                    The integer values must be strictly positive.
                    The game will then be simulated, and output written to files in this directory.
                    """
    );
    startMenu();
  

}
}
