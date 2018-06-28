package com.example.swaraj.bloodbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DonorActivity extends AppCompatActivity {

    TextView name,email,contact,bg, t1, t2, t3, t4, t5;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference user;
    String userName, location;
    ImageView imageView;
    Bitmap bitmap;
    RoundedBitmapDrawable roundedBitmapDrawable;
    FloatingActionButton fab_menu, banks, history, password, contacts, drives;
    Animation fabopen, fabclose, fabclockwise, fabanticlockwise, fabrotcomplete;
    boolean isopen = false;
    String[] locations={"Shivaji Nagar","Aundh", "Kothrud", "Swargate", "Pune Station","Hadapsar", "Katraj","Hinjewadi/ Talegaon"};

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        name = (TextView) findViewById(R.id.userName);
        email = (TextView) findViewById(R.id.email);
        contact = (TextView) findViewById(R.id.contact);
        bg = (TextView) findViewById(R.id.bg);

        imageView = (ImageView) findViewById(R.id.profilepic);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);

        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        userName = i.getStringExtra("UserName");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = storageReference.child(userName+".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        user.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Matrix matrix = new Matrix();
                //set image rotation value to 90 degrees in matrix.
                matrix.postRotate(-90);
                //supply the original width and height, if you don't want to change the height and width of bitmap.
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true);

                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle any errors
        }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
                builder.setTitle("Change Profile Image").setMessage("Do you want to capture a new profile image?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                Uri photoURI = FileProvider.getUriForFile(DonorActivity.this,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        new File("/storage/emulated/0/", userName+".jpg"));
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, 2);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(DonorActivity.this);
                                builder1.setTitle("Upload Image").setMessage("Do you want to upload the captured image?")
                                        .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                            public void onClick(DialogInterface dialog, int which) {

                                                Toast.makeText(DonorActivity.this,"Uploading Image", Toast.LENGTH_LONG).show();
                                                Uri file = Uri.fromFile(new File("/storage/emulated/0/" + userName + ".jpg"));

                                                StorageReference storageRef = storage.getReference();
                                                final StorageReference user1 = storageRef.child(userName+".jpg");
                                                UploadTask uploadTask = user1.putFile(file);
                                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Toast.makeText(DonorActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        Toast.makeText(DonorActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                        user.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                            @Override
                                                            public void onSuccess(byte[] bytes) {
                                                                // Data for "images/island.jpg" is returns, use this as needed

                                                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                                Matrix matrix = new Matrix();
                                                                //set image rotation value to 90 degrees in matrix.
                                                                matrix.postRotate(-90);
                                                                //supply the original width and height, if you don't want to change the height and width of bitmap.
                                                                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),bmp.getHeight(), matrix, true);

                                                                imageView.setImageBitmap(bmp);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                // Handle any errors
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                return false;
            }
        });

        fab_menu = (FloatingActionButton) findViewById(R.id.menu);
        banks = (FloatingActionButton) findViewById(R.id.banks);
        history = (FloatingActionButton) findViewById(R.id.history);
        password = (FloatingActionButton) findViewById(R.id.password);
        contacts = (FloatingActionButton) findViewById(R.id.contacts);
        drives = (FloatingActionButton) findViewById(R.id.drives);

        t1 = (TextView) findViewById(R.id.textView12);
        t2 = (TextView) findViewById(R.id.textView15);
        t3 =  (TextView) findViewById(R.id.textView16);
        t4 = (TextView) findViewById(R.id.textView17);
        t5 = (TextView) findViewById(R.id.textView18);

        banks.setClickable(false);
        history.setClickable(false);
        password.setClickable(false);
        contacts.setClickable(false);
        drives.setClickable(false);

        fabopen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabclockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        fabrotcomplete = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_complete);

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isopen)
                {
                    history.startAnimation(fabrotcomplete);
                    password.startAnimation(fabrotcomplete);
                    contacts.startAnimation(fabrotcomplete);
                    banks.startAnimation(fabrotcomplete);
                    drives.startAnimation(fabrotcomplete);

                    history.startAnimation(fabclose);
                    password.startAnimation(fabclose);
                    contacts.startAnimation(fabclose);
                    banks.startAnimation(fabclose);
                    drives.startAnimation(fabclose);

                    t1.startAnimation(fabclose);
                    t2.startAnimation(fabclose);
                    t3.startAnimation(fabclose);
                    t4.startAnimation(fabclose);
                    t5.startAnimation(fabclose);

                    email.startAnimation(fabopen);
                    contact.startAnimation(fabopen);
                    bg.startAnimation(fabopen);

                    fab_menu.startAnimation(fabanticlockwise);

                    history.setClickable(false);
                    password.setClickable(false);
                    contacts.setClickable(false);
                    banks.setClickable(false);
                    drives.setClickable(false);
                    isopen = false;
                }
                else
                {
                    history.startAnimation(fabrotcomplete);
                    password.startAnimation(fabrotcomplete);
                    contacts.startAnimation(fabrotcomplete);
                    banks.startAnimation(fabrotcomplete);
                    drives.startAnimation(fabrotcomplete);

                    history.startAnimation(fabopen);
                    password.startAnimation(fabopen);
                    contacts.startAnimation(fabopen);
                    banks.startAnimation(fabopen);
                    drives.startAnimation(fabopen);

                    t1.startAnimation(fabopen);
                    t2.startAnimation(fabopen);
                    t3.startAnimation(fabopen);
                    t4.startAnimation(fabopen);
                    t5.startAnimation(fabopen);

                    email.startAnimation(fabclose);
                    contact.startAnimation(fabclose);
                    bg.startAnimation(fabclose);

                    fab_menu.startAnimation(fabclockwise);

                    history.setClickable(true);
                    password.setClickable(true);
                    contacts.setClickable(true);
                    banks.setClickable(true);
                    drives.setClickable(true);
                    isopen = true;
                }
            }
        });

        banks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
                builder.setTitle("Select Location");
                builder.setItems(locations, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        location = locations[i];

                        Intent intent = new Intent(DonorActivity.this, DonorBBDetails.class);
                        intent.putExtra("Location", location);
                        startActivity(intent);
                        Toast.makeText(DonorActivity.this,location,Toast.LENGTH_SHORT).show();

                    }
                });

                builder.create().show();

                history.startAnimation(fabclose);
                password.startAnimation(fabclose);
                contacts.startAnimation(fabclose);
                banks.startAnimation(fabclose);
                drives.startAnimation(fabclose);

                t1.startAnimation(fabclose);
                t2.startAnimation(fabclose);
                t3.startAnimation(fabclose);
                t4.startAnimation(fabclose);
                t5.startAnimation(fabclose);

                email.startAnimation(fabopen);
                contact.startAnimation(fabopen);
                bg.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                history.setClickable(false);
                password.setClickable(false);
                contacts.setClickable(false);
                banks.setClickable(false);
                drives.setClickable(false);
                isopen = false;

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DonorActivity.this, DonorHistory.class);
                i.putExtra("Name",userName);
                startActivity(i);
                history.startAnimation(fabclose);
                password.startAnimation(fabclose);
                contacts.startAnimation(fabclose);
                banks.startAnimation(fabclose);
                drives.startAnimation(fabclose);

                t1.startAnimation(fabclose);
                t2.startAnimation(fabclose);
                t3.startAnimation(fabclose);
                t4.startAnimation(fabclose);
                t5.startAnimation(fabclose);

                email.startAnimation(fabopen);
                contact.startAnimation(fabopen);
                bg.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                history.setClickable(false);
                password.setClickable(false);
                contacts.setClickable(false);
                banks.setClickable(false);
                drives.setClickable(false);
                isopen = false;
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DonorActivity.this, DonorChangePassword.class);
                startActivity(i);
                history.startAnimation(fabclose);
                password.startAnimation(fabclose);
                contacts.startAnimation(fabclose);
                banks.startAnimation(fabclose);
                drives.startAnimation(fabclose);

                t1.startAnimation(fabclose);
                t2.startAnimation(fabclose);
                t3.startAnimation(fabclose);
                t4.startAnimation(fabclose);
                t5.startAnimation(fabclose);

                email.startAnimation(fabopen);
                contact.startAnimation(fabopen);
                bg.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                history.setClickable(false);
                password.setClickable(false);
                contacts.setClickable(false);
                banks.setClickable(false);
                drives.setClickable(false);
                isopen = false;
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DonorActivity.this, DonorChangeContact.class);
                i.putExtra("Name",userName);
                startActivity(i);
                history.startAnimation(fabclose);
                password.startAnimation(fabclose);
                contacts.startAnimation(fabclose);
                banks.startAnimation(fabclose);
                drives.startAnimation(fabclose);

                t1.startAnimation(fabclose);
                t2.startAnimation(fabclose);
                t3.startAnimation(fabclose);
                t4.startAnimation(fabclose);
                t5.startAnimation(fabclose);

                email.startAnimation(fabopen);
                contact.startAnimation(fabopen);
                bg.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                history.setClickable(false);
                password.setClickable(false);
                contacts.setClickable(false);
                banks.setClickable(false);
                drives.setClickable(false);
                isopen = false;
            }
        });

        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("BloodBankDrives").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                Toast.makeText(getApplicationContext(), "Updating List", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(DonorActivity.this, DonorViewDrives.class);
                        startActivity(i);
                    }
                },1500);


                history.startAnimation(fabclose);
                password.startAnimation(fabclose);
                contacts.startAnimation(fabclose);
                banks.startAnimation(fabclose);
                drives.startAnimation(fabclose);

                t1.startAnimation(fabclose);
                t2.startAnimation(fabclose);
                t3.startAnimation(fabclose);
                t4.startAnimation(fabclose);
                t5.startAnimation(fabclose);

                email.startAnimation(fabopen);
                contact.startAnimation(fabopen);
                bg.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                history.setClickable(false);
                password.setClickable(false);
                contacts.setClickable(false);
                banks.setClickable(false);
                drives.setClickable(false);
                isopen = false;
            }
        });

        DocumentReference userdoc = db.collection("Donors").document(userName);
        userdoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if(document != null)
                    {
                        Log.d("Done", document.getId() + "=> " + document.getData());
                        name.setText(document.get("Name").toString());
                        email.setText("Email Id : " + document.get("EmailId").toString());
                        contact.setText("Contact : " + document.get("Contact").toString());
                        bg.setText("Blood Group : " + document.get("Blood Group").toString());

                    }

                }
                else {
                    Log.w("Error", "Failed",task.getException());
                    Toast.makeText(getApplicationContext(), "No donor available", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();

            finish();
            Toast.makeText(DonorActivity.this, "Signing out...",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed(){

    }
}
