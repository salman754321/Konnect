package com.Konnect.App;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private  static  int Splash_Time_out=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );

        new Handler(  ).postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent( SplashActivity.this,StartActivity.class );
                startActivity( in );
                finish();
            }
        } ,Splash_Time_out);
    }
}
