import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bag {

  private File inputFile;
  private BagType type;
  private List<Integer> contents;
  private String bagName;

  /**
   * Creates a new bag object with no contents, hence an empty bag to use in the pebble game.
   *
   * @apiNote This version should only be used for creating a white bag.
   *
   * @param type Can be given as either but should be only BagType.WHITE
   */
  public Bag(BagType type) {
    this.inputFile = null;
    this.type = type;
    this.contents = new ArrayList<>();
  }

  /**
   * Creates a bag object which can be used to play the pebble game.
   *
   * @apiNote Please use the load function after initializing a black bag the bag will be empty.
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
   * Creates a bag object with a list of contents for use in the pebble game.
   *
   * @deprecated Should only be used in testing.
   *
   * @param inputFile The CSV of bag contents
   * @param type The type of bag, either white or black.
   * @param contents The current contents of the bag.
   */
  @Deprecated
  public Bag(File inputFile, BagType type, List<Integer> contents) {
    this.inputFile = inputFile;
    this.type = type;
    this.contents = contents;
  }

  /**
   * As we check the input in our pebble game file this should never
   * throw a FileNotFound exception although will need to be put in a try-catch method.
   *
   * @apiNote Don't try load an empty file or a white bag as these cannot be loaded and will throw exceptions
   *
   * @throws FileNotFoundException This should never throw but could if there is a load called and the file is deleted at the same time.
   * @throws Exception If there is not file there anymore or the bag you are loading is white.
   */
  public void load() throws Exception {
    if (
      inputFile != null &&
      inputFile.exists() &&
      inputFile.isFile() &&
      inputFile.canRead() &&
      type == BagType.BLACK
    ) {
      FileReader fileReader = new FileReader(inputFile);
      int data = fileReader.read();
      String currentValue = "";
      while (data != -1) {
        if ((char) data != ',') {
          //While we haven't met a comma we keep adding the characters to make the value.
          currentValue += (char) data;
        } else {
          //We trim the value and check it isn't a null value
          if (!currentValue.trim().equals("")) {
            int value = 0;
            try {
              value = Integer.parseInt(currentValue.trim());
            } catch (Exception e) {
              fileReader.close();
              throw new Exception(
                "You can only load values that are integers!"
              );
            }
            if (value > 0) contents.add(value); else {
              System.out.println(
                "WARNING: Pebbles have a strictly positive weight negative values will not be loaded!"
              );
            }
          }
          currentValue = "";
        }
        data = fileReader.read();
      }
      if (!currentValue.trim().equals("")) {
        int value = 0;
        try {
          value = Integer.parseInt(currentValue.trim());
        } catch (Exception e) {
          fileReader.close();
          throw new Exception("You can only load values that are integers!");
        }
        if (value > 0) contents.add(value); else {
          System.out.println(
            "WARNING: Pebbles have a strictly positive weight negative values will not be loaded!"
          );
        }
      }
      fileReader.close();

      if (contents.size() == 0) System.out.println(
        "WARNING: Nothing was loaded from the given file into this bag"
      );
    } else {
      throw new Exception(
        type == BagType.WHITE
          ? "Can't load into a white bag"
          : "Please provide a valid, readable file to load from for this bag"
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
   * Gives the current number of pebbles in the bag
   * @return Number of Pebbles
   */
  public int pebbleCount() {
    return contents.size();
  }

  /**
   * Sets the name of the bag
   * @param name New Bag Name
   */
  public void setName(String name) {
    this.bagName = name;
  }

  /**
   * Gets the name of a given bag
   * @return Bag Name
   */
  public String getName() {
    return this.bagName;
  }

  /**
   * Used to empty white bags when refilling an empty black bag.
   * @apiNote This is synchronized so that it is thread safe.
   */
  public synchronized void emptyBag() {
    contents = new ArrayList<>();
  }

  /**
   *
   * Adds a single value to the current contents of the bag.
   * @apiNote This is synchronized so that it is thread safe.
   * @param value Integer to add to the bag.
   */
  public synchronized void addValueToBag(int value) {
    this.contents.add(value);
  }

  /**
   * Adds a list of values to the currents contents of the bag.
   * @apiNote This is synchronized so that it is thread safe.
   * @param values List of values to add to the bag.
   */
  public synchronized void addListToBag(List<Integer> values) {
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
   * @apiNote This is synchronized so that it is thread safe.
   * @return A random value for the current bag.
   */
  public synchronized int pick() throws Exception {
    if (contents.size() > 0 && type == BagType.BLACK) {
      Random rand = new Random();
      int index = rand.nextInt(contents.size());
      removeValueFromBag(index);
      return contents.get(index);
    } else {
      throw new Exception(
        type == BagType.WHITE
          ? "You can't pick from a white bag"
          : "You can't pick from an empty bag"
      );
    }
  }
}
