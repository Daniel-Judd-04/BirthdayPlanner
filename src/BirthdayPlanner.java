import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BirthdayPlanner {

    private static ActivityStore mainActivities;
    private static CafeStore cafes;
    private static RestaurantStore restaurants;

    private static final Random random = new Random();

    // args format:
    // {word input [string], max prefix length [number]}
    public static void main(String[] args) throws IOException {
        // Check that at least 1 argument was given
        if (args.length > 0) {
            // Initialise userInput from 1st argument
            String userInput = args[0];

            // Set maxPrefixLength
            int maxPrefixLength = getMaxPrefixLength(args);

            // Check that maxPrefixLength is within bounds [1-input length]
            if (maxPrefixLength >= 1 && maxPrefixLength <= userInput.length()) {
                // Initialise activities using file names and prefix length
                mainActivities = new ActivityStore("main-activities.txt", maxPrefixLength);
                cafes = new CafeStore("cafes.txt", maxPrefixLength);
                restaurants = new RestaurantStore("restaurants.txt", maxPrefixLength);

                // Generate plan
                ArrayList<String> birthdayPlan = generate(userInput, maxPrefixLength);

                // Display items
                for (String item : birthdayPlan) { // For each item in list
                    System.out.println(item);
                }
            } else {
                System.out.println("2nd argument should be between 1 and " + userInput.length() + ", but was " + maxPrefixLength);
            }
        } else {
            System.out.println("No arguments given");
        }
    }

    private static int getMaxPrefixLength(String[] args) {
        if (args.length == 2) {
            // Check that an int was given
            if (args[1].matches("[0-9]+")) {
                return Integer.parseInt(args[1]);
            } else System.out.println("Second argument must be an int.");
        } else if (args.length == 1) {
            return 1; // Default length is 1 if a second argument is not given
        } else System.out.println("Too many arguments given.");
        return 1;
    }

    // If no maxPrefixLength is given, call generate() with a maxPrefixLength of 1
    public static ArrayList<String> generate(String input) {
        return generate(input, 1);
    }

    public static ArrayList<String> generate(String input, int maxPrefixLength) {
        ArrayList<String> birthdayPlan = new ArrayList<>();

        // To check whether a restaurant activity has been added
        boolean restaurantAdded = false;
        // To check whether the last activity was an eating activity
        boolean lastActivityWasEating = false;
        // To check
        int mainActivityStreak = 0;

        for (int letterIndex = 0; letterIndex < input.length(); letterIndex++) { // For each letter
            ArrayList<ActivityStore> possibleActivities = getPossibleActivities(letterIndex, mainActivityStreak, lastActivityWasEating, restaurantAdded);
            // Get a random activity from possible activities
            ActivityStore chosenActivity = possibleActivities.get(random.nextInt(possibleActivities.size()));

            // Although it is logically impossible for chosenActivity to be null, still check
            if (chosenActivity != null) {

                // Update variables for next letter
                if (chosenActivity == mainActivities) mainActivityStreak++; // Increment streak
                else mainActivityStreak = 0; // Reset streak

                if (chosenActivity == cafes || chosenActivity == restaurants) { // Check for eating activity
                    lastActivityWasEating = true;
                    if (chosenActivity == restaurants) restaurantAdded = true; // Check for restaurant activity
                } else lastActivityWasEating = false; // Only reset eating (not restaurant)


                // Get the biggest valid key from chosenActivity
                String key = getBiggestKey(input, letterIndex, maxPrefixLength, chosenActivity);

                // Check that a valid key was found
                if (key != null) {
                    // Get random item from chosenActivity using the key
                    String item = chosenActivity.getRandomItem(key);
                    birthdayPlan.add(item);

                    // Increment letterIndex by the length of the key-1
                    // E.g. A key of 'MA' should skip the 'A' so should increment letterIndex by 1
                    letterIndex += key.length() - 1;
                } else {
                    //
                    return generate(input, maxPrefixLength);
                }
            }
        }

        return birthdayPlan;
    }

    // Function to get the biggest key
    // It starts by setting a randomStartingLength using random (for variability)
    // It then iterates until it finds a valid key
    // Returns biggest valid key as String
    private static String getBiggestKey(String input, int letterIndex, int maxPrefixLength, ActivityStore chosenActivity) {
        int randomStartingLength = random.nextInt(1, maxPrefixLength+1);
        // Loop from maxPrefixLength down to 1
        for (int prefixLength = randomStartingLength; prefixLength > 0; prefixLength--) {
            // Check that index is within bounds
            if (letterIndex+prefixLength <= input.length()) {
                // Get prefix from current letter + prefix length
                String prefix = input.substring(letterIndex, letterIndex + prefixLength);
                if (chosenActivity.contains(prefix)) { // Check that prefix occurs in chosenActivity
                    return prefix;
                }
            }
        }
        return null;
    }

    // Function to get the possible activities
    // It uses the provided logic to determine if main, cafe or restaurant should be available for this item
    // Returns ArrayList of ActivityStore
    private static ArrayList<ActivityStore> getPossibleActivities(int letterIndex, int mainActivityStreak, boolean lastActivityWasEating, boolean restaurantAdded) {
        ArrayList<ActivityStore> possibleActivities = new ArrayList<>();

        // Add Activities depending on variables
        if (letterIndex == 0) { // Must begin with cafe or main
            possibleActivities.add(cafes);
            possibleActivities.add(mainActivities);
        } else { // Normal sequence
            // Prevent streak of more than 2 main
            if (mainActivityStreak < 2) {
                possibleActivities.add(mainActivities);
            }
            // Prevent two eating in a row
            if (!lastActivityWasEating) {
                possibleActivities.add(cafes);
                // Check restaurant has NOT been added
                if (!restaurantAdded) {
                    possibleActivities.add(restaurants);
                }
            }
        }

        return possibleActivities;
    }
}
