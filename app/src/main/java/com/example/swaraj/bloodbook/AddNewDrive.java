package com.example.swaraj.bloodbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddNewDrive extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EditText nameofdrive;
    public TextView dateofdrive;
    public EditText venueofdrive;
    public EditText details;
    public Button announce;
    public Button calendar1;

    String time;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_drive);

        Intent in3=getIntent();
        Bundle ex9=in3.getExtras();
        final String blooduser=ex9.getString("blooduser");

        nameofdrive=(EditText) findViewById(R.id.nameofdrive);
        dateofdrive=(TextView) findViewById(R.id.dateofdrive);
        venueofdrive=(EditText) findViewById(R.id.venueofdrive);
        details=(EditText) findViewById(R.id.details);

        announce=(Button) findViewById(R.id.announce);
        calendar1=(Button) findViewById(R.id.calendar1);

        calendar1.setOnClickListener(new View.OnClickListener() {
             class mDateSetListener implements DatePickerDialog.OnDateSetListener {

                 public void onDateSet(DatePicker view, int year, int monthOfYear,
                                       int dayOfMonth) {

                     // getCalender();
                     int mYear = year;
                     int mMonth = monthOfYear;
                     int mDay = dayOfMonth;
                     dateofdrive.setText(new StringBuilder()
                             .append(mDay).append("/").append(mMonth + 1).append("/")
                             .append(mYear));
                     // System.out.println(dob.getText().toString());


                 }
            }

            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(AddNewDrive.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();



            }

        });

        announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameofdrive.getText().toString();
                String venue=venueofdrive.getText().toString();
                String detail=details.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                try {

                    Date date=formatter.parse(dateofdrive.getText().toString());
                    Date today = new Date();

                    long diff = date.getTime() - today.getTime();
                    diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    if(diff<0)
                    {
                        Toast.makeText(getApplicationContext(), "Please enter Upcoming date", Toast.LENGTH_SHORT).show();
                    }
                    else if(diff>30)
                    {
                        Toast.makeText(getApplicationContext(), "Cannot generate Drives more than a month in advance", Toast.LENGTH_SHORT).show();
                    }
                    else if(nameofdrive.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please Provide Name of Drive", Toast.LENGTH_SHORT).show();
                    }
                    else if(dateofdrive.getText().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Please Provide Date", Toast.LENGTH_SHORT).show();
                    }
                    else if(venueofdrive.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please Provide Venue", Toast.LENGTH_SHORT).show();
                    }
                    else if(details.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please Provide Details like Time", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        time=dateofdrive.getText().toString();
                        flag=1;
                    }
                }
                catch(ParseException e) {
                    Toast.makeText(getApplicationContext(), "Please enter a Valid Date in the dd/MM/yyyy format", Toast.LENGTH_SHORT).show();

                }

                if(flag==1)
                {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Name of Drive",name);
                    data.put("Date of Drive",time);
                    data.put("Venue of Drive",venue);
                    data.put("Details of Drive",detail);
                    data.put("Blood Bank ID",blooduser);

                    db.collection("BloodBankDrives").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Drive has been announced", Toast.LENGTH_SHORT).show();
                            Intent data6=new Intent(AddNewDrive.this,BloodBankHomeList.class);
                            Bundle extras=new Bundle();
                            extras.putString("blooduser",blooduser);
                            data6.putExtras(extras);
                            data6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(data6);
                        }
                    });
                }
            }
        });
    }
}