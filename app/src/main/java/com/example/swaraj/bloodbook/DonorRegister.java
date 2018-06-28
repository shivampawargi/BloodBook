package com.example.swaraj.bloodbook;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.swaraj.bloodbook.R.layout.donortext;

public class DonorRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    TextView dob;
    Button b1;
    Button calendar;
    EditText name,contact,email,password,confirm;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Spinner bgspin;
    String bloodgroup;
    String[] Category={"Select","O+","O-","A+","A-","B+","B-","AB+","AB-"};
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    int emailexists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_register);

        t1 = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = (TextView) findViewById(R.id.textView6);
        t6 = (TextView) findViewById(R.id.textView7);
        t7 = (TextView) findViewById(R.id.textView8);
        t8 = (TextView) findViewById(R.id.textView19);
        b1 = (Button) findViewById(R.id.donorregister);
        calendar = (Button) findViewById(R.id.calendar);
        name = (EditText) findViewById(R.id.name);
        dob = (TextView) findViewById(R.id.dob);
        contact = (EditText) findViewById(R.id.contact);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        bgspin = (Spinner) findViewById(R.id.bloodGroup);
       // m = (RadioButton) findViewById(R.id.male);
       // f = (RadioButton) findViewById(R.id.female);
        confirm = (EditText) findViewById(R.id.confirm);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);


        bgspin = (Spinner) findViewById(R.id.bloodGroup);

        bgspin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,donortext,Category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgspin.setAdapter(aa);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        calendar.setOnClickListener(new View.OnClickListener() {
             class mDateSetListener implements DatePickerDialog.OnDateSetListener {

                 public void onDateSet(DatePicker view, int year, int monthOfYear,
                                       int dayOfMonth) {

                     // getCalender();
                     int mYear = year;
                     int mMonth = monthOfYear;
                     int mDay = dayOfMonth;
                     dob.setText(new StringBuilder()
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
                DatePickerDialog dialog = new DatePickerDialog(DonorRegister.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });


    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        bloodgroup = Category[pos];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void register_click(View view)
    {

        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        emailexists = 0;

        if (radioSexGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(DonorRegister.this, "Please select gender", Toast.LENGTH_SHORT).show();
        }

        if(dob.getText().equals("DOB"))
        {
            Toast.makeText(DonorRegister.this, "Please select Date of Birth", Toast.LENGTH_SHORT).show();
        }

        if(bloodgroup.equals("Select") || TextUtils.isEmpty(name.getText())|| TextUtils.isEmpty(dob.getText())|| TextUtils.isEmpty(contact.getText())|| TextUtils.isEmpty(email.getText())|| TextUtils.isEmpty(password.getText()) || TextUtils.isEmpty(confirm.getText()))
        {
            Toast.makeText(DonorRegister.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        db.collection("Donors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {

                    for (DocumentSnapshot document : task.getResult())
                    {
                        System.out.println(document.getData());
                        if(document.get("EmailId").toString()==email.getText().toString())
                        {
                            emailexists = 1;
                            t1.setText(document.get("EmailId").toString());

                        }
                    }

                }
                else
                {
                    emailexists = 0;
                }
            }
        });

        */

        DocumentReference dr = db.collection("Donors").document(email.getText().toString());
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if(doc != null)
                    {
                        emailexists = 1;
                        return;
                    }
                    else
                    {
                        emailexists=0;
                    }
                }
            }
        });

        if(emailexists==1)
        {
            Toast.makeText(DonorRegister.this, "Email ID is already registered", Toast.LENGTH_SHORT).show();
            return;
        }



        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/mm/yyyy");
        try {
            Date date = sdf.parse(dob.getText().toString());
            Date curdate = new Date();
            long diff = curdate.getTime() - date.getTime();
            if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)<6570)
            {
                Toast.makeText(DonorRegister.this, "The donor must be above 18", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(DonorRegister.this, "Enter valid date of birth", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            Toast.makeText(DonorRegister.this, "Enter valid email-id", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.PHONE.matcher(contact.getText().toString()).matches() || contact.getText().toString().length()!=10)
        {
            Toast.makeText(DonorRegister.this, "Enter valid contact details", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.getText().toString().length()<6 || confirm.getText().toString().length()<6)
        {
            Toast.makeText(DonorRegister.this, "Password must contain atleast 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.getText().toString().equals(confirm.getText().toString()))
        {
            Toast.makeText(DonorRegister.this, "Password mismatch", Toast.LENGTH_SHORT).show();
            return;
        }



        final ProgressDialog progressDialog = ProgressDialog.show(DonorRegister.this, "Please wait...", "Processing...", true);

        (firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isComplete())
                {
                    CollectionReference donors = db.collection("Donors");
                    Map<String, Object> donor = new HashMap<>();
                    donor.put("Name", name.getText().toString());
                    donor.put("Date of Birth", dob.getText());
                    donor.put("Gender",radioSexButton.getText());
                    donor.put("Blood Group", bloodgroup);
                    donor.put("EmailId", email.getText().toString());
                    donor.put("Contact", contact.getText().toString());
                    donors.document(email.getText().toString()).set(donor);


                    Toast.makeText(DonorRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(DonorRegister.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}



