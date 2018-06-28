package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryRequests extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> DocList2 = new ArrayList<>();
    private ListView listv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_requests);

        Intent in3=getIntent();
        Bundle ex9=in3.getExtras();
        final String blooduser=ex9.getString("blooduser");

        Toast.makeText(getApplicationContext(), "Updating List...", Toast.LENGTH_SHORT).show();

        listv2=(ListView) findViewById(R.id.listv2);

        db.collection("ReceiverRequests").whereEqualTo("Blood Bank ID",blooduser).whereEqualTo("Processed",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (final DocumentSnapshot document : task.getResult())
                    {
                        String temp="";
                        temp+="Name : ";
                        temp+=document.get("Name").toString();
                        temp+="\nContact Number : ";
                        temp+=document.get("Contact no").toString();
                        temp+="\nBlood Group : ";
                        temp+=document.get("Blood Group").toString();
                        temp+="\nUnits : ";
                        temp+=document.get("Units").toString();
                        temp+="\nReason : ";
                        temp+=document.get("Reason").toString();
                        temp+="\nLocation : ";
                        temp+=document.get("Location").toString();

                        DocList2.add(temp);

                    }
                    if(DocList2.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    ArrayAdapter<String> adapterl=new ArrayAdapter<String>(HistoryRequests.this,R.layout.donortext,DocList2);
                    listv2.setAdapter(adapterl);
                }
            }
        });


    }
}
