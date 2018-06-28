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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DonorLogin extends AppCompatActivity {

    EditText email;
    EditText password;
    Button donorSignin;
    Button donorNewRegister;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        donorSignin = (Button) findViewById(R.id.donorsignin);
        donorNewRegister = (Button) findViewById(R.id.donorregister);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void donorSignin_click(View view)
    {
        if(TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText()))
        {
            Toast.makeText(DonorLogin.this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(DonorLogin.this, "Please wait...", "Processing...", true);

        (firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(DonorLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DonorLogin.this, DonorActivity.class);
                    i.putExtra("UserName", email.getText().toString() );
                    startActivity(i);
                    email.setText("");
                    password.setText("");
                }
                else
                {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(DonorLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setDonorNewRegister(View view)
    {
        Intent intent = new Intent(DonorLogin.this, DonorRegister.class);
        startActivity(intent);
        email.setText("");
        password.setText("");
    }
}
