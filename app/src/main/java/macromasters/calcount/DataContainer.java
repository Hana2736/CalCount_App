package macromasters.calcount;

import java.util.LinkedList;
import java.util.List;

public class DataContainer {

    public static List<FoodItem> customFoods;
    static boolean setupOnce = false;

    public static void setup() {
        if (setupOnce) return;
        setupOnce = true;
        customFoods = new LinkedList<>();

    }


    public enum MealType {
        MEAL, SNACK, DRINK
    }

    public class FoodItem {
        String foodName;
        int calories;
        MealType mealType;
    }
}

