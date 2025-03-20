package macromasters.calcount;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }
}