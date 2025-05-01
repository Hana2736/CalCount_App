package macromasters.calcount;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FoodItem implements Serializable {
    String foodName;
    int calories;
    MealType mealType;

    LocalDateTime eatenDate;
}
