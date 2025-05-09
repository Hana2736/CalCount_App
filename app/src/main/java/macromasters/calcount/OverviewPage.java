package macromasters.calcount;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
//import android.view.View; // Unused import
//import android.view.ViewGroup; // Unused import
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Activity that provides an overview of recently logged food items.
 * It displays a limited number of the latest food entries and provides navigation to other pages.
 */
public class OverviewPage extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Initializes the UI, sets up navigation, and displays a summary of recent food items.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge

        // Set status bar and navigation bar to transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightNavigationBars(false);

        setContentView(R.layout.activity_overview_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Apply system window insets to the main view
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
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

        DataContainer.sort();

        LinearLayout scrollList = findViewById(R.id.linearlayout_overview);

        LayoutInflater inflater = getLayoutInflater();
        int limit = 0;
        for (FoodItem eatenFood : DataContainer.customFoods) {
            if (limit >= 5) break; // Changed to >= to correctly limit to 5 items (0,1,2,3,4)
            inflater.inflate(R.layout.slice_single_food_item, scrollList, true);
            // Ensure child count matches the expected index
            if (scrollList.getChildCount() > limit) {
                Button mealTypeIcon = scrollList.getChildAt(limit).findViewById(R.id.mealTypeIcon_slice);
                Button mealDesc = scrollList.getChildAt(limit).findViewById(R.id.mealDesc_slice);

                mealTypeIcon.setText(DataContainer.mealTypeToString(eatenFood.mealType));
                mealDesc.setText(eatenFood.foodName + "\n" + eatenFood.calories + " calories");
            }
            limit++;
        }

    }
}