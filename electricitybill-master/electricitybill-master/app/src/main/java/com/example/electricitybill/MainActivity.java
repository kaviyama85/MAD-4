package com.example.electricitybill;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    EditText etUnits, etPhone;
    Button btnCalculate;
    TextView tvDisplayResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing variables
        etUnits = findViewById(R.id.etUnits);
        etPhone = findViewById(R.id.etPhone);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvDisplayResult = findViewById(R.id.tvDisplayResult);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitString = etUnits.getText().toString();
                String phoneNumber = etPhone.getText().toString();

                if (unitString.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double units = Double.parseDouble(unitString);
                double rate = 7.50; // Assume 7.50 per unit
                double totalBill = units * rate;

                String finalMessage = "Units: " + units + "\nTotal Amount: ₹" + totalBill;

                // 1. Create and show appropriate alert message
                showBillDialog(phoneNumber, finalMessage, totalBill);
            }
        });
    }

    private void showBillDialog(final String phone, final String message, double amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bill Summary");
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Send SMS", (dialog, which) -> {
            sendSMS(phone, message);
            tvDisplayResult.setText("Bill: ₹" + amount + " (Sent)");
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendSMS(String phoneNumber, String message) {
        // Check for SMS Permission at runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "SMS Sent successfully!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }
}