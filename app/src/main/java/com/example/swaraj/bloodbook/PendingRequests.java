package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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

public class PendingRequests extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> DocList2 = new ArrayList<>();
    List<String> DocList3 = new ArrayList<>();
    List<String> DocList4 = new ArrayList<>();
    List<String> DocList5 = new ArrayList<>();
    private ListView listv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        Intent tent1=getIntent();
        Bundle rece=tent1.getExtras();
        final String blooduser=rece.getString("blooduser");

        listv1=(ListView) findViewById(R.id.listv1);

        Toast t1=Toast.makeText(getApplicationContext(), "Refreshing List...", Toast.LENGTH_SHORT);
        t1.show();


        db.collection("ReceiverRequests").whereEqualTo("Blood Bank ID",blooduser).whereEqualTo("Processed",false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        DocList3.add(document.getId());
                        DocList4.add(document.get("Blood Group").toString());
                        DocList5.add(document.get("Units").toString());
                    }
                    if(DocList2.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "No Pending Requests !", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast t2=Toast.makeText(getApplicationContext(), "A unit is of 350 ml", Toast.LENGTH_SHORT);
                        t2.show();

                        Toast toast=Toast.makeText(getApplicationContext(), "Click on the item to allocate packets", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }

                    ArrayAdapter<String> adapterl=new ArrayAdapter<String>(PendingRequests.this,R.layout.donortext,DocList2);
                    listv1.setAdapter(adapterl);

                    listv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            Intent g=new Intent(PendingRequests.this,ProcessRequests.class);
                            Bundle ex2=new Bundle();
                            ex2.putString("bg",DocList4.get(position));
                            ex2.putString("units",DocList5.get(position));
                            ex2.putString("blooduser",blooduser);
                            ex2.putString("requestid",DocList3.get(position));
                            g.putExtras(ex2);
                            startActivity(g);
                        }
                    });
                }
            }
        });
    }
}