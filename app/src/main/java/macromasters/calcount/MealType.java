package macromasters.calcount;

import java.io.Serializable;

/**
 * Represents the type of a meal.
 * This enum is {@link Serializable} to allow its instances to be saved as part of {@link FoodItem}.
 */
public enum MealType implements Serializable {
    /** Represents a full meal. */
    MEAL,
    /** Represents a snack. */
    SNACK,
    /** Represents a drink. */
    DRINK
}