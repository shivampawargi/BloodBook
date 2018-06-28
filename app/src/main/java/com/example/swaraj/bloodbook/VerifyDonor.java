package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyDonor extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Button search;
    public EditText usernametxt;
    long diff1=0;
    int flag=0;

    Date latest=new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_donor);

        Intent in=getIntent();
        Bundle ex=in.getExtras();
        final String blooduser=ex.getString("blooduser");

        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            latest = format.parse("01/01/1900");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        search=(Button) findViewById(R.id.search);
        usernametxt=(EditText) findViewById(R.id.usernametxt);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernm = usernametxt.getText().toString();
                if(usernametxt.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please provide a username", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    flag=0;
                    db.collection("donorHistory").whereEqualTo("DonorName",usernm).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for (DocumentSnapshot document : task.getResult())
                                {
                                    if (document.exists())
                                    {
                                        Map<String, Object> data = document.getData();
                                        for (Map.Entry<String, Object> entry : data.entrySet())
                                        {
                                            if (entry.getKey().equals("Date"))
                                            {
                                                try {
                                                    String time = entry.getValue().toString();
                                                    DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT'z yyyy", Locale.ENGLISH);
                                                    Date date1 = inputFormat.parse(time);

                                                    if (date1.after(latest)) {
                                                        latest = date1;
                                                    }

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        flag=1;
                                    }
                                }
                                if(flag==1)
                                {

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date1 = new Date();

                                    diff1 = date1.getTime() - latest.getTime();
                                    diff1 = TimeUnit.DAYS.convert(diff1, TimeUnit.MILLISECONDS);

                                    if (diff1 < 90)
                                    {
                                        Intent data3 = new Intent(VerifyDonor.this, NotEligibleToDonate.class);
                                        Bundle exp1 = new Bundle();
                                        exp1.putString("blooduser", blooduser);
                                        exp1.putString("Username", usernm);
                                        exp1.putString("latest", dateFormat.format(latest));
                                        data3.putExtras(exp1);
                                        startActivity(data3);

                                    }
                                    else
                                    {
                                        Intent data2 = new Intent(VerifyDonor.this, DonorInfo.class);
                                        Bundle exp = new Bundle();
                                        exp.putString("blooduser", blooduser);
                                        exp.putString("Username", usernm);
                                        exp.putString("latest", dateFormat.format(latest));
                                        data2.putExtras(exp);
                                        startActivity(data2);
                                    }
                                }
                                else
                                {
                                    DocumentReference docRef = db.collection("Donors").document(usernm);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists())
                                            {
                                                Intent data2=new Intent(VerifyDonor.this,DonorInfo.class);
                                                Bundle exp=new Bundle();
                                                exp.putString("blooduser",blooduser);
                                                exp.putString("Username",usernm);
                                                exp.putString("latest","Not Found");
                                                data2.putExtras(exp);
                                                startActivity(data2);
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "User does not Exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                            }

                        }
                    });

                }
            }
        });
    }
}
