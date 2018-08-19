package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.PersistData;

public class SplashActivity extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 000;
    private Context con;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        con = this;

      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                MyDbHelper db = MyDbHelper.getInstance(con);
                if (db.getSigninStatus(Config.LOGIN_API).equalsIgnoreCase("true")) {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity2.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, SigninActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
