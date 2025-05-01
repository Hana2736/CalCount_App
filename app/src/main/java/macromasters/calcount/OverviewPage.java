package macromasters.calcount;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class OverviewPage extends AppCompatActivity {

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
            if (limit > 10) break;
            inflater.inflate(R.layout.slice_single_food_item, scrollList, true);
            Button mealTypeIcon = scrollList.getChildAt(limit).findViewById(R.id.mealTypeIcon_slice);
            Button mealDesc = scrollList.getChildAt(limit).findViewById(R.id.mealDesc_slice);


            mealTypeIcon.setText(DataContainer.mealTypeToString(eatenFood.mealType));
            mealDesc.setText(eatenFood.foodName + "\n" + eatenFood.calories + " calories");
            limit++;
        }

    }
}
