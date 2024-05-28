package com.example.autopark;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.report), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textViewCared = findViewById(R.id.textViewCared);
        TextView textViewCanceled = findViewById(R.id.textViewCanceled);
        TextView textViewSalon = findViewById(R.id.textViewSalon);

        Bundle extras = getIntent().getExtras();

        textViewCared.setText(String.format("%s %d", textViewCared.getText(), extras.getInt("cared")));
        textViewCanceled.setText(String.format("%s %d", textViewCanceled.getText(), extras.getInt("canceled")));
        textViewSalon.setText(String.format("%s %d", textViewSalon.getText(), extras.getInt("salon")));

        Button buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(view -> {
            finish();
        });
    }
}