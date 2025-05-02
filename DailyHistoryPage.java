package macromasters.calcount;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DailyHistoryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set status bar and navigation bar to transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightNavigationBars(false);

        setContentView(R.layout.activity_daily_history_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0,
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });

        // Initialize buttons
        Button overviewBtn = findViewById(R.id.overview_history);
        Button settingsBtn = findViewById(R.id.settings_history);
        Button customFoodsBtn = findViewById(R.id.customFoodBtn_history);

        // Set button click listeners
        overviewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, OverviewPage.class);
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

        // Sort the food items by date (newest first)
        DataContainer.sort();

        // Get the LinearLayout to populate with daily history
        LinearLayout historyLayout = findViewById(R.id.history_layout);

        // Group food items by day
        Map<String, List<FoodItem>> foodByDay = new HashMap<>();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy"); // e.g., "Monday, 30 Apr 2025"

        for (FoodItem item : DataContainer.customFoods) {
            String dayKey = item.eatenDate.format(dayFormatter);
            foodByDay.computeIfAbsent(dayKey, k -> new LinkedList<>()).add(item);
        }

        // Inflate and populate the layout for each day
        LayoutInflater inflater = getLayoutInflater();
        for (Map.Entry<String, List<FoodItem>> entry : foodByDay.entrySet()) {
            String day = entry.getKey();
            List<FoodItem> foods = entry.getValue();

            // Add day header
            TextView dayHeader = new TextView(this);
            dayHeader.setText(day);
            dayHeader.setTextSize(20);
            dayHeader.setTextColor(Color.BLACK);
            dayHeader.setBackgroundColor(Color.parseColor("#D3C8E5"));
            dayHeader.setPadding(16, 16, 16, 8);
            historyLayout.addView(dayHeader);

            // Add food items for the day
            for (FoodItem food : foods) {
                View foodView = inflater.inflate(R.layout.slice_single_food_item, historyLayout, false);

                Button mealTypeIcon = foodView.findViewById(R.id.mealTypeIcon_slice);
                Button mealDesc = foodView.findViewById(R.id.mealDesc_slice);

                mealTypeIcon.setText(DataContainer.mealTypeToString(food.mealType));
                mealDesc.setText(food.foodName + "\n" + food.calories + " cal");

                historyLayout.addView(foodView);
            }
        }
    }
}