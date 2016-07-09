package com.mandeep.zoogol;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mandeep.zoogol.authentication.LoginActivity;
import com.mandeep.zoogol.authentication.WelcomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                finish();
            }
        }, 3000);

    }
}
