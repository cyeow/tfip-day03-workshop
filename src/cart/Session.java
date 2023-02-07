package cart;

import java.io.Console;

/*
 * this class manages the input given by the user via console
 * and sends out the commands appropriately
 */
public class Session {
    String savePath = "db";
    ShoppingCartDB cartdb = null;
    Cart currentCart = null;

    private static String ADD = "add";
    private static String DELETE = "delete";
    private static String LIST = "list";
    private static String LOGIN = "login";
    private static String SAVE = "save";
    private static String USERS = "users";
    private static String EXIT = "exit";
    private static String HELP = "help";

    public Session(String savePath) {
        this.savePath = savePath;
    }

    public void start() {
        Console cons = System.console();
        boolean continueRunning = true;
        String input;
        
        cartdb = new ShoppingCartDB(savePath);
        currentCart = cartdb.getCurrentCart();
    
        System.out.println("Welcome to your shopping cart!");
        printCommands();

        while(continueRunning) {
            input = cons.readLine("> ");

            if(input.startsWith(ADD)) {
                currentCart.add(input.replace(ADD, ""));

            } else if(input.startsWith(DELETE)) {
                currentCart.delete(input.replace(DELETE, ""));

            } else if(input.startsWith(LIST)) {
                currentCart.printList();

            } else if(input.startsWith(LOGIN)) {
                currentCart = cartdb.login(input.replace(LOGIN, ""));

            } else if(input.startsWith(SAVE)) {
                currentCart.save(savePath);

            } else if(input.startsWith(USERS)) {
                cartdb.printUsers();

            } else if(input.startsWith(EXIT)) {
                cartdb.close();
                continueRunning = false;

            } else if(input.startsWith(HELP)) {
                printCommands();

            } else {
                System.out.println("Invalid input, please try again.\n");
                printCommands();
            }
        }
        
        System.out.println("Thanks for shopping here. Goodbye!");
    }

    public void printCommands() {
        System.out.println("Available commands:");
        System.out.println("- add <item1>, <item2>");
        System.out.println("- delete <index/item>");
        System.out.println("- list");
        System.out.println("- login <username>");
        System.out.println("- save");
        System.out.println("- users"); 
        System.out.println("- exit");
    }
    
}
