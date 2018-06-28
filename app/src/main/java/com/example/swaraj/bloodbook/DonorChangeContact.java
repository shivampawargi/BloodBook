package com.example.swaraj.bloodbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonorChangeContact extends AppCompatActivity {

    String userid;
    FirebaseFirestore db;
    EditText contact, password;
    Button change;
    String contacttxt, passtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_change_contact);

        Intent i = getIntent();
        userid = i.getStringExtra("Name");
        db = FirebaseFirestore.getInstance();

        contact = (EditText) findViewById(R.id.contact);
        change = (Button) findViewById(R.id.change);



    }

    public void setContact(View view)
    {
        if(TextUtils.isEmpty(contact.getText()))
        {
            Toast.makeText(DonorChangeContact.this, "Enter new contact details" , Toast.LENGTH_SHORT).show();

            contact.setText("");
            return;
        }
        contacttxt = contact.getText().toString();

        DocumentReference docref = db.collection("Donors").document(userid);

        docref.update("Contact",contacttxt);
        Toast.makeText(DonorChangeContact.this,"Contact update successful",Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
