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

public class PersonalDetailsActivity extends AppCompatActivity {
    private EditText etName, etPhone, etEmail, etAddress;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "PersonalDetailsPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadpersonalDetails();
        Button btnSave = findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name",etName.getText().toString());
                editor.putString("email",etEmail.getText().toString());
                editor.putString("phone",etPhone.getText().toString());
                editor.putString("address",etAddress.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(),"Personal Details updated",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadpersonalDetails() {
        String name = sharedPreferences.getString("name", "");
        if (!name.isEmpty()) {
            etName.setText(name);
        }

        String address = sharedPreferences.getString("address", "");
        if (!address.isEmpty()) {
            etAddress.setText(address);
        }

        String email = sharedPreferences.getString("email", "");
        if (!email.isEmpty()) {
            etEmail.setText(email);
        }

        String phone = sharedPreferences.getString("phone", "");
        if (!phone.isEmpty()) {
            etPhone.setText(phone);
        }

    }
}