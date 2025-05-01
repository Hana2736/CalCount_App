package macromasters.calcount;

import android.content.Context;

import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DataContainer {

    public static List<FoodItem> customFoods;
    static boolean setupOnce = false;
    static String SAVE_FILE_NAME = "savedata.bin";

    public static void setup() {
        if (setupOnce) return;
        setupOnce = true;
        customFoods = new LinkedList<>();

    }

    public static void sort() {
        customFoods.sort((f1, f2) -> f2.eatenDate.compareTo(f1.eatenDate));
    }

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

    public static void SaveData(Context ctx) {
        try (FileOutputStream fos = ctx.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(customFoods);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void LoadData(Context ctx) {
        try (FileInputStream fis = ctx.openFileInput(SAVE_FILE_NAME); ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object loadedObject = ois.readObject();
            if (loadedObject instanceof List) {
                customFoods = (List<FoodItem>) loadedObject;
                sort();
            } else {
                throw new IOException("Loading failed");
            }

        } catch (IOException | ClassNotFoundException e) {
            customFoods = new LinkedList<>();
        }
    }
}

