package com.autodokta.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HelpCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        getSupportActionBar().setTitle("Help");
    }
}
