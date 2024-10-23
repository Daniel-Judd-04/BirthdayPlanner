import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class ActivityStore {

    protected final Hashtable<String, ArrayList<String>> store;

    public ActivityStore() {
        // Initialise store
        this.store = new Hashtable<>();
    }

    public ActivityStore(String fileName) throws IOException {
        this.store = new Hashtable<>();

        // Read the lines from file
        ArrayList<String> lines = readLinesFromFile(fileName);
        // Add each item to store, with a key of the first letter of item
        for (String line : lines) {
            add(line.substring(0, 1), line);
        }
    }

    public ActivityStore(String fileName, int maxPrefixLength) throws IOException {
        this.store = new Hashtable<>();

        // Read the lines from file
        ArrayList<String> lines = readLinesFromFile(fileName);
        for (String line : lines) { // For each line
            for (int i = 0; i < maxPrefixLength; i++) { // For each prefixLength
                // Check that the item is bigger than prefix+1
                if (line.length() > i + 1) add(line.substring(0, i + 1), line);
            }
        }
    }

    private ArrayList<String> readLinesFromFile(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        FileReader file;
        BufferedReader reader;

        try {
            file = new FileReader(fileName);
            reader = new BufferedReader(file);

            // Read each line, and add to list, until end is reached
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine(); // Read next line
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException(e);
        }

        // Close the file and reader after try and catch
        file.close();
        reader.close();

        return lines;
    }

    public void add(String key, String item) {
        key = key.toUpperCase(); // Case-insensitive

        if (contains(key)) { // Already exists in store
            // Get list using given key, then add the new item
            store.get(key).add(item);
        } else { // Does NOT exist in store
            // Create new list, and add new item
            ArrayList<String> itemList = new ArrayList<>();
            itemList.add(item);
            // Add this new list to the store using the given key
            store.put(key, itemList);
        }
    }

    public String getRandomItem(String key) {
        key = key.toUpperCase(); // Case-insensitive

        // Check that the key occurs in store
        if (contains(key)) {
            // Get list of items using given key
            ArrayList<String> itemList = store.get(key);
            // Get random item from list using random
            Random random = new Random();
            int randomIndex = random.nextInt(itemList.size());
            String randomItem = itemList.get(randomIndex);

            // Return the capitalised key + the rest of the string
            return key + randomItem.substring(key.length());
        } else {
            return null;
        }
    }

    // Helper function
    // To check if store contains key (case-insensitive)
    // Used when adding items, getting items and in BirthdayPlanner.generate
    public boolean contains(String key) {
        return store.containsKey(key.toUpperCase());
    }
}
