package com.example.smd_ass_2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class ExportPDFActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private SharedPreferences personalPrefs, summaryPrefs, educationPrefs, experiencePrefs, certPrefs, refPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_export_pdfactivity);
        Button btnGeneratePDF = findViewById(R.id.btnGeneratePDF);
        btnGeneratePDF.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                generatePDF();
            } else {
                requestStoragePermission();
            }
        });
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void generatePDF() {
        // Load stored data from SharedPreferences
        personalPrefs = getSharedPreferences("PersonalDetailsPrefs", MODE_PRIVATE);
        summaryPrefs = getSharedPreferences("SummaryPrefs", MODE_PRIVATE);
        educationPrefs = getSharedPreferences("EducationPrefs", MODE_PRIVATE);
        experiencePrefs = getSharedPreferences("ExperiencePrefs", MODE_PRIVATE);
        certPrefs = getSharedPreferences("CertificationsPrefs", MODE_PRIVATE);
        refPrefs = getSharedPreferences("ReferencesPrefs", MODE_PRIVATE);
        SharedPreferences profilePrefs = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);

        // Retrieve Personal Details
        String name = personalPrefs.getString("name", "Not Provided");
        String email = personalPrefs.getString("email", "Not Provided");
        String phone = personalPrefs.getString("phone", "Not Provided");
        String address = personalPrefs.getString("address", "Not Provided");

        // Retrieve Summary
        String summary = summaryPrefs.getString("summary", "Not Provided");

        // Retrieve Education Details
        String degree = educationPrefs.getString("degree", "Not Provided");
        String university = educationPrefs.getString("university", "Not Provided");
        String gradYear = educationPrefs.getString("graduation_year", "Not Provided");

        // Retrieve Experience (Multiple Entries)
        Set<String> experienceSet = experiencePrefs.getStringSet("experience_list", new HashSet<>());

        // Retrieve Certifications (Multiple Entries)
        Set<String> certSet = certPrefs.getStringSet("certifications_list", new HashSet<>());

        // Retrieve References (Multiple Entries)
        Set<String> refSet = refPrefs.getStringSet("references_list", new HashSet<>());

        // Retrieve Profile Picture (Base64 Encoded)
        String encodedImage = profilePrefs.getString("profile_image", null);
        Bitmap profileBitmap = null;
        if (encodedImage != null) {
            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            profileBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }

        // Create PDF Document
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(16);

        int x = 50, y = 50;

        // Draw Profile Picture (If Available)
        if (profileBitmap != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(profileBitmap, 100, 100, true);
            canvas.drawBitmap(scaledBitmap, x + 200, y, null);
            y += 120;
        }

        // PDF Content Formatting
        canvas.drawText("Curriculum Vitae", x + 180, y, paint);
        y += 30;

        // Personal Details
        paint.setTextSize(14);
        canvas.drawText("Personal Details:", x, y, paint);
        y += 20;
        canvas.drawText("Name: " + name, x, y, paint);
        y += 20;
        canvas.drawText("Email: " + email, x, y, paint);
        y += 20;
        canvas.drawText("Phone: " + phone, x, y, paint);
        y += 20;
        canvas.drawText("Address: " + address, x, y, paint);
        y += 30;

        // Summary
        canvas.drawText("Summary:", x, y, paint);
        y += 20;
        for (String line : summary.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += 20;
        }
        y += 30;

        // Education
        canvas.drawText("Education:", x, y, paint);
        y += 20;
        canvas.drawText(degree + " - " + university + " (" + gradYear + ")", x, y, paint);
        y += 30;

        // Experience
        if (!experienceSet.isEmpty()) {
            canvas.drawText("Experience:", x, y, paint);
            y += 20;
            for (String exp : experienceSet) {
                canvas.drawText(exp, x, y, paint);
                y += 20;
            }
            y += 30;
        }

        // Certifications
        if (!certSet.isEmpty()) {
            canvas.drawText("Certifications:", x, y, paint);
            y += 20;
            for (String cert : certSet) {
                canvas.drawText(cert, x, y, paint);
                y += 20;
            }
            y += 30;
        }

        // References
        if (!refSet.isEmpty()) {
            canvas.drawText("References:", x, y, paint);
            y += 20;
            for (String ref : refSet) {
                canvas.drawText(ref, x, y, paint);
                y += 20;
            }
        }

        // Finish PDF Creation
        pdfDocument.finishPage(page);
        savePDF(pdfDocument);
        pdfDocument.close();
    }

    private void savePDF(PdfDocument pdfDocument) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore for Android 10+
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "CV.pdf");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), values);

            try {
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                pdfDocument.writeTo(outputStream);
                outputStream.close();
                Toast.makeText(this, "PDF Saved in Documents", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Saving PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            // For Android 9 and below
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "CV.pdf");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
                fileOutputStream.close();
                Toast.makeText(this, "PDF Saved in Downloads", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Saving PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }


}