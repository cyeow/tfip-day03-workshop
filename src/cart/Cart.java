package cart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/*
 * this class manages the active cart
 */
public class Cart {
    private static final String defaultUser = "defaultUser";
    private String user = defaultUser;
    private List<String> contents = new ArrayList<>();
    private boolean isSaved = false;

    public Cart() {
        // default constructor initialises the username to defaultUser.
        // this will be the first cart setup when the program starts.
    }

    public Cart(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isDefaultUser() {
        return defaultUser.equalsIgnoreCase(user);
    }

    public boolean isSaved() {
        return isSaved;
    }

    public boolean hasContent() {
        return contents.size() > 0;
    }

    public void add(String items) {
        String[] list = items.toLowerCase().split(",");

        for(String l : list) {
            l = l.trim();
            if(!contents.contains(l)) {
                contents.add(l);
            } else {
                System.out.printf("%s is already in %s's cart.\n", l, user);
            }
            System.out.printf("%s added to %s's cart.\n", l, user);
        }

        isSaved = false;
    }

    // the user will pass in an index that is 1 larger than the 
    // actual index, session.java will handle this by passing in
    // an index that is 1 less than indicated by the user.
    public void delete(int index) {
        if(contents.size() > index) {
            String removedItem = contents.remove(index);
            System.out.printf("%s was removed from %s's cart.\n", removedItem, user);
        } else {
            System.err.println("There are no items at index " + (index + 1) + "in " + user + "'s cart.");
        }

        isSaved = false;
    }

    // this is another way of deleting an item, using the item name
    // extra step just to consider an alternative way for users to 
    // use this function
    public void delete(String item) {
        // check if the user is requesting for more than 1 item to be deleted.
        // if yes, delete them 1 by 1 (using function overload)
        if(item.contains(",")) {
            delete(item.split(","));
            return;
        }

        item = item.toLowerCase().trim(); // clean the input

        if(item.length() == 1) {
            delete(Integer.parseInt(item) - 1);
            return;
        }

        if(contents.contains(item)) {
            if(contents.remove(item)) { 
                System.out.printf("%s was removed from %s's cart.\n", item, user);
            } else {
                // should not happen as we have verified that contents has item.
                System.err.printf("Error encountered deleting %s from %s's cart.\n", item, user);
            }
        } else {
            System.err.printf("%s's cart does not contain %s.\n", user, item);
        }

        isSaved = false;
    }
    
    // to delete multiple items in 1 line
    public void delete(String[] items) {
        for(String item : items) {
            delete(item);
        }
    }

    public void printList() {
        if(contents.size() <= 0) {
            System.out.printf("%s's cart is empty.\n", user); 
        } else {
            for(int i = 0; i < contents.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, contents.get(i));
            }
        }
    }

    public void save(String path) {
        // check if the user is set to defaultUser
        if(user.equalsIgnoreCase(defaultUser)) {
            System.out.println("Please login before saving the cart.");
            return;
        }


        // check if file has been saved before
        if(!isSaved) {
            String saveLocation = path + File.separator + user + ".cart";
            File saveFile = new File(saveLocation); 
            File saveFolder = new File(path);

            try {
                if(!saveFolder.exists()) {
                    Files.createDirectory(Paths.get(path));
                }
                if(!saveFile.exists()) {
                    saveFile.createNewFile();
                } else {
                    // honestly idk if this is necessary
                    saveFile.delete(); 
                    saveFile.createNewFile();
                }

                FileWriter fw = new FileWriter(saveLocation);
                BufferedWriter bw = new BufferedWriter(fw);

                for(String c : contents) {
                    bw.write(c + "\n");
                }

                bw.flush();
                bw.close();
                fw.close();
                isSaved = true;
                System.out.printf("%s's cart has been successfully saved.\n", user);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.printf("%s's cart has already been saved!\n", user);
        }
        
    }

    public void load(String path) {
        String storedLocation = path + File.separator + user + ".cart";

        File f = new File(storedLocation);

        // dont need to do anything if there is no cart saved to this user
        if(f.exists()) {
            try {

                FileReader fr = new FileReader(storedLocation);
                BufferedReader br = new BufferedReader(fr);
                String input;

                while((input = br.readLine()) != null) {
                    contents.add(input);
                }

                br.close();
                fr.close();

                System.out.printf("%s's cart has been successfully loaded!\n", user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} 
