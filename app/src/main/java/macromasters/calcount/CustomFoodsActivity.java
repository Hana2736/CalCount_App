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

public class CustomFoodsActivity extends AppCompatActivity {

    // Meal type selections
    private Spinner mealTypeSpinner;
    private String[] mealType = {"MEAL", "SNACK", "DRINK"};

    // Day selections
    private Spinner selectDaySpinner;
    private String[] selectDay = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set status bar and navigation bar to transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightNavigationBars(false);

        setContentView(R.layout.activity_custom_foods);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Apply system window insets to the main view
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
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMeal = mealType[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = selectDay[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText mealNameEditText = findViewById(R.id.Meal_Name);
        EditText caloriesEditText = findViewById(R.id.Calorie_Amount);
        Button addFoodButton = findViewById(R.id.Submit_Button);

        addFoodButton.setOnClickListener(v -> {
            String mealName = mealNameEditText.getText().toString().trim();
            String caloriesText = caloriesEditText.getText().toString().trim();

            if (mealName.isEmpty() || caloriesText.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int calorieAmount;
            try {
                calorieAmount = Integer.parseInt(caloriesText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid calorie amount", Toast.LENGTH_SHORT).show();
                return;
            }

            String mealType = mealTypeSpinner.getSelectedItem().toString();
            String eatenDate = selectDaySpinner.getSelectedItem().toString();

            FoodItem newFood = new FoodItem();
            newFood.foodName = mealName;
            newFood.calories = calorieAmount;
            newFood.mealType = mealType;
            newFood.eatenDate = LocalDateTime.parse(eatenDate);

            DataContainer.customFoods.add(newFood);
            DataContainer.SaveData(this);

            Toast.makeText(this, "Food item added: " + mealName, Toast.LENGTH_SHORT).show();

    })
;}
}
