package macromasters.calcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

/**
 * Activity for managing application settings.
 * Allows users to set calorie goals, manage notification preferences,
 * change language (feature pending), and erase all stored data.
 */
public class SettingsPage extends AppCompatActivity {

    private static final String PREFS_NAME = "SettingsPrefs";

    private Button eraseHistory, languageButton, customFoodBtn, dailyHistoryBtn, overviewBtn;
    private EditText calorieGoalInput;
    private RadioButton notificationsOn, notificationsOff;
    private RadioGroup notificationsGroup;

    private SharedPreferences sharedPreferences;

    /**
     * Called when the activity is first created.
     * Initializes UI elements, loads saved settings, and sets up listeners for user interactions.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Applying only top and bottom padding for system bars
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

        calorieGoalInput.setInputType(InputType.TYPE_NULL); // Prevents keyboard from showing on click
        calorieGoalInput.setOnClickListener(v -> showCalorieInputDialog());
        calorieGoalInput.setOnFocusChangeListener((v, hasFocus) -> { // Also show dialog on focus if not using TYPE_NULL
            if (hasFocus) {
                showCalorieInputDialog();
            }
        });
    }

    /**
     * Loads settings from SharedPreferences and updates the UI elements accordingly.
     * Defaults are provided if no settings are found (notifications enabled, 2000 calorie goal).
     */
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

    /**
     * Saves the notification preference (enabled/disabled) to SharedPreferences.
     *
     * @param enabled {@code true} if notifications are to be enabled, {@code false} otherwise.
     */
    private void saveNotificationsSetting(boolean enabled) {
        sharedPreferences.edit().putBoolean("notificationsEnabled", enabled).apply();
    }

    /**
     * Saves the calorie goal to SharedPreferences and updates the input field.
     *
     * @param goal The calorie goal to save.
     */
    private void saveCalorieGoal(int goal) {
        sharedPreferences.edit().putInt("calorieGoal", goal).apply();
        calorieGoalInput.setText(String.valueOf(goal));
        Toast.makeText(this, "Calorie goal saved!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays an AlertDialog to allow the user to input their calorie goal.
     * Validates the input and saves it if it's a valid number.
     */
    private void showCalorieInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Calorie Goal");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(calorieGoalInput.getText()); // Pre-fill with current goal
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String value = input.getText().toString().trim();
            if (!value.isEmpty()) {
                try {
                    int goal = Integer.parseInt(value);
                    if (goal < 0) { // Basic validation
                        Toast.makeText(this, "Goal cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saveCalorieGoal(goal);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Displays a confirmation dialog and then erases all application settings and stored food data.
     * This includes clearing SharedPreferences and deleting the food data save file.
     */
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
                                // Optionally, do not proceed with UI reset if critical data isn't deleted
                                // return;
                            }
                        }

                        // Reset DataContainer's customFoods list
                        if (DataContainer.customFoods != null) {
                            DataContainer.customFoods.clear();
                        }


                        // Reset UI elements to defaults
                        loadSettings(); // This will apply default values as preferences are now cleared

                        // Notify user
                        Toast.makeText(this, "All settings and food data erased", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error erasing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Called when the activity is becoming visible to the user.
     * Followed by {@link #onResume} if the activity comes to the foreground, or {@link #onStop} if it becomes hidden.
     * Loads the latest settings, in case they were changed elsewhere or by a reset.
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadSettings(); // Refresh settings when activity starts/restarts
    }


    /**
     * Called when the activity is no longer visible to the user, because another activity has been resumed and is
     * covering this one. This may happen either because a new activity is being started, an existing one
     * is being brought in front of this one, or this one is being destroyed.
     * <p>
     * In this case, it attempts to save the calorie goal if it has been changed in the EditText.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // It's generally better to save explicitly via a button or when dialog closes.
        // Saving in onPause can be problematic if the activity is paused for reasons other than user navigation.
        // However, if this is the desired behavior:
        String currentGoalText = calorieGoalInput.getText().toString().trim();
        int currentGoalInPrefs = sharedPreferences.getInt("calorieGoal", 2000);

        if (!currentGoalText.isEmpty()) {
            try {
                int goalFromInput = Integer.parseInt(currentGoalText);
                if (goalFromInput != currentGoalInPrefs) { // Only save if different
                    // saveCalorieGoal(goalFromInput); // Decided to remove implicit save onPause
                }
            } catch (NumberFormatException ignored) {
                // If input is not a valid number, maybe revert to saved value or show error
                // For now, just ignoring.
            }
        }
    }
}