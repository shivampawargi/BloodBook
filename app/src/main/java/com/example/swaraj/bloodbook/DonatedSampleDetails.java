package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DonatedSampleDetails extends AppCompatActivity {

    public TextView name;
    public TextView date;
    public Button ok;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_sample_details);

        Intent data5=getIntent();

        Bundle extras = data5.getExtras();
        final String samplesample = extras.getString("samplename");
        final String blooduser=extras.getString("blooduser");
        final String donor=extras.getString("donor");

        ok=(Button) findViewById(R.id.ok);

        name=(TextView) findViewById(R.id.name);
        String name1="ID : "+ samplesample + ".";
        name.setText(name1);

        date=(TextView) findViewById(R.id.date);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = new Date();
        String dates="Date : "+dateFormat.format(date1);
        date.setText(dates);


        Map<String, Object> donated = new HashMap<>();
        donated.put("BloodBankdeposited",blooduser);
        donated.put("Date",new Date());
        donated.put("DonorName",donor);

        db.collection("donorHistory").add(donated).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Added to donorHistory","done");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data6=new Intent(DonatedSampleDetails.this,BloodBankHomeList.class);
                Bundle extras=new Bundle();
                extras.putString("blooduser",blooduser);
                data6.putExtras(extras);
                data6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(data6);
            }
        });
    }
}
