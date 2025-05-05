package macromasters.calcount;

import static macromasters.calcount.R.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Scanner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import macromasters.calcount.MainActivity;

public class SettingsPage extends AppCompatActivity {

    Scanner scan = new Scanner(System.in);
    private static final String PREFS_NAME = "SettingsPrefs";

    private Button eraseHistory, languageButton;
    private EditText calorieGoalInput;
    private RadioButton notificationsOn, notificationsOff;
    private RadioGroup notificationsGroup;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notificationsGroup = findViewById(R.id.notificationsGroup);
        notificationsOn = findViewById(R.id.notificationsOn);
        notificationsOff = findViewById(R.id.notificationsOff);
        eraseHistory = findViewById(R.id.eraseHistoryButton);
        calorieGoalInput = findViewById(R.id.calorieGoalInput);
        languageButton = findViewById(R.id.languageButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadSettings();

        notificationsOn.setOnClickListener(v -> saveNotificationsSetting(true));
        notificationsOff.setOnClickListener(v -> saveNotificationsSetting(false));
        eraseHistory.setOnClickListener(v -> eraseAllSettings());
        languageButton.setOnClickListener(v -> Toast.makeText(this, "Language change feature coming soon!", Toast.LENGTH_SHORT).show());

        calorieGoalInput.setInputType(InputType.TYPE_NULL);
        calorieGoalInput.setOnClickListener(v -> showCalorieInputDialog());
    }


    private void loadSettings() {
        boolean notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true);

        if (notificationsEnabled) {
            notificationsOn.setChecked(true);
        } else {
            notificationsOff.setChecked(true);
        }
        int savedGoal = sharedPreferences.getInt("calorieGoal", 2000);

        calorieGoalInput.setText(String.valueOf(savedGoal));
    }

    private void saveNotificationsSetting(boolean enabled) {
        sharedPreferences.edit().putBoolean("notificationsEnabled", enabled).apply();
    }

    private void saveCalorieGoal(int goal) {
        sharedPreferences.edit().putInt("calorieGoal", goal).apply();
        calorieGoalInput.setText(String.valueOf(goal));
        Toast.makeText(this, "Calorie goal saved!", Toast.LENGTH_SHORT).show();
    }

    private void showCalorieInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Calorie Goal");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String value = input.getText().toString().trim();
            if (!value.isEmpty()) {
                try {
                    int goal = Integer.parseInt(value);
                    saveCalorieGoal(goal);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void eraseAllSettings() {
        sharedPreferences.edit().clear().apply();
        calorieGoalInput.setText("");
        notificationsOn.setChecked(true);
        Toast.makeText(this, "History erased", Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        String input = calorieGoalInput.getText().toString().trim();
        if (!input.isEmpty()) {
            try {
                int goal = Integer.parseInt(input);
                saveCalorieGoal(goal);
            } catch (NumberFormatException ignored) {
            }
        }


    }
}