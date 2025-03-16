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

public class SummaryActivity extends AppCompatActivity {
    private EditText etSummary;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "SummaryPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        etSummary=findViewById(R.id.etSummary);
        Button btnSave = findViewById(R.id.btnSave);
        String temp = sharedPreferences.getString("summary","");

        if (temp.length()>0){
            etSummary.setText(temp);
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("summary",etSummary.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(),"Summary Update",Toast.LENGTH_SHORT).show();
            }
        });
    }
}