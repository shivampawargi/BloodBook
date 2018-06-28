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
import java.util.List;

public class DonorHistory extends AppCompatActivity {

    ListView listView;
    List<String> history;
    FirebaseFirestore db;
    ArrayAdapter<String> adapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_history);


        listView = (ListView) findViewById(R.id.history2);
        db = FirebaseFirestore.getInstance();
        history= new ArrayList<>();

        Intent i = getIntent();
        name = i.getStringExtra("Name");


        db.collection("donorHistory").whereEqualTo("DonorName",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("Done", document.getId() + "=> " + document.getData());
                        String temp = "";
                        temp += "Date : ";
                        temp += document.get("Date").toString();
                        temp += "\nBloodBank Id : ";
                        temp += document.get("BloodBankdeposited").toString();
                        history.add(temp);
                        Log.d("History",temp);
                    }
                    adapter = new ArrayAdapter<String>(DonorHistory.this, R.layout.donortext, history);
                    listView.setAdapter(adapter);


                    if(history.isEmpty())
                    {
                        Toast.makeText(DonorHistory.this, "No History Available",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Log.w("Error", "Failed",task.getException());
                    Toast.makeText(getApplicationContext(), "No history available", Toast.LENGTH_LONG);
                }
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String bbvalue = history.get(i);
                Toast.makeText(DonorHistory.this, bbvalue, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
