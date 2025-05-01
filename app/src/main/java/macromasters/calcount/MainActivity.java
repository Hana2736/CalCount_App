package macromasters.calcount;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {

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
            startActivity(intent);
        });
        Button dailyHistoryBtn = findViewById(R.id.dailyhistoryPageBtn);
        dailyHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DailyHistoryPage.class);
            startActivity(intent);
        });
        Button customFoodsBtn = findViewById(R.id.customfoodsPageBtn);
        customFoodsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomFoodsActivity.class);
            startActivity(intent);
        });
        Button settingsBtn = findViewById(R.id.settingsPageBtn);
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsPage.class);
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