import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bag {

  File inputFile;

  BagType type;

  List<Integer> contents;

  public Bag(File inputFile, BagType type) {
    this.inputFile = inputFile;
    this.type = type;
    this.contents = new ArrayList<>();
  }

  public Bag(File inputFile, BagType type, List<Integer> contents) {
    this.inputFile = inputFile;
    this.type = type;
    this.contents = contents;
  }

  public void Load() {}

  public List<Integer> getBag() {
    return contents;
  }
}
