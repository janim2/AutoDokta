package com.autodokta.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Jobs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        if (getSupportActionBar() != null){

            getSupportActionBar().setTitle("Jobs");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.jobs,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedOption = item.getItemId();
        if (selectedOption == R.id.events){
            startActivity(new Intent(Jobs.this,UploadEvent.class));
        }

        return super.onOptionsItemSelected(item);
    }






}
