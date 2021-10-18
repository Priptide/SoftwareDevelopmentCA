import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bag {

  File inputFile;

  BagType type;

  List<Integer> contents;

  /**
   * @apiNote This version should only be used for creating a white bag.
   *
   * Creates a new bag object with no contents, hence an empty bag to use in the pebble game.
   *
   * @param type Can be given as either but should be only BagType.WHITE
   */
  public Bag(BagType type) {
    this.inputFile = null;
    this.type = type;
    this.contents = new ArrayList<>();
  }

  /**
   * @apiNote Please use the load function after initializing a black bag the bag will be empty.
   *
   * Creates a bag object which can be used to play the pebble game.
   *
   * @param inputFile Used when creating a black bag, needs to be a readable csv file.
   * @param type The type of bag, either white or black.
   */
  public Bag(File inputFile, BagType type) {
    this.inputFile = inputFile;
    this.type = type;
    this.contents = new ArrayList<>();
  }

  /**
   * @deprecated Should only be used in testing.
   *
   * Creates a new bag object with a list of contents for use in the pebble game.
   *
   * @param inputFile The CSV of bag contents
   * @param type The type of bag, either white or black.
   * @param contents The current contents of the bag.
   */
  public Bag(File inputFile, BagType type, List<Integer> contents) {
    this.inputFile = inputFile;
    this.type = type;
    this.contents = contents;
  }

  /**
   * @apiNote Don't try load an empty file or a white bag as these cannot be loaded and will throw exceptions
   *
   * As we check the input in our pebble game file this should never
   * throw a FileNotFound exception although will need to be put in a try-catch method.
   *
   * @throws FileNotFoundException This should never throw but could if there is a load called and the file is deleted at the same time.
   * @throws Exception If there is not file there anymore or the bag you are loading is white.
   */
  public void load() throws Exception {
    if (inputFile.exists() && type == BagType.BLACK) {
      FileReader fileReader = new FileReader(inputFile);
      int data = fileReader.read();
      String currentValue = "";
      while (data != -1) {
        if ((char) data != ',') {
          currentValue += (char) data;
        } else {
          if (!currentValue.trim().equals("")) {
            try {
              contents.add(Integer.parseInt(currentValue.trim()));
            } catch (Exception e) {}
          }
          currentValue = "";
        }
        data = fileReader.read();
      }
      if (!currentValue.trim().equals("")) {
        try {
          contents.add(Integer.parseInt(currentValue.trim()));
        } catch (Exception e) {}
      }
      fileReader.close();
    } else {
      throw new Exception(
        type == BagType.WHITE
          ? "Can't load for a white bag"
          : "The file provide for this bag has been deleted"
      );
    }
  }

  /**
   * Gets a list of integers currently in the bag.
   *
   * @return Current contents of the bag.
   */
  public List<Integer> getContents() {
    return contents;
  }

  /**
   * @apiNote Should be the only method used to add to the bag.
   *
   * Adds a single value to the current contents of the bag.
   *
   * @param value Integer to add to the bag.
   */
  public void addValueToBag(int value) {
    this.contents.add(value);
  }

  /**
   * @deprecated Should only be used for testing.
   *
   * Adds a list of values to the currents contents of the bag.
   *
   * @param values List of values to add to the bag.
   */
  public void addListToBag(List<Integer> values) {
    this.contents.addAll(values);
  }

  /**
   * Removes a given value from the bag using its index.
   *
   * @param index The given index of the value in the bag.
   */
  public void removeValueFromBag(int index) {
    this.contents.remove(index);
  }

  /**
   * Picks a random value from the bag, removes it from it's contents and returns it too the user.
   *
   * @return A random value for the current bag.
   */
  public synchronized int pick() {
    Random rand = new Random();
    int index = rand.nextInt(contents.size());
    removeValueFromBag(index);
    return contents.get(index);
  }
}