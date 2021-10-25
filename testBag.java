import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import org.junit.Test;

public class testBag {

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

  //Test sending a file containing negative numbers and one positive value.
  @Test
  public void testNegativeFile() {
    URL url = this.getClass().getResource("/test_file_1.csv");
    File testFile = new File(url.getFile());
    if (!testFile.exists()) {
      throw new AssertionError("Can't find file!");
    }
    Bag test = new Bag(testFile, BagType.BLACK);

    //We don't need to worry about this as an error will be
    try {
      test.load();
    } catch (Exception e) {
      throw new AssertionError("Failed to load test file: " + e.getMessage());
    }
    //Check we only have one value
    assertEquals(1, test.getContents().size());

    //Check this value is the 12 we put in.
    assertEquals(12, (int) test.getContents().get(0));
  }
}
