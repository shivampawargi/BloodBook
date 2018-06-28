package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.os.Handler;

public class StockList extends AppCompatActivity {

    public Button opos;
    public Button oneg;
    public Button apos;
    public Button aneg;
    public Button bpos;
    public Button bneg;
    public Button abpos;
    public Button abneg;
    public Button expired;
    long diff3=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        Intent intnt=getIntent();
        Bundle bgrp=intnt.getExtras();
        final String blooduser=bgrp.getString("blooduser");

        opos=(Button) findViewById(R.id.opos);

        opos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data2= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun1=new Bundle();
                bun1.putString("blooduser",blooduser);
                bun1.putString("bg","O+");
                data2.putExtras(bun1);
                startActivity(data2);
            }
        });

        oneg=(Button) findViewById(R.id.oneg);

        oneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data3= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun2=new Bundle();
                bun2.putString("blooduser",blooduser);
                bun2.putString("bg","O-");
                data3.putExtras(bun2);
                startActivity(data3);
            }
        });

        apos=(Button) findViewById(R.id.apos);

        apos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data4= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun1=new Bundle();
                bun1.putString("blooduser",blooduser);
                bun1.putString("bg","A+");
                data4.putExtras(bun1);
                startActivity(data4);
            }
        });

        aneg=(Button) findViewById(R.id.aneg);

        aneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data5= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun1=new Bundle();
                bun1.putString("blooduser",blooduser);
                bun1.putString("bg","A-");
                data5.putExtras(bun1);
                startActivity(data5);
            }
        });

        bpos=(Button) findViewById(R.id.bpos);

        bpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data6= new Intent(StockList.this,BloodGroupList.class);
                Bundle bpos=new Bundle();
                bpos.putString("blooduser",blooduser);
                bpos.putString("bg","B+");
                data6.putExtras(bpos);
                startActivity(data6);
            }
        });

        bneg=(Button) findViewById(R.id.bneg);

        bneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data7= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun1=new Bundle();
                bun1.putString("blooduser",blooduser);
                bun1.putString("bg","B-");
                data7.putExtras(bun1);
                startActivity(data7);
            }
        });

        abpos=(Button) findViewById(R.id.abpos);

        abpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data8= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun1=new Bundle();
                bun1.putString("blooduser",blooduser);
                bun1.putString("bg","AB+");
                data8.putExtras(bun1);
                startActivity(data8);
            }
        });

        abneg=(Button) findViewById(R.id.abneg);

        abneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data9= new Intent(StockList.this,BloodGroupList.class);
                Bundle bun1=new Bundle();
                bun1.putString("blooduser",blooduser);
                bun1.putString("bg","AB-");
                data9.putExtras(bun1);
                startActivity(data9);
            }
        });

        expired=(Button) findViewById(R.id.expired);

        expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Updating Values", Toast.LENGTH_SHORT).show();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent data7= new Intent(StockList.this,AboutToExpire.class);
                        Bundle bposexp=new Bundle();
                        bposexp.putString("blooduser",blooduser);
                        data7.putExtras(bposexp);
                        startActivity(data7);

                    }
                }, 1000);

            }
        });


    }
}
