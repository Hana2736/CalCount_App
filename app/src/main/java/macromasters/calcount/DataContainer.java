package macromasters.calcount;

import android.content.Context;
import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages the storage and retrieval of food item data.
 * This class provides methods to set up, sort, save, load, and clear food data.
 */
public class DataContainer {

    /** List to store custom food items. */
    public static List<FoodItem> customFoods;
    /** Flag to ensure setup is performed only once. */
    static boolean setupOnce = false;
    /** Name of the file used for saving data. */
    static String SAVE_FILE_NAME = "savedata.bin";

    /**
     * Initializes the {@code customFoods} list if it hasn't been set up yet.
     * This method ensures that {@code customFoods} is initialized only once.
     */
    public static void setup() {
        if (setupOnce) return;
        setupOnce = true;
        customFoods = new LinkedList<>();
    }

    /**
     * Sorts the {@code customFoods} list by {@code eatenDate} in descending order (newest first).
     */
    public static void sort() {
        customFoods.sort((f1, f2) -> f2.eatenDate.compareTo(f1.eatenDate));
    }

    /**
     * Converts a {@link MealType} enum to its corresponding emoji string representation.
     *
     * @param mealType The {@link MealType} to convert.
     * @return A string (emoji) representing the meal type, or an empty string if the type is unknown.
     */
    public static String mealTypeToString(MealType mealType) {
        switch (mealType) {
            case MEAL:
                return "🍝";
            case SNACK:
                return "🍫";
            case DRINK:
                return "🍹";
            default:
                return "";
        }
    }

    /**
     * Saves the current list of {@code customFoods} to persistent storage.
     *
     * @param ctx The application context, used to access file output streams.
     * @throws RuntimeException if an {@link IOException} occurs during saving.
     */
    public static void SaveData(Context ctx) {
        try (FileOutputStream fos = ctx.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(customFoods);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the list of {@code customFoods} from persistent storage.
     * If the file does not exist or an error occurs during loading,
     * {@code customFoods} is initialized as a new empty list.
     *
     * @param ctx The application context, used to access file input streams.
     */
    @SuppressWarnings("unchecked")
    public static void LoadData(Context ctx) {
        try (FileInputStream fis = ctx.openFileInput(SAVE_FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object loadedObject = ois.readObject();
            if (loadedObject instanceof List) {
                List<?> loadedList = (List<?>) loadedObject;
                if (!loadedList.isEmpty() && !(loadedList.get(0) instanceof FoodItem)) {
                    throw new IOException("Loaded list does not contain FoodItem objects");
                }
                customFoods = (List<FoodItem>) loadedList;
                sort();
            } else {
                throw new IOException("Loaded object is not a List");
            }
        } catch (IOException | ClassNotFoundException e) {
            customFoods = new LinkedList<>();
        }
    }

    /**
     * Clears all food data from the {@code customFoods} list and deletes the save file from storage.
     *
     * @param ctx The application context, used to access the file system.
     */
    public static void clearData(Context ctx) {
        customFoods.clear();
        File file = new File(ctx.getFilesDir(), SAVE_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}