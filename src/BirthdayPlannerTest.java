import java.io.IOException;

public class BirthdayPlannerTest {

    public static void main(String[] args) throws IOException {

        System.out.println("Testing testGetRandomItem()");
        assert testGetRandomItem();
        System.out.println("[PASSED]");

        System.out.println("Testing testNonExistingKey()");
        assert testNonExistingKey();
        System.out.println("[PASSED]");

        System.out.println("Testing testNonExistingKey()");
        assert testCaseInsensitiveKey();
        System.out.println("[PASSED]");

        System.out.println("Testing testLoadFromFile()");
        assert testLoadFromFile();
        System.out.println("[PASSED]");

        System.out.println("All Tests Passed");
    }

    private static boolean testGetRandomItem() {
        // item should equal either 'Archery' or 'Apple bobbing'
        ActivityStore store = new ActivityStore();
        store.add("a", "archery");
        store.add("a", "apple bobbing");
        String item = store.getRandomItem("a");
        return item.equals("Archery") || item.equals("Apple bobbing");
    }

    private static boolean testNonExistingKey() {
        // As 'd' is not added as a key, store.getRandomItem('d') should return null
        ActivityStore store = new ActivityStore();
        store.add("a", "archery");
        store.add("b", "bungee jumping");
        store.add("c", "cycling");
        return store.getRandomItem("d") == null;
    }

    private static boolean testCaseInsensitiveKey() {
        // Even though 'A' is the added key and 'a' is the given key, 'Archery' should still be returned
        // To prove this also works both ways, I have added 'B'
        ActivityStore store = new ActivityStore();
        store.add("A", "archery");
        store.add("b", "bungee jumping");
        return store.getRandomItem("a").equals("Archery") && store.getRandomItem("B").equals("Bungee jumping");
    }

    private static boolean testLoadFromFile() throws IOException {
        // Test that at least one item is added from cafes.txt
        ActivityStore store = new ActivityStore("cafes.txt");
        return store.getRandomItem("a") != null;
    }
}
