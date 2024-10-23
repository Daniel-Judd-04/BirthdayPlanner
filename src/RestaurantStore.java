import java.io.IOException;

public class RestaurantStore extends ActivityStore {

    public RestaurantStore() {
        super();
    }

    public RestaurantStore(String fileName) throws IOException {
        super(fileName);
    }

    public RestaurantStore(String fileName, int maxPrefixLength) throws IOException {
        super(fileName, maxPrefixLength);
    }

//    @Override
//    public void add(String key, String item) {
//        // Append ' (restaurant)' to the end of all items
//        super.add(key, item + " (restaurant)");
//    }

    @Override
    public String getRandomItem(String key) {
        // Append ' (restaurant)' to the end of the random item
        return super.getRandomItem(key) + " (restaurant)";
    }
}
