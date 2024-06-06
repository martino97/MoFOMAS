package com.example.mofomas.admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.mofomas.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    TextView detailDesc,detailTittle;
    CircleImageView detailImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        detailTittle =findViewById(R.id.detailTittle);
        detailImage =findViewById(R.id.detailImage);
        detailDesc =findViewById(R.id.detailDesc);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            detailTittle.setText(bundle.getString("Tittle"));
            detailDesc.setText(bundle.getString("Amount"));
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}