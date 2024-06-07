package com.example.mofomas.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.mofomas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Updates extends AppCompatActivity {
    CircleImageView imageView;
    Button update;
    TextInputLayout updateTittle, updateCos;
    String tittle, desc;
    String imageUrl;
    String key, oldImageUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_updates);

        update = findViewById(R.id.updateBtn);
        updateTittle = findViewById(R.id.updateName);
        updateCos = findViewById(R.id.updateCost);
        imageView = findViewById(R.id.updateImage);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            imageView.setImageURI(uri);

                        } else{
                            Toast.makeText(Updates.this, "No Image Is Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Glide.with(Updates.this).load(bundle.getString("Image")).into(imageView);
            updateTittle.getEditText().setText(bundle.getString("Tittle"));
            updateCos.getEditText().setText(bundle.getString("Amount"));
            key = bundle.getString("Key");
            oldImageUrl = bundle.getString("Image");

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodItems").child(key);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(Updates.this,AddItems.class);
                startActivity(intent);
            }
        });
    }
    public  void saveData()
    {
        storageReference = FirebaseStorage.getInstance().getReference().child("FoodImages").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(Updates.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog alertDialog =builder.create();
        alertDialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uriImage = uriTask.getResult();
                imageUrl = uriImage.toString();
                updateData();
                alertDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.dismiss();
            }
        });
    }
    public  void updateData()
    {
        tittle = updateTittle.getEditText().getText().toString().trim();
        desc = updateCos.getEditText().getText().toString().trim();

        DataClass dataClass = new DataClass(tittle, desc, imageUrl);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                    storage.delete();
                    Toast.makeText(Updates.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Updates.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}