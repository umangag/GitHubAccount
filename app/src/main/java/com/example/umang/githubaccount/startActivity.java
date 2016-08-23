package com.example.umang.githubaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by umang on 23/8/16.
 */
public class startActivity extends AppCompatActivity {

    sharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = new sharedPreferences(this);
        new Thread(new Runnable() {
            public void run() {
                Boolean valid = (shared.getvalue(sharedPreferences.USERNAME) == null) ? true : false;

                if (valid)
                    startActivity(new Intent(startActivity.this, Login.class));
                else {
                    startActivity(new Intent(startActivity.this, MainActivity.class));
                }

                finish();
            }
        }).start();

    }

}

