package com.example.swaraj.bloodbook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiverBBDetails extends AppCompatActivity {


    ListView listView;
    ArrayAdapter<String> adapterl;
    ArrayList<String> bloodbanks;
    ArrayList<String> bbid;
    ArrayList<Integer> packetcnt;
    ArrayList<String> tempbb;
    FirebaseFirestore db1,db2;
    ArrayAdapter<String> adapter;
    String name, reason, contact, quantity, bloodgroup, location;
    int count;
    boolean isSuccess = false;
    //DocumentSnapshot document;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_bbdetails);

        listView = (ListView) findViewById(R.id.bblist);
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        bloodbanks = new ArrayList<>();
        bbid = new ArrayList<>();
        packetcnt = new ArrayList<>();
        tempbb = new ArrayList<>();

        Intent i = getIntent();

        name = i.getStringExtra("Name");
        reason = i.getStringExtra("Reason");
        contact = i.getStringExtra("Contact");
        quantity = i.getStringExtra("Quantity");
        bloodgroup = i.getStringExtra("Bloodgroup");
        location = i.getStringExtra("Location");

        adapterl=new ArrayAdapter<String>(ReceiverBBDetails.this,R.layout.donortext,bloodbanks);
        listView.setAdapter(adapterl);

        count = 0;

        db1.collection("BloodBanks").whereEqualTo("Location",location).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    for (final DocumentSnapshot document : task.getResult())
                    {
                        Log.d("Done", document.getId() + "=> " + document.getData());

                        count= 0;
                        db2.collection("Blood Details").whereEqualTo("Blood Bank ID",document.get("UserName").toString()).whereEqualTo("Blood Group",bloodgroup).whereEqualTo("Usable",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                if (task1.isSuccessful()) {

                                    int count = 0;
                                    for (DocumentSnapshot blooddoc : task1.getResult()) {

                                        count++;
                                    }
                                    Log.d("Count",Integer.toString(count));
                                    if(count >= Integer.parseInt(quantity)) {
                                        String temp = "";
                                        temp += "Name : ";
                                        temp += document.get("Name").toString();
                                        temp += "\nAddress : ";
                                        temp += document.get("Address").toString();
                                        temp += "\nContact : ";
                                        temp += document.get("Contact").toString();
                                        bloodbanks.add(temp);
                                        bbid.add(document.getId());
                                        Log.d("ID",document.getId());
                                        adapterl.notifyDataSetChanged();
                                    }
                                }
                            }
                        });




                    }

                    /*
                    if(bbid.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "No Blood packets available", Toast.LENGTH_SHORT).show();
                        finish();
                    }
*/


                } else {
                    Log.w("Error", "Failed",task.getException());
                    Toast.makeText(getApplicationContext(), "No blood banks in this area available", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


        /*
        for( String id : bbid)
        {
            count= 0 ;
            db2.collection("Blood Details").whereEqualTo("Blood Bank ID",id).whereEqualTo("Blood Group",bloodgroup).whereEqualTo("Usable",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                    if (task1.isSuccessful()) {

                        for (DocumentSnapshot blooddoc : task1.getResult()) {

                            count++;
                        }
                        packetcnt.add(count);
                    }
                }
            });
            if(packetcnt.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "No Blood packets available", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        int bbcnt = 1;
        for (int cnt : packetcnt)
        {
            if(cnt >= Integer.parseInt(quantity))
            {
                bloodbanks.add(tempbb.get(bbcnt));
                isSuccess =true;
            }
            bbcnt++;
        }
        ArrayAdapter<String> adapterl=new ArrayAdapter<String>(ReceiverBBDetails.this,R.layout.donortext,bloodbanks);
        listView.setAdapter(adapterl);
*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String bbvalue = bbid.get(i);
                Toast.makeText(ReceiverBBDetails.this, bbvalue, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ReceiverBBDetails.this, CaptureReceiver.class);
                intent.putExtra("BloodBankID",bbvalue);
                intent.putExtra("Name",name);
                intent.putExtra("Reason",reason);
                intent.putExtra("Contact",contact);
                intent.putExtra("Quantity",quantity);
                intent.putExtra("Bloodgroup",bloodgroup);
                intent.putExtra("Location",location);
                startActivity(intent);

                finish();
            }
        });




    }


}
