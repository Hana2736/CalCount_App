package macromasters.calcount;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a food item with its name, calories, meal type, and the date it was eaten.
 * This class implements {@link Serializable} to allow its instances to be saved to a file.
 */
public class FoodItem implements Serializable {
    /** The name of the food item. */
    String foodName;
    /** The number of calories in the food item. */
    int calories;
    /** The type of meal (e.g., MEAL, SNACK, DRINK). */
    MealType mealType;
    /** The date and time the food item was eaten. */
    LocalDateTime eatenDate;
}