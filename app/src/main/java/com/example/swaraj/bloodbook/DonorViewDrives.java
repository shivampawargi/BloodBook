package com.example.swaraj.bloodbook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class DonorViewDrives extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> DocList = new ArrayList<>();
    private ListView drivelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_view_drives);

        drivelist=(ListView) findViewById(R.id.drives3);

        db.collection("BloodBankDrives").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (final DocumentSnapshot document : task.getResult())
                    {

                        if(document.getId().equals("dummy"))
                        {
                            Log.d("Empty List","Dummy");
                        }
                        else
                        {
                            String temp = "";
                            temp += "Name of Drive: ";
                            temp += document.get("Name of Drive").toString();
                            temp += "\nDate : ";
                            temp += document.get("Date of Drive").toString();
                            temp += "\nVenue : ";
                            temp += document.get("Venue of Drive").toString();
                            temp += "\nDetails : ";
                            temp += document.get("Details of Drive").toString();

                            DocList.add(temp);
                        }

                    }
                    if(DocList.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    ArrayAdapter<String> adapterl=new ArrayAdapter<String>(DonorViewDrives.this,R.layout.donortext,DocList);
                    drivelist.setAdapter(adapterl);
                }
            }
        });
    }
}
