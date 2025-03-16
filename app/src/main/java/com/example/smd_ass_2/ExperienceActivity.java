package com.example.smd_ass_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;
import java.util.Set;

public class ExperienceActivity extends AppCompatActivity {
    private EditText etCompany, etPosition, etYears;
    private LinearLayout experienceContainer;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ExperiencePrefs";
    private static final String EXPERIENCE_KEY = "experience_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etCompany = findViewById(R.id.etCompany);
        etPosition = findViewById(R.id.etPosition);
        etYears = findViewById(R.id.etYears);
        Button btnAdd = findViewById(R.id.btnAdd);
        experienceContainer = findViewById(R.id.experienceContainer);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved experiences
        loadExperiences();

        btnAdd.setOnClickListener(v -> addExperience());
    }

    // Add new job experience
    private void addExperience() {
        String company = etCompany.getText().toString();
        String position = etPosition.getText().toString();
        String years = etYears.getText().toString();

        if (company.isEmpty() || position.isEmpty() || years.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the new experience in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> experienceSet = sharedPreferences.getStringSet(EXPERIENCE_KEY, new HashSet<>());
        experienceSet.add(company + " - " + position + " (" + years + " years)");
        editor.putStringSet(EXPERIENCE_KEY, experienceSet);
        editor.apply();

        // Add experience to UI
        displayExperience(company + " - " + position + " (" + years + " years)");

        // Clear input fields
        etCompany.setText("");
        etPosition.setText("");
        etYears.setText("");

        Toast.makeText(this, "Experience Added!", Toast.LENGTH_SHORT).show();
    }

    // Load existing experiences from SharedPreferences
    private void loadExperiences() {
        Set<String> experienceSet = sharedPreferences.getStringSet(EXPERIENCE_KEY, new HashSet<>());
        for (String experience : experienceSet) {
            displayExperience(experience);
        }
    }

    // Display experience in UI with delete button
    private void displayExperience(String experienceText) {
        LinearLayout experienceItem = new LinearLayout(this);
        experienceItem.setOrientation(LinearLayout.HORIZONTAL);
        experienceItem.setPadding(10, 10, 10, 10);

        TextView textView = new TextView(this);
        textView.setText(experienceText);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button btnDelete = new Button(this);
        btnDelete.setText("Delete");
        btnDelete.setOnClickListener(v -> removeExperience(experienceText, experienceItem));

        experienceItem.addView(textView);
        experienceItem.addView(btnDelete);
        experienceContainer.addView(experienceItem);
    }

    // Remove experience from UI and SharedPreferences
    private void removeExperience(String experienceText, View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> experienceSet = sharedPreferences.getStringSet(EXPERIENCE_KEY, new HashSet<>());
        experienceSet.remove(experienceText);
        editor.putStringSet(EXPERIENCE_KEY, experienceSet);
        editor.apply();

        experienceContainer.removeView(view);
        Toast.makeText(this, "Experience Removed!", Toast.LENGTH_SHORT).show();
    }
}