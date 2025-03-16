package com.example.smd_ass_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EducationActivity extends AppCompatActivity {
    private EditText etDegree, etUniversity, etGraduationYear;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "EducationPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        etDegree = findViewById(R.id.etDegree);
        etUniversity = findViewById(R.id.etName);
        etGraduationYear = findViewById(R.id.etGraduationYear);
        Button btnSave = findViewById(R.id.save);

        String degree = sharedPreferences.getString("degree", "");
        if (!degree.isEmpty()) {
            etDegree.setText(degree);
        }

        String university = sharedPreferences.getString("university", "");
        if (!university.isEmpty()) {
            etUniversity.setText(university);
        }

        String graduationYear = sharedPreferences.getString("graduation_year", "");
        if (!graduationYear.isEmpty()) {
            etGraduationYear.setText(graduationYear);
        }

        // Load saved education details

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("degree", etDegree.getText().toString());
                editor.putString("university", etUniversity.getText().toString());
                editor.putString("graduation_year", etGraduationYear.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(), "Education Details Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}