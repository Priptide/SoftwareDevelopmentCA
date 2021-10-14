public class Menu {
    public static void startUp() {
        System.out.println("Welcome to the PebbleGame!!\n" + 
                           "You will be asked to enter the number of players.\n" + 
                           "and then for the location of the three files in turn containing comma separated integer values for the pebble weights.\n" + 
                           "The integer values must be strictly positive.\n" +
                           "The game will then be simulated, and output written to files in this directory.");  
    }
    public static void main(String[] args) {
        startUp();
    }
}