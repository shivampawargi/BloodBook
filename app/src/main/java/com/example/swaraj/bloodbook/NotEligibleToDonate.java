package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotEligibleToDonate extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public TextView text;

    AdapterDonor adaptno;
    ExpandableListView exp_listno;
    List<String> DocList = new ArrayList<>();
    HashMap<String,List<String>> plasma = new HashMap<>();
    String usernm;
    String bg;
    public Button ok;
    public  TextView last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_eligible_to_donate);

        Intent inte=getIntent();

        Bundle bundle = inte.getExtras();
        final String blooduser=bundle.getString("blooduser");
        final String usernm = bundle.getString("Username");
        final String latest=bundle.getString("latest");

        DocumentReference docRef = db.collection("Donors").document(usernm);

        exp_listno=(ExpandableListView) findViewById(R.id.exp_listno);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Map<String,Object> data = document.getData();
                        for (Map.Entry<String, Object> entry : data.entrySet())
                        {
                            if(entry.getKey().equals("Blood Group"))
                            {
                                DocList.add(entry.getKey() + " : " + entry.getValue().toString());
                                bg=entry.getValue().toString();
                            }
                            if((entry.getKey().equals("Date of Birth")) || (entry.getKey().equals("Gender")) || (entry.getKey().equals("Contact")) || (entry.getKey().equals("Name")))
                            {
                                DocList.add(entry.getKey() + " : " + entry.getValue().toString());
                            }
                            Log.d("DocList", DocList.toString());

                        }
                        plasma.put(usernm,DocList);
                        DocList=new ArrayList<String>(plasma.keySet());
                        adaptno=new AdapterDonor(NotEligibleToDonate.this,plasma,DocList);
                        exp_listno.setAdapter(adaptno);


                    }
                }
            }
        });

        last=(TextView) findViewById(R.id.last);
        String lastdate="Last Donated on : "+latest;
        last.setText(lastdate);

        ok=(Button) findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data3=new Intent(NotEligibleToDonate.this,BloodBankHomeList.class);
                Bundle bd=new Bundle();
                bd.putString("blooduser",blooduser);
                data3.putExtras(bd);
                data3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(data3);
            }
        });
    }
}

