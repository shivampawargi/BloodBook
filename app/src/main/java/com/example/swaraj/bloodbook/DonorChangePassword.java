package com.example.swaraj.bloodbook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DonorChangePassword extends AppCompatActivity {

    TextView t1,t2,t3;
    EditText current, newpass, confirm;
    String currpass, newpass_,confirmpass;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_change_password);

        t1 = (TextView) findViewById(R.id.textView22);
        t3 = (TextView) findViewById(R.id.textView24);

        newpass = (EditText) findViewById(R.id.newpass);
        confirm = (EditText) findViewById(R.id.confirm);

        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void change_pass(View view)
    {

        if(TextUtils.isEmpty(newpass.getText()) || TextUtils.isEmpty(confirm.getText()) )
        {
            Toast.makeText(DonorChangePassword.this, "Enter all fields",Toast.LENGTH_SHORT).show();
            confirm.setText("");
            newpass.setText("");
        }
        newpass_ = newpass.getText().toString();
        confirmpass = confirm.getText().toString();

        if(newpass_.equals(confirmpass))
        {
            user.updatePassword(newpass_).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(DonorChangePassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(DonorChangePassword.this, "Password change failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(DonorChangePassword.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
            confirm.setText("");
            newpass.setText("");
        }
    }
}
