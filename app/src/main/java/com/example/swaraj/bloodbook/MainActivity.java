package com.example.swaraj.bloodbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView splashImageView;
    boolean splashloading;
    FloatingActionButton fab_menu, bank, user, help, about;
    Animation fabopen, fabclose, fabclockwise, fabanticlockwise, fabrotcomplete;
    boolean isopen = false;
    TextView title, subtitle, abouttxt, helptxt, usertxt, banktxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*splashImageView = new ImageView(this);
        splashImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        splashImageView.setImageResource(R.drawable.start_screen);
        setContentView(splashImageView);
        splashloading = true;
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                splashloading = false;
                setContentView(R.layout.activity_main);
            }

        }, 3000);
*/
        setContentView(R.layout.activity_main);

        if(!haveNetworkConnection())
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Available");
            builder.setMessage("Please check your internet connection");
            builder.create().show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    finish();
                }

            }, 4000);



        }

        fab_menu = (FloatingActionButton) findViewById(R.id.float_menu);
        bank = (FloatingActionButton) findViewById(R.id.bank);
        user = (FloatingActionButton) findViewById(R.id.user);
        help = (FloatingActionButton) findViewById(R.id.help);
        about = (FloatingActionButton) findViewById(R.id.about);

        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        usertxt = (TextView) findViewById(R.id.usertxt);
        banktxt = (TextView) findViewById(R.id.banktxt);
        abouttxt = (TextView) findViewById(R.id.abouttxt);
        helptxt = (TextView) findViewById(R.id.helptxt);


        about.setClickable(false);
        help.setClickable(false);
        user.setClickable(false);
        bank.setClickable(false);

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
                    about.startAnimation(fabclose);
                    help.startAnimation(fabclose);
                    user.startAnimation(fabclose);
                    bank.startAnimation(fabclose);

                    abouttxt.startAnimation(fabclose);
                    helptxt.startAnimation(fabclose);
                    usertxt.startAnimation(fabclose);
                    banktxt.startAnimation(fabclose);

                    title.startAnimation(fabopen);
                    subtitle.startAnimation(fabopen);

                    fab_menu.startAnimation(fabanticlockwise);

                    about.setClickable(false);
                    help.setClickable(false);
                    user.setClickable(false);
                    bank.setClickable(false);
                    isopen = false;
                }
                else
                {
                    about.startAnimation(fabopen);
                    help.startAnimation(fabopen);
                    user.startAnimation(fabopen);
                    bank.startAnimation(fabopen);

                    abouttxt.startAnimation(fabopen);
                    helptxt.startAnimation(fabopen);
                    usertxt.startAnimation(fabopen);
                    banktxt.startAnimation(fabopen);

                    title.startAnimation(fabclose);
                    subtitle.startAnimation(fabclose);

                    fab_menu.startAnimation(fabclockwise);

                    about.setClickable(true);
                    help.setClickable(true);
                    user.setClickable(true);
                    bank.setClickable(true);
                    isopen = true;
                }
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank.startAnimation(fabrotcomplete);
                Intent i = new Intent(MainActivity.this, BloodBankLogin.class);
                startActivity(i);
                about.startAnimation(fabclose);
                help.startAnimation(fabclose);
                user.startAnimation(fabclose);
                bank.startAnimation(fabclose);

                abouttxt.startAnimation(fabclose);
                helptxt.startAnimation(fabclose);
                usertxt.startAnimation(fabclose);
                banktxt.startAnimation(fabclose);

                title.startAnimation(fabopen);
                subtitle.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                about.setClickable(false);
                help.setClickable(false);
                user.setClickable(false);
                bank.setClickable(false);
                isopen = false;

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.startAnimation(fabrotcomplete);
                Intent i = new Intent(MainActivity.this, UserActivity.class);
                startActivity(i);
                about.startAnimation(fabclose);
                help.startAnimation(fabclose);
                user.startAnimation(fabclose);
                bank.startAnimation(fabclose);

                abouttxt.startAnimation(fabclose);
                helptxt.startAnimation(fabclose);
                usertxt.startAnimation(fabclose);
                banktxt.startAnimation(fabclose);

                title.startAnimation(fabopen);
                subtitle.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                about.setClickable(false);
                help.setClickable(false);
                user.setClickable(false);
                bank.setClickable(false);
                isopen = false;
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help.startAnimation(fabrotcomplete);
                Intent i = new Intent(MainActivity.this, Help.class);
                startActivity(i);
                about.startAnimation(fabclose);
                help.startAnimation(fabclose);
                user.startAnimation(fabclose);
                bank.startAnimation(fabclose);

                abouttxt.startAnimation(fabclose);
                helptxt.startAnimation(fabclose);
                usertxt.startAnimation(fabclose);
                banktxt.startAnimation(fabclose);

                title.startAnimation(fabopen);
                subtitle.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                about.setClickable(false);
                help.setClickable(false);
                user.setClickable(false);
                bank.setClickable(false);
                isopen = false;
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.startAnimation(fabrotcomplete);
                Intent i = new Intent(MainActivity.this, AboutUS.class);
                startActivity(i);
                about.startAnimation(fabclose);
                help.startAnimation(fabclose);
                user.startAnimation(fabclose);
                bank.startAnimation(fabclose);

                abouttxt.startAnimation(fabclose);
                helptxt.startAnimation(fabclose);
                usertxt.startAnimation(fabclose);
                banktxt.startAnimation(fabclose);

                title.startAnimation(fabopen);
                subtitle.startAnimation(fabopen);

                fab_menu.startAnimation(fabanticlockwise);

                about.setClickable(false);
                help.setClickable(false);
                user.setClickable(false);
                bank.setClickable(false);
                isopen = false;
            }
        });
    }

    protected boolean haveNetworkConnection()
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo [] networkInfos = cm.getAllNetworkInfo();
        for(NetworkInfo ni : networkInfos)
        {
            if(ni.getTypeName().equalsIgnoreCase("WIFI"))
            {
                if (ni.isConnected())
                    haveConnectedWifi = true;
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
            {
                if(ni.isConnected())
                    haveConnectedMobile = true;
            }

        }
        return (haveConnectedMobile || haveConnectedWifi);
    }
}
