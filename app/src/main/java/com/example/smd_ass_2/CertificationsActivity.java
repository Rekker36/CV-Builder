package com.example.smd_ass_2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class CertificationsActivity extends AppCompatActivity {

    private EditText etCertificationName, etIssuingOrganization, etYear;
    private LinearLayout certificationsContainer;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CertificationsPrefs";
    private static final String CERTIFICATIONS_KEY = "certifications_list";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certifications);
        etCertificationName = findViewById(R.id.etCertificationName);
        etIssuingOrganization = findViewById(R.id.etIssuingOrganization);
        etYear = findViewById(R.id.etYear);
        Button btnAddCertification = findViewById(R.id.btnAddCertification);
        certificationsContainer = findViewById(R.id.certificationsContainer);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved certifications
        loadCertifications();

        btnAddCertification.setOnClickListener(v -> addCertification());

    }
    private void addCertification() {
        String certificationName = etCertificationName.getText().toString();
        String organization = etIssuingOrganization.getText().toString();
        String year = etYear.getText().toString();

        if (certificationName.isEmpty() || organization.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save certification in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> certificationsSet = sharedPreferences.getStringSet(CERTIFICATIONS_KEY, new HashSet<>());
        certificationsSet.add(certificationName + " - " + organization + " (" + year + ")");
        editor.putStringSet(CERTIFICATIONS_KEY, certificationsSet);
        editor.apply();

        // Add certification to UI
        displayCertification(certificationName + " - " + organization + " (" + year + ")");

        // Clear input fields
        etCertificationName.setText("");
        etIssuingOrganization.setText("");
        etYear.setText("");

        Toast.makeText(this, "Certification Added!", Toast.LENGTH_SHORT).show();
    }

    private void loadCertifications() {
        Set<String> certificationsSet = sharedPreferences.getStringSet(CERTIFICATIONS_KEY, new HashSet<>());
        if (certificationsSet == null) certificationsSet = new HashSet<>();

        for (String certification : certificationsSet) {
            displayCertification(certification);
        }
    }

    // Display certification in UI with delete button
    private void displayCertification(String certificationText) {
        LinearLayout certificationItem = new LinearLayout(this);
        certificationItem.setOrientation(LinearLayout.HORIZONTAL);
        certificationItem.setPadding(10, 10, 10, 10);

        TextView textView = new TextView(this);
        textView.setText(certificationText);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button btnDelete = new Button(this);
        btnDelete.setText("Delete");
        btnDelete.setOnClickListener(v -> removeCertification(certificationText, certificationItem));

        certificationItem.addView(textView);
        certificationItem.addView(btnDelete);
        certificationsContainer.addView(certificationItem);
    }

    // Remove certification from SharedPreferences and UI
    private void removeCertification(String certificationText, View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> certificationsSet = sharedPreferences.getStringSet(CERTIFICATIONS_KEY, new HashSet<>());
        certificationsSet.remove(certificationText);
        editor.putStringSet(CERTIFICATIONS_KEY, certificationsSet);
        editor.apply();        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        certificationsContainer.removeView(view);
        Toast.makeText(this, "Certification Removed!", Toast.LENGTH_SHORT).show();
    }


}