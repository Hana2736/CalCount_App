package macromasters.calcount;

import android.content.Intent;
import android.os.Bundle;
//import android.provider.ContactsContract; // Unused import
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import java.time.LocalDateTime; // Unused import for example data

/**
 * The main entry point of the application.
 * This activity provides navigation to other parts of the application
 * such as Overview, Daily History, Custom Foods, and Settings.
 * It also initializes and loads application data.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Initializes the UI, sets up navigation button listeners,
     * initializes the data container, and loads any saved food history.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button overviewBtn = findViewById(R.id.overviewPageBtn);
        overviewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, OverviewPage.class);
            new OverviewPage().onCreate(savedInstanceState);
            startActivity(intent);
        });
        Button dailyHistoryBtn = findViewById(R.id.dailyhistoryPageBtn);
        dailyHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DailyHistoryPage.class);
            new DailyHistoryPage().onCreate(savedInstanceState);;
            startActivity(intent);
        });
        Button customFoodsBtn = findViewById(R.id.customfoodsPageBtn);
        customFoodsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomFoodsActivity.class);
            new CustomFoodsActivity().onCreate(savedInstanceState);;
            startActivity(intent);
        });
        Button settingsBtn = findViewById(R.id.settingsPageBtn);
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsPage.class);
            new SettingsPage().onCreate(savedInstanceState);
            startActivity(intent);
        });

        //Create the food container
        DataContainer.setup();

        /*
        FoodItem item0 = new FoodItem();
        item0.calories = 800;
        item0.foodName = "Chicken Sandwich";
        item0.mealType = MealType.MEAL;
        item0.eatenDate = LocalDateTime.now().minusHours(8);
        DataContainer.customFoods.add(item0);

       FoodItem item1 = new FoodItem();
        item1.calories = 200;
        item1.foodName = "Snickers Bar";
        item1.mealType = MealType.SNACK;
        item1.eatenDate = LocalDateTime.now().minusHours(12);
        DataContainer.customFoods.add(item1);

        FoodItem item2 = new FoodItem();
        item2.calories = 50;
        item2.foodName = "Orange Juice";
        item2.mealType = MealType.DRINK;
        item2.eatenDate = LocalDateTime.now().minusHours(13);
        DataContainer.customFoods.add(item2);*/

        //Load the saved food history from storage
        DataContainer.LoadData(this);

        // DataContainer.SaveData(this);


    }
}