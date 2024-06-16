package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mofomas.admin.AddItems;
import com.example.mofomas.admin.ViewPackage;

public class AdminDashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        // Find the "Add Item" CardView by its ID
        CardView addItemCardView = findViewById(R.id.cardViewAddMenu);
        CardView packageCardView = findViewById(R.id.packageId);
        CardView view = findViewById(R.id.viewOrder);

        // Set click listener for the "Add Item" CardView
        addItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddItemActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AddItems.class);
                startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddItemActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewOrder.class);
                startActivity(intent);
            }
        });

        // Set click listener for the "Package" CardView
        packageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewPackageActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewPackage.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}