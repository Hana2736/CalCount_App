package macromasters.calcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File;

public class SettingsPage extends AppCompatActivity {

    private static final String PREFS_NAME = "SettingsPrefs";

    private Button eraseHistory, languageButton, customFoodBtn, dailyHistoryBtn, overviewBtn;
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
            insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });

        // Initialize UI elements
        notificationsGroup = findViewById(R.id.notificationsGroup);
        notificationsOn = findViewById(R.id.notificationsOn);
        notificationsOff = findViewById(R.id.notificationsOff);
        eraseHistory = findViewById(R.id.eraseHistoryButton);
        calorieGoalInput = findViewById(R.id.calorieGoalInput);
        languageButton = findViewById(R.id.languageButton);
        customFoodBtn = findViewById(R.id.customFoodBtn);
        dailyHistoryBtn = findViewById(R.id.dailyHistoryBtn);
        overviewBtn = findViewById(R.id.overviewBtn);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved settings
        loadSettings();

        // Set navigation button listeners
        customFoodBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomFoodsActivity.class);
            startActivity(intent);
        });

        dailyHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DailyHistoryPage.class);
            startActivity(intent);
        });

        overviewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, OverviewPage.class);
            startActivity(intent);
        });

        // Set existing settings listeners
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
        new AlertDialog.Builder(this)
                .setTitle("Erase All Data")
                .setMessage("Are you sure you want to erase all settings and food data? This cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        // Clear SharedPreferences
                        sharedPreferences.edit().clear().apply();

                        // Delete savedata.bin file
                        File file = new File(getFilesDir(), DataContainer.SAVE_FILE_NAME);
                        if (file.exists()) {
                            boolean deleted = file.delete();
                            if (!deleted) {
                                Toast.makeText(this, "Failed to delete food data file", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        // Reset DataContainer's customFoods list
                        DataContainer.customFoods.clear();

                        // Reset UI elements
                        calorieGoalInput.setText("");
                        notificationsOn.setChecked(true);

                        // Notify user
                        Toast.makeText(this, "All settings and food data erased", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error erasing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
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