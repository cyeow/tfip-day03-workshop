package cart;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * this class manages the folder of users' carts
 */
public class ShoppingCartDB {

    private static List<String> users = new ArrayList<>();
    private static Cart currentCart = new Cart();
    private String savePath = "db";
    private boolean areUsersLoaded = false;

    public ShoppingCartDB(String savePath) {
        this.savePath = savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
    
    public List<String> getUsers() {
        return users;
    }
    private void setUsers(List<String> users) {
        ShoppingCartDB.users = users;
    }
    public Cart getCurrentCart() {
        return currentCart;
    }
    public void setCurrentCart(Cart currentCart) {
        if(!ShoppingCartDB.currentCart.isSaved()) {
            Console cons = System.console();
            String input = cons.readLine("%s's cart has not been saved. Do you want to save it before loading a new cart? (Y/N)\n", currentCart.getUser());
            boolean awaitInput = true;

            while(awaitInput) {
                if(input.toLowerCase().startsWith("y")) {
                    currentCart.save(savePath);
                    awaitInput = false;
                } else if(input.toLowerCase().startsWith("n")) {
                    awaitInput = false;
                } else {
                    System.out.println("Invalid input!");
                }
            }
        }
        ShoppingCartDB.currentCart = currentCart;
    }

    public void loadUsers() {
        File f = new File(savePath);
        
        if(!f.isDirectory()) {
            System.err.printf("Error in accessing %s. Path is not a directory.\n", savePath);
            return;
        }

        List<String> userFileList = new ArrayList<>();

        for(String s : Arrays.asList(f.list())) {
            userFileList.add(s.replace(".cart",""));
        }

        setUsers(userFileList);
        areUsersLoaded = true;
    }

    public Cart login(String user) {

        // if there are items in the active cart, 
        // and the user is set to the default user, 
        // --> set the current cart to be the new user's cart. 
        // otherwise, the current cart belongs to someone else,
        // --> so if it hasn't been saved, we save it then proceed
        // to create a new cart.
        user = user.trim().toLowerCase();

        if(!areUsersLoaded) {
            loadUsers();
        }

        if(!users.contains(user)) {
            if(currentCart.hasContent()) {
                if(currentCart.isDefaultUser()) {
                    currentCart.setUser(user);
                    System.out.printf("Logged into %s's cart.\n", user);
                    return currentCart;
                } else if(!currentCart.isSaved()) {
                    currentCart.save(savePath);
                }
            }
            users.add(user);
        } else if(!currentCart.isSaved()) {
            currentCart.save(savePath);
        }

        currentCart = new Cart(user);
        currentCart.load(savePath);  
        
        System.out.printf("Logged into %s's cart.\n", user);

        return currentCart;
    }

    public void printUsers() {
        if(!areUsersLoaded) {
            loadUsers();
        }
        for(int i = 0; i < users.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, users.get(i).replace(".cart",""));
        }
    }

    public void close() {
        currentCart.save(savePath);
    }
    
}
