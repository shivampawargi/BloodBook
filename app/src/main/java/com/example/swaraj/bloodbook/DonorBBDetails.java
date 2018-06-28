package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonorBBDetails extends AppCompatActivity {

    ListView listView;
    ArrayList<String> bloodbanks;
    ArrayList<String> bbid;
    FirebaseFirestore db;
    ArrayAdapter<String> adapter;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_bbdetails);

        listView = (ListView) findViewById(R.id.bbdetails);
        db = FirebaseFirestore.getInstance();
        bloodbanks = new ArrayList<>();
        bbid = new ArrayList<>();

        Intent i = getIntent();
        location = i.getStringExtra("Location");




        db.collection("BloodBanks").whereEqualTo("Location",location).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("Done", document.getId() + "=> " + document.getData());
                        String temp = "";
                        temp += "Name : ";
                        temp += document.get("Name").toString();
                        temp += "\nAddress : ";
                        temp += document.get("Address").toString();
                        temp += "\nContact : ";
                        temp += document.get("Contact").toString();
                        bloodbanks.add(temp);
                        bbid.add(document.getId());
                    }
                    if(bbid.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "No Blood Banks nearby", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    ArrayAdapter<String> adapterl=new ArrayAdapter<String>(DonorBBDetails.this,R.layout.donortext,bloodbanks);
                    listView.setAdapter(adapterl);
                } else {
                    Log.w("Error", "Failed",task.getException());
                    Toast.makeText(getApplicationContext(), "No donors available", Toast.LENGTH_LONG).show();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String bbvalue = bloodbanks.get(i);
                Toast.makeText(DonorBBDetails.this, bbvalue, Toast.LENGTH_SHORT).show();

            }
        });




    }

}

