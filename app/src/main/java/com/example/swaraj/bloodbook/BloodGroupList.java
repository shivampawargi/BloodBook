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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BloodGroupList extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AdapterDonor adapt;
    ExpandableListView exp_list1;
    List<String> DocList1 = new ArrayList<>();
    HashMap<String,List<String>> bgroup = new HashMap<>();
    public Button summed;
    Integer totals=0,sum=0;
    public TextView totalquant;
    long diff=0;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group_list);

        Intent tent=getIntent();
        Bundle bposu=tent.getExtras();
        final String blooduser=bposu.getString("blooduser");
        final String bg=bposu.getString("bg");

        Toast.makeText(getApplicationContext(), ""+bg + " Stock", Toast.LENGTH_SHORT).show();

        exp_list1=(ExpandableListView) findViewById(R.id.exp_list1);

        db.collection("Blood Details").whereEqualTo("Blood Bank ID",blooduser).whereEqualTo("Blood Group",bg).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DocumentSnapshot document : task.getResult())
                    {
                        String DocId = document.getId();

                        List<String> DocList1 = new ArrayList<>();
                        DocList1.add("Packet ID : "+DocId);
                        count++;

                        Map<String,Object> data = document.getData();
                        for (Map.Entry<String, Object> entry : data.entrySet())
                        {
                            if(entry.getKey().equals("Date"))
                            {

                                try
                                {

                                    String time = entry.getValue().toString();
                                    DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT'z yyyy", Locale.ENGLISH);
                                    Date date = inputFormat.parse(time);

                                    Date date1 = new Date();

                                    diff = date1.getTime() - date.getTime();
                                    diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if((entry.getKey().equals("Date")))
                            {
                                DocList1.add(entry.getKey() + " : " + entry.getValue().toString());
                                Log.d("DocList", DocList1.toString());
                            }

                        }
                        if(diff<30)
                        {
                            totals++;
                        }
                        bgroup.put("Packet "+count,DocList1);
                        DocList1=new ArrayList<String>(bgroup.keySet());
                        adapt=new AdapterDonor(BloodGroupList.this,bgroup,DocList1);
                        exp_list1.setAdapter(adapt);
                    }
                    if(bgroup.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        });

        totalquant=(TextView) findViewById(R.id.totalquant);
        summed=(Button) findViewById(R.id.summed);
        summed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "A sample is usable for 30 days !", Toast.LENGTH_SHORT).show();

                String total=totals+" units";
                totalquant.setText(total);
            }
        });

    }
}