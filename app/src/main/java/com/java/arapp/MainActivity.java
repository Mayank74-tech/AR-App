package com.java.arapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Spinner drillSpinner;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle window insets for proper layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupDrillSpinner();
        setupStartButton();
    }

    private void initializeViews() {
        drillSpinner = findViewById(R.id.drillSpinner);
        startButton = findViewById(R.id.startButton);
    }

    private void setupDrillSpinner() {
        String[] drills = {
                getString(R.string.drill_1),
                getString(R.string.drill_2),
                getString(R.string.drill_3)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                drills
        );
        drillSpinner.setAdapter(adapter);
    }

    private void setupStartButton() {
        startButton.setOnClickListener(v -> {
            try {
                int selectedDrill = drillSpinner.getSelectedItemPosition();

                // Validate selection
                if (selectedDrill < 0 || selectedDrill > 2) {
                    Toast.makeText(this, R.string.invalid_drill_selection, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Launch AR Activity
                Intent intent = new Intent(MainActivity.this, ARActivity.class);
                intent.putExtra("selectedDrill", selectedDrill);
                startActivity(intent);

                // Optional animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } catch (Exception e) {
                Toast.makeText(this, R.string.error_launching_ar, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset spinner selection when returning
        drillSpinner.setSelection(0);
    }
}