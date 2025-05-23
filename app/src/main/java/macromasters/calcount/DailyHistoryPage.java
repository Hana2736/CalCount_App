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

/**
 * Activity to display the daily history of food items logged by the user.
 * Food items are grouped by day and displayed in a scrollable list.
 */
public class DailyHistoryPage extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Initializes the UI elements, sets up navigation buttons, and populates the history list.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
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

        // Initial population of history
        populateHistory();
    }

    /**
     * Called when the activity will start interacting with the user.
     * At this point your activity is at the top of the activity stack,
     * with user input going to it.
     * Refreshes the history list.
     */
    @Override
    protected void onResume() {
        super.onResume();
        populateHistory();
    }

    /**
     * Populates the history layout with food items.
     * Food items are sorted by date, grouped by day, and displayed.
     */
    private void populateHistory() {
        // Clear existing views
        LinearLayout historyLayout = findViewById(R.id.history_layout);
        historyLayout.removeAllViews();

        // Sort the food items by date (newest first)
        DataContainer.sort();

        // Group food items by day
        Map<String, List<FoodItem>> foodByDay = new HashMap<>();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yy");

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