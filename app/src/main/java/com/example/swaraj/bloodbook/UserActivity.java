package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UserActivity extends AppCompatActivity {

    ImageButton b1, b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        b1 = (ImageButton) findViewById(R.id.donate);
        b2 = (ImageButton) findViewById(R.id.receive);
    }

    public  void donor_click(View view)
    {
        Intent i = new Intent(UserActivity.this, DonorLogin.class);
        startActivity(i);
    }

    public  void receiver_click(View view)
    {
        Intent i = new Intent(UserActivity.this, ReceiverActivity.class);
        startActivity(i);
    }

}
