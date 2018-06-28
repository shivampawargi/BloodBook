package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ExistingDrives extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> DocList2 = new ArrayList<>();
    private ListView listv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_drives);

        Intent in66=getIntent();
        Bundle ex10=in66.getExtras();
        final String blooduser=ex10.getString("blooduser");

        listv3=(ListView) findViewById(R.id.listv3);

        db.collection("BloodBankDrives").whereEqualTo("Blood Bank ID",blooduser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (final DocumentSnapshot document : task.getResult())
                    {
                        String temp="";
                        temp+="Name of Drive: ";
                        temp+=document.get("Name of Drive").toString();
                        temp+="\nDate : ";
                        temp+=document.get("Date of Drive").toString();
                        temp+="\nVenue : ";
                        temp+=document.get("Venue of Drive").toString();
                        temp+="\nDetails : ";
                        temp+=document.get("Details of Drive").toString();

                        DocList2.add(temp);

                    }
                    if(DocList2.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    ArrayAdapter<String> adapterl=new ArrayAdapter<String>(ExistingDrives.this,R.layout.donortext,DocList2);
                    listv3.setAdapter(adapterl);
                }
            }
        });
    }
}

