package com.example.mofomas.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.mofomas.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {
    TextView detailDesc,detailTittle;
    ImageView detailImage;
    FloatingActionButton deleteMenu, UpdateBtn;
    String key = "";
    String imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        detailTittle =findViewById(R.id.detailTittle);
        detailImage =findViewById(R.id.detailImage);
        UpdateBtn =findViewById(R.id.update);
        detailDesc =findViewById(R.id.detailDesc);
        deleteMenu = findViewById(R.id.delete);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            detailTittle.setText(bundle.getString("Tittle"));
            detailDesc.setText(bundle.getString("Amount"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FoodItems");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),UploadItem.class));
                        finish();

                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,Updates.class)
                        .putExtra("Tittle",detailTittle.getText().toString())
                        .putExtra("Amount",detailDesc.getText().toString())
                        .putExtra("Image",imageUrl)
                        .putExtra("Key",key);
                startActivity(intent);
            }
        });
    }
}