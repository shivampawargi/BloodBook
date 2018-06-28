package com.example.swaraj.bloodbook;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ClipboardManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListOfDonors extends AppCompatActivity {

    private ListView listv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> DocList5 = new ArrayList<>();
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_donors);

        Intent tent3=getIntent();
        Bundle rece=tent3.getExtras();
        final String blooduser=rece.getString("blooduser");

        Toast.makeText(getApplicationContext(), "Updating List...", Toast.LENGTH_SHORT).show();

        Toast toast=Toast.makeText(getApplicationContext(), "Long Press on the item to copy User ID", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        listv=(ListView) findViewById(R.id.listv);

        db.collection("donorHistory").whereEqualTo("BloodBankdeposited",blooduser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (final DocumentSnapshot document : task.getResult())
                {
                    String DocId = document.get("DonorName").toString();
                    DocList5.add(DocId);
                }

                if(DocList5.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Donor List is Empty", Toast.LENGTH_SHORT).show();
                    finish();
                }

                Set<String> hs = new HashSet<>();
                hs.addAll(DocList5);
                DocList5.clear();
                DocList5.addAll(hs);

                ArrayAdapter<String> adapterl=new ArrayAdapter<String>(ListOfDonors.this,R.layout.donortext,DocList5);
                listv.setAdapter(adapterl);

                listv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ID=DocList5.get(i);

                        ClipboardManager clipbooard=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip=ClipData.newPlainText("User ID",ID);
                        clipbooard.setPrimaryClip(clip);

                        Toast.makeText(getApplicationContext(), "User ID Copied to Clip Board", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });

            }
        });



    }
}

