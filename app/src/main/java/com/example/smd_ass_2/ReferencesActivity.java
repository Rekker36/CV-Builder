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

public class ReferencesActivity extends AppCompatActivity {
    private EditText etRefName, etRefContact, etRefRelation;
    private LinearLayout referencesContainer;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ReferencesPrefs";
    private static final String REFERENCES_KEY = "references_list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_references);

        etRefName = findViewById(R.id.etRefName);
        etRefContact = findViewById(R.id.etRefContact);
        etRefRelation = findViewById(R.id.etRefRelation);
        Button btnAddReference = findViewById(R.id.btnAddReference);
        referencesContainer = findViewById(R.id.referencesContainer);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved references
        loadReferences();

        btnAddReference.setOnClickListener(v -> addReference());
    }
    private void addReference() {
        String refName = etRefName.getText().toString();
        String refContact = etRefContact.getText().toString();
        String refRelation = etRefRelation.getText().toString();

        if (refName.isEmpty() || refContact.isEmpty() || refRelation.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save reference in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> referencesSet = sharedPreferences.getStringSet(REFERENCES_KEY, new HashSet<>());
        referencesSet.add(refName + " - " + refContact + " (" + refRelation + ")");
        editor.putStringSet(REFERENCES_KEY, referencesSet);
        editor.apply();

        // Add reference to UI
        displayReference(refName + " - " + refContact + " (" + refRelation + ")");

        // Clear input fields
        etRefName.setText("");
        etRefContact.setText("");
        etRefRelation.setText("");

        Toast.makeText(this, "Reference Added!", Toast.LENGTH_SHORT).show();
    }

    private void loadReferences() {
        Set<String> referencesSet = sharedPreferences.getStringSet(REFERENCES_KEY, new HashSet<>());
        for (String reference : referencesSet) {
            displayReference(reference);
        }
    }

    // Display reference in UI with delete button
    private void displayReference(String referenceText) {
        LinearLayout referenceItem = new LinearLayout(this);
        referenceItem.setOrientation(LinearLayout.HORIZONTAL);
        referenceItem.setPadding(10, 10, 10, 10);

        TextView textView = new TextView(this);
        textView.setText(referenceText);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button btnDelete = new Button(this);
        btnDelete.setText("Delete");
        btnDelete.setOnClickListener(v -> removeReference(referenceText, referenceItem));

        referenceItem.addView(textView);
        referenceItem.addView(btnDelete);
        referencesContainer.addView(referenceItem);
    }

    // Remove reference from SharedPreferences and UI
    private void removeReference(String referenceText, View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> referencesSet = sharedPreferences.getStringSet(REFERENCES_KEY, new HashSet<>());
        referencesSet.remove(referenceText);
        editor.putStringSet(REFERENCES_KEY, referencesSet);
        editor.apply();

        referencesContainer.removeView(view);
        Toast.makeText(this, "Reference Removed!", Toast.LENGTH_SHORT).show();
    }
}