package com.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.guojian.weekcook.R;

public class WelcomeScreenActivity extends Activity {

    private static final String TAG = "VerticalSplash";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //Display the current version number
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("com.guojian.weekcook", 0);
            TextView versionNumber = (TextView) findViewById(R.id.text000001);
            String version = "©2017 易厨房v" + pi.versionName+" ";
            if (versionNumber != null){
                versionNumber.setText(version);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(WelcomeScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                WelcomeScreenActivity.this.finish();
            }
        }, 3000);

    }

}
