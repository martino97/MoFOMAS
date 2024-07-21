package com.example.mofomas;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PaymentMethods extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private RadioGroup radioGroup;
    private RadioButton radioAirtelMoney, radioKPay, radioTigoPesa, radioHalopesa, radioMpesa;
    private Button payButton;
    private TextView animatedText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        radioGroup = findViewById(R.id.radioGroup);
        radioAirtelMoney = findViewById(R.id.radioAirtelMoney);
        radioKPay = findViewById(R.id.radioKPay);
        radioTigoPesa = findViewById(R.id.radioTigoPesa);
        radioMpesa = findViewById(R.id.radioMpesa);
        radioHalopesa = findViewById(R.id.radioHalopesa);
        payButton = findViewById(R.id.payButton);
        animatedText = findViewById(R.id.animatedText);

        startTextAnimation();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioAirtelMoney) {
                    Toast.makeText(PaymentMethods.this, "Airtel Money selected", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.radioKPay) {
                    Toast.makeText(PaymentMethods.this, "K-Pay selected", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.radioTigoPesa) {
                    Toast.makeText(PaymentMethods.this, "Tigo Pesa selected", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.radioMpesa) {
                    Toast.makeText(PaymentMethods.this, "Mpesa selected", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.radioHalopesa) {
                    Toast.makeText(PaymentMethods.this, "Halopesa selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(PaymentMethods.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                } else {
                    String ussdCode = "";
                    if (selectedId == R.id.radioAirtelMoney) {
                        ussdCode = "*150*60#";
                    } else if (selectedId == R.id.radioKPay) {
                        ussdCode = "*150*88#";
                    } else if (selectedId == R.id.radioTigoPesa) {
                        ussdCode = "*150*01#";
                    } else if (selectedId == R.id.radioMpesa) {
                        ussdCode = "*150*00#";
                    } else if (selectedId == R.id.radioHalopesa) {
                        ussdCode = "*150*88#";
                    }
                    if (!ussdCode.isEmpty()) {
                        makeUssdCall(ussdCode);
                    }
                }
            }
        });

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startTextAnimation() {
        ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(animatedText, "translationX",
                getWindow().getDecorView().getWidth(),
                -animatedText.getWidth());
        moveAnimator.setDuration(15000); // 15 seconds for one complete move
        moveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        moveAnimator.setInterpolator(new LinearInterpolator());
        moveAnimator.start();
    }

    private void makeUssdCall(String ussdCode) {
        if (ContextCompat.checkSelfPermission(PaymentMethods.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentMethods.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            String ussdEncoded = ussdCode.replace("#", Uri.encode("#"));
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdEncoded));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}