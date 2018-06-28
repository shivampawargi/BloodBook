package com.example.swaraj.bloodbook;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.icu.text.DateFormat.NONE;

public class CaptureReceiver extends AppCompatActivity {


    String bbid,name, reason, contact, quantity, bloodgroup, location;;
    String recDetails;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    FirebaseFirestore db;
    String bbemail;
    Button send,capture;
    String receiverDetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_receiver);

        db = FirebaseFirestore.getInstance();

        send = (Button) findViewById(R.id.send);
        capture = (Button) findViewById(R.id.capture);

        send.setClickable(false);
        Intent i = getIntent();
        bbid = i.getStringExtra("BloodBankID");
        name = i.getStringExtra("Name");
        reason = i.getStringExtra("Reason");
        contact = i.getStringExtra("Contact");
        quantity = i.getStringExtra("Quantity");
        bloodgroup = i.getStringExtra("Bloodgroup");
        location = i.getStringExtra("Location");

        receiverDetails += ("\nName : " + name);
        receiverDetails += ("\nBloodgroup : " + bloodgroup);
        receiverDetails += ("\nReason : " + reason);
        receiverDetails += ("\nContact : " + contact);
        receiverDetails += ("\nUnits : " + quantity);
        receiverDetails += ("\nLocation : " + location);

        DocumentReference docref = db.collection("BloodBanks").document(bbid);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document!=null)
                    {
                        bbemail = document.get("Email Id").toString();
                    }
                }
                else
                {
                    Log.d("Failed","Retrieval failed ",task.getException());
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/storage/emulated/0/", "temp.jpg")));
                startActivityForResult(intent, 2);
                send.setClickable(true);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, 2);*/
               if (send.isClickable()) {
                   CollectionReference donors = db.collection("ReceiverRequests");
                   Map<String, Object> donor = new HashMap<>();
                   donor.put("Name", name);
                   donor.put("Blood Bank ID", bbid);
                   donor.put("Blood Group", bloodgroup);
                   donor.put("Reason", reason);
                   donor.put("Contact no", contact);
                   donor.put("Units", quantity);
                   donor.put("Location", location);
                   donor.put("Processed", false);
                   donors.add(donor);

                   Toast.makeText(CaptureReceiver.this, "Request sent", Toast.LENGTH_SHORT).show();


                   Thread thread = new Thread() {
                       @Override
                       public void run() {

                           GMailSender sender = new GMailSender("blood.requestor@gmail.com", "bloodrequest123");
                           try {
                               sender.sendMail("Blood Request", "Requestor Details : \n" + receiverDetails, "blood.requestor@gmail.com", bbemail, "/storage/emulated/0/temp.jpg");

                           } catch (Exception e) {
                               e.printStackTrace();
                           }

                       }
                   };
                   thread.start();
                   Toast.makeText(CaptureReceiver.this, "Sending Mail request", Toast.LENGTH_LONG).show();
                   finish();
               }
               else
               {
                   Toast.makeText(CaptureReceiver.this, "Capture your image first", Toast.LENGTH_SHORT).show();
               }

            }
        });

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        if (requestCode == 2) {
            File picture = new File("/storage/emulated/0/temp.jpg");
            //ImageView imageView=(ImageView)findViewById(R.id.imageView);
            Uri imgUri = Uri.fromFile(picture);
            //imageView.setImageURI(imgUri);

        }
    }
}
