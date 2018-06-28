package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BloodBankDrives extends AppCompatActivity {

    public Button adddrive;
    public Button seedrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_drives);

        Intent in=getIntent();
        Bundle ex=in.getExtras();
        final String blooduser=ex.getString("blooduser");

        adddrive=(Button) findViewById(R.id.adddrive);
        seedrive=(Button) findViewById(R.id.seedrive);

        adddrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data4=new Intent(BloodBankDrives.this,AddNewDrive.class);
                Bundle ex=new Bundle();
                ex.putString("blooduser",blooduser);
                data4.putExtras(ex);
                startActivity(data4);
            }
        });

        seedrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data=new Intent(BloodBankDrives.this,ExistingDrives.class);
                Bundle ex1=new Bundle();
                ex1.putString("blooduser",blooduser);
                data.putExtras(ex1);
                startActivity(data);
                finish();


            }
        });

    }
}

