import java.io.File;
import java.util.Scanner;

public class PebbleGame {

  public static void startMenu() {
    System.out.println("Please enter the number of players:");

    Scanner sc = new Scanner(System.in);

    String key = sc.nextLine();

    if (key.toLowerCase().equals("e")) System.exit(1);

    int playerCount = 0;

    int playerCountHold = 0;

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

      File myObj = new File(filePath);

      if (!myObj.exists()) System.out.println(
        "File doesn't exist try again!"
      ); else playerCount--;
    }
    sc.close();
  }

  public static void main(String[] args) {
    System.out.println(
      "Welcome to the PebbleGame!!\n" +
      "You will be asked to enter the number of players.\n" +
      "and then for the location of the three files in turn containing comma separated integer values for the pebble weights.\n" +
      "The integer values must be strictly positive.\n" +
      "The game will then be simulated, and output written to files in this directory.\n"
    );
    startMenu();
  }
}
