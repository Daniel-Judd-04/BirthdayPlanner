import java.io.IOException;

public class CafeStore extends ActivityStore {

    public CafeStore() {
        super();
    }

    public CafeStore(String fileName) throws IOException {
        super(fileName);
    }

    public CafeStore(String fileName, int maxPrefixLength) throws IOException {
        super(fileName, maxPrefixLength);
    }

//    @Override
//    public void add(String key, String item) {
//        // Append ' (cafe)' to the end of all items
//        super.add(key, item + " (cafe)");
//    }

    @Override
    public String getRandomItem(String key) {
        // Append ' (cafe)' to the end of the random item
        return super.getRandomItem(key) + " (cafe)";
    }
}
