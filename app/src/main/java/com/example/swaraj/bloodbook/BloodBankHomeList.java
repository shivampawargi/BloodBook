package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BloodBankHomeList extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    long diff3;

    public Button logout;

    TextView textview0;
    TextView textview1;
    TextView textview3;
    TextView textview4;
    TextView textview5;
    TextView textview2;
    TextView textview6;
    TextView textview7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_home_list);

        Intent in3=getIntent();
        Bundle ex9=in3.getExtras();
        final String blooduser=ex9.getString("blooduser");


        textview0=(TextView) findViewById(R.id.textview0);
        textview1=(TextView) findViewById(R.id.textview1);
        textview2=(TextView) findViewById(R.id.textview2);
        textview3=(TextView) findViewById(R.id.textview3);
        textview4=(TextView) findViewById(R.id.textview4);
        textview5=(TextView) findViewById(R.id.textview5);
        textview6=(TextView) findViewById(R.id.textview6);
        textview7=(TextView) findViewById(R.id.textview7);

        logout=(Button) findViewById(R.id.logout);


        CircleMenu circleMenu=(CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#FFFFFF"),R.drawable.blogo,R.drawable.blogo)
                .addSubMenu(Color.parseColor("#FFEBAD"),R.drawable.stock)
                .addSubMenu(Color.parseColor("#FFFFA3"),R.drawable.pending)
                .addSubMenu(Color.parseColor("#F5D6FF"),R.drawable.history)
                .addSubMenu(Color.parseColor("#BFFFBF"),R.drawable.drive)
                .addSubMenu(Color.parseColor("#CCEBFF"),R.drawable.lod)
                .addSubMenu(Color.parseColor("#FFD9E8"),R.drawable.verify1)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {

                        textview0.setAlpha(0.0f);
                        textview1.setAlpha(0.0f);
                        textview2.setAlpha(0.0f);
                        textview3.setAlpha(0.0f);
                        textview4.setAlpha(0.0f);
                        textview5.setAlpha(0.0f);
                        textview6.setAlpha(0.0f);
                        textview7.setAlpha(0.0f);

                        switch(i)
                        {
                            case 0:

                                db.collection("Blood Details").whereEqualTo("Blood Bank ID",blooduser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            for (DocumentSnapshot document : task.getResult())
                                            {
                                                DocumentReference expRef = db.collection("Blood Details").document(document.getId());

                                                Map<String,Object> data = document.getData();
                                                for (Map.Entry<String, Object> entry : data.entrySet())
                                                {
                                                    if(entry.getKey().equals("Date")) {

                                                        try {

                                                            String time = entry.getValue().toString();
                                                            DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT'z yyyy", Locale.ENGLISH);
                                                            Date date = inputFormat.parse(time);

                                                            Date date1 = new Date();

                                                            diff3 = date1.getTime() - date.getTime();
                                                            diff3 = TimeUnit.DAYS.convert(diff3, TimeUnit.MILLISECONDS);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }
                                                if(diff3>21)
                                                {
                                                    expRef.update("Expired",true);
                                                    expRef.update("Days Completed",diff3);
                                                }
                                                if(diff3>30)
                                                {
                                                    expRef.update("Usable",false);
                                                }
                                            }

                                        }
                                        else
                                        {
                                            Log.d("Error", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent data= new Intent(BloodBankHomeList.this,StockList.class);
                                        Bundle ex1=new Bundle();
                                        ex1.putString("blooduser",blooduser);
                                        data.putExtras(ex1);
                                        startActivity(data);


                                    }
                                },230);
                                break;

                            case 1:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent data1=new Intent(BloodBankHomeList.this,PendingRequests.class);
                                        Bundle bun=new Bundle();
                                        bun.putString("blooduser",blooduser);
                                        data1.putExtras(bun);
                                        startActivity(data1);

                                    }
                                },230);

                                break;
                            case 2:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent data2=new Intent(BloodBankHomeList.this,HistoryRequests.class);
                                        Bundle ex2=new Bundle();
                                        ex2.putString("blooduser",blooduser);
                                        data2.putExtras(ex2);
                                        startActivity(data2);

                                    }
                                },230);

                                break;
                            case 3:

                                db.collection("BloodBankDrives").whereEqualTo("Blood Bank ID",blooduser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        for (DocumentSnapshot document : task.getResult())
                                        {
                                            Map<String,Object> data = document.getData();
                                            for (Map.Entry<String, Object> entry : data.entrySet())
                                            {
                                                if(entry.getKey().equals("Date of Drive"));
                                                {
                                                    try {
                                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                                                        Date date = formatter.parse(entry.getValue().toString());

                                                        Date today = new Date();

                                                        long diff = today.getTime() - date.getTime();
                                                        diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                                        if(diff>0)
                                                        {
                                                            db.collection("BloodBankDrives").document(document.getId()).delete();
                                                        }
                                                        else
                                                            break;
                                                    }catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent data4=new Intent(BloodBankHomeList.this,BloodBankDrives.class);
                                        Bundle ex=new Bundle();
                                        ex.putString("blooduser",blooduser);
                                        data4.putExtras(ex);
                                        startActivity(data4);
                                    }
                                },230);

                                break;
                            case 4:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent data3=new Intent(BloodBankHomeList.this,ListOfDonors.class);
                                        Bundle ex3=new Bundle();
                                        ex3.putString("blooduser",blooduser);
                                        data3.putExtras(ex3);
                                        startActivity(data3);

                                    }
                                },230);

                                break;
                            case 5:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent data4=new Intent(BloodBankHomeList.this,VerifyDonor.class);
                                        Bundle ex=new Bundle();
                                        ex.putString("blooduser",blooduser);
                                        data4.putExtras(ex);
                                        startActivity(data4);

                                    }
                                },230);


                                break;

                        }
                    }
                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
                textview0.setAlpha(1.0f);
                textview1.setAlpha(1.0f);
                textview2.setAlpha(1.0f);
                textview3.setAlpha(1.0f);
                textview4.setAlpha(1.0f);
                textview5.setAlpha(1.0f);
                textview6.setAlpha(1.0f);
                textview7.setAlpha(1.0f);
                logout.setAlpha(0.0f);
            }

            @Override
            public void onMenuClosed() {
                textview0.setAlpha(0.0f);
                textview1.setAlpha(0.0f);
                textview2.setAlpha(0.0f);
                textview3.setAlpha(0.0f);
                textview4.setAlpha(0.0f);
                textview5.setAlpha(0.0f);
                textview6.setAlpha(0.0f);
                textview7.setAlpha(0.0f);
                logout.setAlpha(1.0f);
            }
        });

    }

    @Override
    public void onBackPressed(){

    }

    public void sign_out(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(BloodBankHomeList.this, "Signing Out...",Toast.LENGTH_SHORT).show();
        finish();
    }


}

