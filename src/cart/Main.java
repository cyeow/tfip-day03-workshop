package cart;

import java.io.Console;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    // private static List<String> cart = new ArrayList<>();
    // private static Set<String> users = new HashSet<>();
    // private static String activeUser = null;
    private static File activeDirectory = null;

    public static void main(String[] args) throws Exception {
        Console cons = System.console();
        // boolean continueRunning = true;
        // List<String> cart = new ArrayList<>();
        // Set<String> users = new HashSet<>();
        // String activeUser = null;

        // check / setup directory 
        Path p = Paths.get(args[0]);
        activeDirectory = p.toFile();

        while(activeDirectory.isFile()) {
            System.out.println("You've provided a file. Please provide a directory instead:");
            p = Paths.get(cons.readLine());
            activeDirectory = p.toFile();
        }

        Session s = new Session(args[0]);
        s.start();
    }
}
