package com.example.swaraj.bloodbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import android.os.Handler;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProcessRequests extends AppCompatActivity {
    ArrayList<String> selectedItems=new ArrayList<>();

    ListView list;
    int count=0;
    int packet=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> DocList4 = new ArrayList<>();
    List<String> DocList5 = new ArrayList<>();
    List<String> last = new ArrayList<>();
    String temp;
    String temp1;
    String temp2;
    public Button proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_requests);

        Intent tent5=getIntent();
        Bundle give=tent5.getExtras();
        final String bg=give.getString("bg");
        final String units=give.getString("units");
        final String blooduser=give.getString("blooduser");
        final String requestid=give.getString("requestid");

        list=(ListView) findViewById(R.id.list);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        db.collection("Blood Details").whereEqualTo("Blood Bank ID",blooduser).whereEqualTo("Blood Group",bg).whereEqualTo("Usable",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult())
                {
                    String DocId = document.getId();
                    packet++;

                    String time=document.get("Date").toString();
                    last.add(time);
                    DocList4.add("Packet "+packet+"\nPacket ID : "+DocId);
                    DocList5.add(DocId);
                }

            }
        });

        Toast.makeText(getApplicationContext(), "Updating List...", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(last.size()>1)
                {
                    for(int i=0;i<last.size()-1;i++)
                    {
                        for(int j=0;j<last.size()-i-1;j++)
                        {
                            try
                            {
                                DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT'z yyyy", Locale.ENGLISH);
                                Date date1 = inputFormat.parse(last.get(j));

                                Date date2=inputFormat.parse(last.get(j+1));

                                if (date1.after(date2)) {
                                    temp=last.get(j+1);
                                    last.set(j+1,last.get(j));
                                    last.set(j,temp);

                                    temp1=DocList4.get(j+1);
                                    DocList4.set(j+1,DocList4.get(j));
                                    DocList4.set(j,temp1);

                                    temp2=DocList5.get(j+1);
                                    DocList5.set(j+1,DocList5.get(j));
                                    DocList5.set(j,temp2);

                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                final Integer unit;
                unit=Integer.parseInt(units);

                if(DocList4.size()<unit)
                {
                    Toast.makeText(getApplicationContext(), "Not enough Packets !", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Oldest Packets are Displayed First", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProcessRequests.this,R.layout.check,R.id.txt_lan,DocList4);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String selectedItem=DocList5.get(position);
                        if(selectedItems.contains(selectedItem))
                        {
                            selectedItems.remove(selectedItem);
                            count--;
                        }
                        else {
                            selectedItems.add(selectedItem);
                            count++;
                        }
                    }
                });
                proceed=(Button) findViewById(R.id.proceed);

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(count==unit)
                        {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProcessRequests.this);
                            builder1.setMessage("Are you sure you want to allocate these packets ?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            for(int i=0;i<selectedItems.size();i++)
                                            {
                                                db.collection("Blood Details").document(selectedItems.get(i)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast toast=Toast.makeText(getApplicationContext(), "Congratulations ! You just saved a life.", Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();

                                                    }
                                                });
                                            }

                                            db.collection("ReceiverRequests").document(requestid).update("Processed",true);


                                            Intent data6=new Intent(ProcessRequests.this,BloodBankHomeList.class);
                                            Bundle extras=new Bundle();
                                            extras.putString("blooduser",blooduser);
                                            data6.putExtras(extras);
                                            data6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(data6);

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Toast.makeText(getApplicationContext(), "Pending Allocation of Packets to Receiver !", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Select Number of Packets : "+unit, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        },2000);

    }
}