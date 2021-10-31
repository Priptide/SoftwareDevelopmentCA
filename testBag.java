import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testBag {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  //Set up for listening to warnings
  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  //Restore streams for warnings
  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  //Test loading a black bag with no file
  @Test
  public void testNoFile() {
    Bag test = new Bag(BagType.BLACK);
    Throwable exception = assertThrows(Exception.class, () -> test.load());
    assertEquals(
      "Please provide a valid, readable file to load from for this bag",
      exception.getMessage()
    );
  }

  //Test picking works
  @Test
  public void checkPick() {
    URL url = this.getClass().getResource("/test_file_3.csv");
    File testFile = new File(url.getFile());
    Bag test = new Bag(testFile, BagType.BLACK);
    Bag whiteTest = new Bag(BagType.WHITE);
    try {
      test.load();
    } catch (Exception e) {
      throw new AssertionError("Failed to load test file: " + e.getMessage());
    }

    int initCount = test.pebbleCount();
    for (int i = 0; i < initCount; i++) {
      try {
        int temp = test.pick();
        whiteTest.addValueToBag(temp);
      } catch (Exception e) {
        throw new AssertionError("Failed to pick: " + e.getMessage());
      }
    }

    assertEquals(test.pebbleCount(), 0);

    assertEquals(initCount, whiteTest.pebbleCount());
  }

  //Test sending a file containing negative numbers and one positive value.
  @Test
  public void testNegativeFile() {
    URL url = this.getClass().getResource("/test_file_1.csv");
    File testFile = new File(url.getFile());
    if (!testFile.exists()) {
      throw new AssertionError("Can't find test file!");
    }
    Bag test = new Bag(testFile, BagType.BLACK);

    //We don't need to worry about this as an error will be the fault of something else
    try {
      test.load();
    } catch (Exception e) {
      throw new AssertionError("Failed to load test file: " + e.getMessage());
    }

    assertEquals(
      "WARNING: Pebbles have a strictly positive weight negative values will not be loaded!",
      outContent.toString().trim()
    );

    //Check we only have one value
    assertEquals(1, test.getContents().size());

    //Check this value is the 12 we put in.
    assertEquals(12, (int) test.getContents().get(0));
  }

  //Test trying to load from an empty file
  @Test
  public void testEmptyFile() {
    URL url = this.getClass().getResource("/test_file_2.csv");
    File testFile = new File(url.getFile());
    Bag test = new Bag(testFile, BagType.BLACK);

    try {
      test.load();
    } catch (Exception e) {
      throw new AssertionError("Failed to load test file: " + e.getMessage());
    }

    assertEquals(
      "WARNING: Nothing was loaded from the given file into this bag",
      outContent.toString().trim()
    );
  }

  //Test trying to pick from an empty bag
  @Test
  public void testEmptyPick() {
    URL url = this.getClass().getResource("/test_file_2.csv");
    File testFile = new File(url.getFile());
    Bag test = new Bag(testFile, BagType.BLACK);

    try {
      test.load();
    } catch (Exception e) {
      throw new AssertionError("Failed to load test file: " + e.getMessage());
    }

    Throwable exception = assertThrows(Exception.class, () -> test.pick());
    assertEquals("You can't pick from an empty bag", exception.getMessage());
  }

  @Test
  public void testWhitePick() {
    Bag test = new Bag(BagType.WHITE);

    Throwable exception = assertThrows(Exception.class, () -> test.pick());
    assertEquals("You can't pick from a white bag", exception.getMessage());
  }
}
