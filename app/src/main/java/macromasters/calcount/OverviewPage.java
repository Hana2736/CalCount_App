package macromasters.calcount;

import android.graphics.Color;
import android.os.Bundle;

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
    }
}
