package macromasters.calcount;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.time.LocalDateTime;
/**
 * Activity for users to add custom food items to their log.
 * Users can specify the meal name, calorie amount, meal type, and the day it was eaten.
 */
public class CustomFoodsActivity extends AppCompatActivity {

    // Meal type selections
    private Spinner mealTypeSpinner;
    private String[] mealType = {"MEAL", "SNACK", "DRINK"};
    private String selectedMealType;

    // Day selections
    private Spinner selectDaySpinner;
    private String[] selectDay = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String selectedDay;
    /**
     * Called when the activity is first created.
     * Initializes the UI elements, sets up spinners for meal type and day selection,
     * and handles the logic for adding a new food item.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize DataContainer
        DataContainer.setup();

        // Set status bar and navigation bar to transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightNavigationBars(false);

        setContentView(R.layout.activity_custom_foods);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0,
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });

        Button historyBtn = findViewById(R.id.dailyHistory_overview);
        Button settingsBtn = findViewById(R.id.settings_overview);
        Button customFoodsBtn = findViewById(R.id.customFoodBtn_overview);

        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DailyHistoryPage.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsPage.class);
            startActivity(intent);
        });

        customFoodsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomFoodsActivity.class);
            startActivity(intent);
        });

        // Spinner selection Meal Type
        mealTypeSpinner = findViewById(R.id.Meal_Type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mealType
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);
        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Callback method to be invoked when an item in this view has been
             * selected.
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMealType = mealType[position];
            }
            /**
             * Callback method to be invoked when the selection disappears from this
             * view.
             * @param parent The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMealType = null;
            }
        });

        // Spinner selection Select Day
        selectDaySpinner = findViewById(R.id.Select_Day);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                selectDay
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDaySpinner.setAdapter(adapter2);
        selectDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Callback method to be invoked when an item in this view has been
             * selected.
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = selectDay[position];
            }
            /**
             * Callback method to be invoked when the selection disappears from this
             * view.
             * @param parent The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDay = null;
            }
        });

        EditText mealNameEditText = findViewById(R.id.Meal_Name);
        EditText caloriesEditText = findViewById(R.id.Calorie_Amount);
        Button addFoodButton = findViewById(R.id.Submit_Button);

        addFoodButton.setOnClickListener(v -> {
            String mealName = mealNameEditText.getText().toString().trim();
            String caloriesText = caloriesEditText.getText().toString().trim();

            if (mealName.isEmpty() || caloriesText.isEmpty()) {
                return;
            }

            int calorieAmount;
            try {
                calorieAmount = Integer.parseInt(caloriesText);
                if (calorieAmount < 0) {
                    return;
                }
            } catch (NumberFormatException e) {
                return;
            }

            if (selectedMealType == null) {
                return;
            }

            if (selectedDay == null) {
                return;
            }

            // Convert selectedMealType (String) to MealType enum
            MealType mealTypeEnum;
            switch (selectedMealType) {
                case "MEAL":
                    mealTypeEnum = MealType.MEAL;
                    break;
                case "SNACK":
                    mealTypeEnum = MealType.SNACK;
                    break;
                case "DRINK":
                    mealTypeEnum = MealType.DRINK;
                    break;
                default:
                    return;
            }

            // Calculate LocalDateTime for the selected day (relative to today)
            LocalDateTime now = LocalDateTime.now();
            int daysToSubtract = 0;
            String todayDay = now.getDayOfWeek().toString();
            for (int i = 0; i < selectDay.length; i++) {
                if (selectDay[i].equalsIgnoreCase(todayDay)) {
                    daysToSubtract = (i - selectDay.length + 1) % 7;
                    break;
                }
            }
            for (int i = 0; i < selectDay.length; i++) {
                if (selectDay[i].equals(selectedDay)) {
                    daysToSubtract += i;
                    break;
                }
            }
            LocalDateTime eatenDate = now.minusDays(daysToSubtract);

            // Create new FoodItem
            FoodItem newFood = new FoodItem();
            newFood.foodName = mealName;
            newFood.calories = calorieAmount;
            newFood.mealType = mealTypeEnum;
            newFood.eatenDate = eatenDate;

            // Add to customFoods and save
            try {
                DataContainer.customFoods.add(newFood);
                DataContainer.SaveData(this);
                Toast.makeText(this, "Food item added: " + mealName + " " + DataContainer.mealTypeToString(mealTypeEnum), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                return;
            }

            // Clear the form
            mealNameEditText.setText("");
            caloriesEditText.setText("");
            mealTypeSpinner.setSelection(0);
            selectDaySpinner.setSelection(0);
        });
    }
}