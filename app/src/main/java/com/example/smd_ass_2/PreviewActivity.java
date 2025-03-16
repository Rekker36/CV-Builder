package com.example.smd_ass_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class PreviewActivity extends AppCompatActivity {
    ImageView profileImageView;
    TextView nameTextView, emailTextView, phoneTextView, summaryTextView, educationTextView, experienceTextView, referencesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preview);

        // Initialize UI elements
        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        summaryTextView = findViewById(R.id.summaryTextView);
        educationTextView = findViewById(R.id.educationTextView);
        experienceTextView = findViewById(R.id.experienceTextView);
        referencesTextView = findViewById(R.id.referencesTextView);

        // Load data from SharedPreferences
        SharedPreferences personalPrefs = getSharedPreferences("PersonalDetailsPrefs", MODE_PRIVATE);
        SharedPreferences summaryPrefs = getSharedPreferences("SummaryPrefs", MODE_PRIVATE);
        SharedPreferences educationPrefs = getSharedPreferences("EducationPrefs", MODE_PRIVATE);
        SharedPreferences experiencePrefs = getSharedPreferences("ExperiencePrefs", MODE_PRIVATE);
        SharedPreferences profilePrefs = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);

        // Set user details
        nameTextView.setText(personalPrefs.getString("name", "Not Provided"));
        emailTextView.setText(personalPrefs.getString("email", "Not Provided"));
        phoneTextView.setText(personalPrefs.getString("phone", "Not Provided"));
        summaryTextView.setText(summaryPrefs.getString("summary", "Not Provided"));
        educationTextView.setText(educationPrefs.getString("university", "Not Provided"));
        experienceTextView.setText(experiencePrefs.getString("job", "Not Provided"));

        // Load profile picture
        String encodedImage = profilePrefs.getString("profile_image", null);
        if (encodedImage != null) {
            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap profileBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileImageView.setImageBitmap(profileBitmap);
        }
    }
}