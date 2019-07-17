package com.autodokta.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class TodoDetailsActivity extends AppCompatActivity {

        String title,message, thedate;
        TextView nametxtView,messagetxtView,the_datetxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        nametxtView = (TextView)findViewById(R.id.todoName);
        messagetxtView = (TextView)findViewById(R.id.todoMessage);
        the_datetxtView = (TextView)findViewById(R.id.todoDate);

        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        thedate = intent.getStringExtra("the_date");

        nametxtView.setText(title);
        messagetxtView.setText(message);
        the_datetxtView.setText(thedate);




    }
}
