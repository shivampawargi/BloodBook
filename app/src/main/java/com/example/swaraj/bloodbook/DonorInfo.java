package com.example.swaraj.bloodbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonorInfo extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public TextView text;
    public Button donating;
    public TextView last1;
    String lastdate;
    String sampleid;
    public Button bloodg;
    EditText input;
    String bgselected;

    AdapterDonor adap;
    ExpandableListView exp_list;
    List<String> DocList = new ArrayList<>();
    HashMap<String,List<String>> plasma = new HashMap<>();
    String bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info);

        Intent inte1=getIntent();

        Bundle bundle4 = inte1.getExtras();
        final String blooduser=bundle4.getString("blooduser");
        final String usernm = bundle4.getString("Username");
        final String latest=bundle4.getString("latest");

        Toast.makeText(getApplicationContext(), "Updating List...", Toast.LENGTH_SHORT).show();

        DocumentReference docRef = db.collection("Donors").document(usernm);

        exp_list=(ExpandableListView) findViewById(R.id.exp_list);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    Map<String,Object> data = document.getData();
                    for (Map.Entry<String, Object> entry : data.entrySet())
                    {

                        if(entry.getKey().equals("Blood Group"))
                        {
                            bg=entry.getValue().toString();
                            DocList.add(entry.getKey() + " : " + entry.getValue().toString());

                        }
                        if((entry.getKey().equals("Date of Birth")) || (entry.getKey().equals("Gender")) || (entry.getKey().equals("Contact")) || (entry.getKey().equals("Name")))
                        {
                            DocList.add(entry.getKey() + " : " + entry.getValue().toString());
                        }

                        Log.d("DocList", DocList.toString());

                    }
                    plasma.put(usernm,DocList);
                    DocList=new ArrayList<String>(plasma.keySet());
                    adap=new AdapterDonor(DonorInfo.this,plasma,DocList);
                    exp_list.setAdapter(adap);

                }
            }
        });

        last1=(TextView) findViewById(R.id.last1);
        lastdate="Last Donated on : "+latest;

        last1.setText(lastdate);

        donating=(Button) findViewById(R.id.donating);
        donating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(DonorInfo.this);
                builder1.setMessage("Make sure you have checked the Details ! If you proceed, 350 ml will be donated.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Donate",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                Map<String, Object> ans = new HashMap<>();
                                ans.put("Date",new Date());
                                ans.put("Blood Group",bg);
                                ans.put("Blood Bank ID",blooduser);
                                ans.put("Expired",false);
                                ans.put("Days Completed",0);
                                ans.put("Usable",true);

                                db.collection("Blood Details").add(ans).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        sampleid=documentReference.getId();

                                        Toast toast=Toast.makeText(getApplicationContext(), "Congratulations ! You just saved a life.", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        Intent data5 = new Intent(DonorInfo.this, DonatedSampleDetails.class);
                                        Bundle extras=new Bundle();
                                        extras.putString("samplename",sampleid);
                                        extras.putString("blooduser",blooduser);
                                        extras.putString("donor",usernm);
                                        data5.putExtras(extras);
                                        startActivity(data5);
                                        finish();
                                    }
                                });
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        bloodg=(Button) findViewById(R.id.bloodg);
        bloodg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(latest.equals("Not Found"))
                {

                    final CharSequence[] bg={"O+","O-","A+","A-","B+","B-","AB+","AB-"};
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(DonorInfo.this);
                    builder2.setTitle("Update Blood Group").setSingleChoiceItems(bg, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            switch (arg1)
                            {
                                case 0:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 1:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 2:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 3:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 4:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 5:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 6:
                                    bgselected=(String) bg[arg1];
                                    break;
                                case 7:
                                    bgselected=(String) bg[arg1];
                                    break;

                            }

                        }
                    }).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            db.collection("Donors").document(usernm).update("Blood Group",bgselected);
                            Toast.makeText(getApplicationContext(), "Blood Group Updated  As "+bgselected, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog alert12 = builder2.create();
                    alert12.show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Blood Group has been Verified", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}