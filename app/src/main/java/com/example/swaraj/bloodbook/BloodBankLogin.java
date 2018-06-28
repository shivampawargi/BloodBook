package com.example.swaraj.bloodbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class BloodBankLogin extends AppCompatActivity {

    EditText email;
    EditText password;
    Button bbSignin;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_login);

        FirebaseApp.initializeApp(this);
        email = (EditText) findViewById( R.id.email);
        password = (EditText) findViewById(R.id.password);
        bbSignin = (Button) findViewById( R.id.bbsignin);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void bbsignin_click(View view)
    {

        if(TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText()))
        {
            Toast.makeText(BloodBankLogin.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(BloodBankLogin.this, "Please wait...", "Processing...", true);

        (firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(BloodBankLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(BloodBankLogin.this, BloodBankHomeList.class);
                    i.putExtra("blooduser",email.getText().toString());
                    startActivity(i);
                    email.setText("");
                    password.setText("");
                }
                else
                {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(BloodBankLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
