package com.example.swaraj.bloodbook;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class ReceiverActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView t1,t2,t3,t4,t5,t6,t7;
    EditText name,reason,contact,quantity;
    Spinner bgspin, componentspin, locatespin;
    String[] Category={"Select","O+","O-","A+","A-","B+","B-","AB+","AB-"};
    String[] locations={"Select","Shivaji Nagar","Aundh", "Kothrud", "Swargate", "Pune Station","Hadapsar", "Katraj","Hinjewadi/ Talegaon"};

    String bloodgroup;
    long diff3;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        t1 = (TextView) findViewById(R.id.textView5);
        t2 = (TextView) findViewById(R.id.textView9);
        t3 = (TextView) findViewById(R.id.textView10);
        t4 = (TextView) findViewById(R.id.textView11);
        t6 = (TextView) findViewById(R.id.textView13);
        t7 = (TextView) findViewById(R.id.textView14);
        name = (EditText) findViewById(R.id.name);
        reason = (EditText) findViewById(R.id.reason);
        contact = (EditText) findViewById(R.id.contact);
        quantity = (EditText) findViewById(R.id.quantity);

        bgspin = (Spinner) findViewById(R.id.bloodgroup);
        locatespin = (Spinner) findViewById(R.id.location);


        db.collection("Blood Details").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

        bgspin.setOnItemSelectedListener(this);
        ArrayAdapter bgaa = new ArrayAdapter(this,R.layout.donortext,Category);
        bgaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgspin.setAdapter(bgaa);

        bgspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgroup = Category[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        locatespin.setOnItemSelectedListener(this);
        ArrayAdapter locateaa = new ArrayAdapter(this,R.layout.donortext,locations);
        locateaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locatespin.setAdapter(locateaa);

        locatespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location = locations[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    public void go_click(View view)
    {
        String nametxt = name.getText().toString();
        String reasontxt = reason.getText().toString();
        String contact_txt = contact.getText().toString();
        String quantitytxt = quantity.getText().toString();

        if( location.equals("Select") || bloodgroup.equals("Select") || TextUtils.isEmpty(name.getText())|| TextUtils.isEmpty(reason.getText())|| TextUtils.isEmpty(contact.getText())|| TextUtils.isEmpty(quantity.getText()))
        {
            Toast.makeText(ReceiverActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        if(!Patterns.PHONE.matcher(contact.getText().toString()).matches() || contact_txt.length()!=10)
        {
            Toast.makeText(ReceiverActivity.this, "Enter valid contact details", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent i = new Intent(ReceiverActivity.this, ReceiverBBDetails.class);
        i.putExtra("Name",nametxt);
        i.putExtra("Reason",reasontxt);
        i.putExtra("Contact",contact_txt);
        i.putExtra("Quantity",quantitytxt);
        i.putExtra("Bloodgroup",bloodgroup);
        i.putExtra("Location",location);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();

        name.setText("");
        reason.setText("");
        contact.setText("");
        quantity.setText("");
        bgspin.setSelection(0);
        locatespin.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
