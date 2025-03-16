package com.example.smd_ass_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        Button btnProfilePic = findViewById(R.id.profile_pic);
        Button btnPersonalDetails = findViewById(R.id.per_det);
        Button btnSummary = findViewById(R.id.summ);
        Button btnEducation = findViewById(R.id.edu);
        Button btnExperience = findViewById(R.id.exp);
        Button btnCertifications = findViewById(R.id.cert);
        Button btnReferences = findViewById(R.id.references);

        btnProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ProfileActivity.class);
            }
        });
        btnPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivity(PersonalDetailsActivity.class);
            }
        });
        btnSummary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivity(SummaryActivity.class);
            }
        });
        btnEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EducationActivity.class);
            }
        });

        btnExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ExperienceActivity.class);
            }
        });

        btnCertifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CertificationsActivity.class);
            }
        });

        btnReferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ReferencesActivity.class);
            }
        });
        Button btnExportPDF = findViewById(R.id.btnExportPDF);
        btnExportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ExportPDFActivity.class);
            }
        });

    }
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
}