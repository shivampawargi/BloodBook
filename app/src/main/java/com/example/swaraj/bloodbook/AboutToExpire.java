package com.example.swaraj.bloodbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutToExpire extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AdapterDonor adapt;
    ExpandableListView exp_list2;
    List<String> DocList1 = new ArrayList<>();
    List<String> DocList2 = new ArrayList<>();
    HashMap<String, List<String>> bgroup = new HashMap<>();
    String dates;
    Integer expire;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_to_expire);

        Intent tent = getIntent();
        Bundle bposexp = tent.getExtras();
        final String blooduser = bposexp.getString("blooduser");

        exp_list2 = (ExpandableListView) findViewById(R.id.exp_list2);

        db.collection("Blood Details").whereEqualTo("Blood Bank ID", blooduser).whereEqualTo("Expired", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String DocId = document.getId();

                        DocList1 = new ArrayList<>();
                        DocList1.add("Packet ID : " + DocId);
                        count++;

                        Map<String, Object> data = document.getData();
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            if (entry.getKey().equals("Days Completed")) {
                                String days = entry.getValue().toString();
                                expire = Integer.parseInt(days);
                                if (expire > 30) {
                                    DocList1.add(entry.getKey() + " : " + "Expired");
                                } else {
                                    DocList1.add(entry.getKey() + " : " + entry.getValue().toString());
                                }

                            }
                            if ((entry.getKey().equals("Date"))) {
                                DocList1.add(entry.getKey() + " : " + entry.getValue().toString());
                                Log.d("DocList", DocList1.toString());
                            }
                        }
                        bgroup.put("Packet " + count, DocList1);
                        DocList2.add(document.getId());
                        DocList1 = new ArrayList<String>(bgroup.keySet());
                        adapt = new AdapterDonor(AboutToExpire.this, bgroup, DocList1);
                        exp_list2.setAdapter(adapt);

                        exp_list2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView expandableListView, View view, final int groupPosition, int childPosition, long l) {

                                dates = bgroup.get(DocList1.get(groupPosition)).get(childPosition).substring(0, 4);

                                if (dates.equals("Date")) {

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AboutToExpire.this);
                                    builder1.setMessage("This Packet will be removed from your Database.\nMake sure you have disposed it off Physically\n\nPacket ID : " + DocList2.get(groupPosition));
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            db.collection("Blood Details").document(DocList2.get(groupPosition)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Packet has been removed from Database", Toast.LENGTH_SHORT).show();

                                                    Intent w = new Intent(AboutToExpire.this, StockList.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("blooduser", blooduser);
                                                    w.putExtras(b);
                                                    w.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(w);
                                                }
                                            });
                                        }
                                    });

                                    builder1.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                        }
                                    });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();

                                }
                                return false;
                            }
                        });
                    }
                    if (bgroup.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Great ! No expired Items", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
