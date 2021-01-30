package com.autodokta.app;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.autodokta.app.Adapters.Todo;

import java.util.ArrayList;

public class TodoDetailsActivity extends AppCompatActivity {

        String title,note, thedate,the_vname,the_vmodel,thevyear,thevfuel_capacity,
                thevmaker,thevfueltype,thevtype;
        TextView carnametxtView,messagetxtView,the_datetxtView,the_vmodel_text,thevyear_text,
                thevfuel_capacity_text,thevmaker_text,thevfueltype_text,thevtype_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        carnametxtView = (TextView)findViewById(R.id.todocarName);
        messagetxtView = (TextView)findViewById(R.id.todoNote);
        the_datetxtView = (TextView)findViewById(R.id.todoDate);

        the_vmodel_text = (TextView)findViewById(R.id.todocarmodel);
        thevyear_text = (TextView)findViewById(R.id.todocaryear);
        thevfuel_capacity_text = (TextView)findViewById(R.id.todocarfuel_capa);
        thevmaker_text = (TextView)findViewById(R.id.todocarmaker);
        thevfueltype_text = (TextView)findViewById(R.id.todofueltype);
        thevtype_text = (TextView)findViewById(R.id.todocartype);

//        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
        thedate = intent.getStringExtra("the_date");
        the_vname = intent.getStringExtra("the_vehicle_name");
        the_vmodel = intent.getStringExtra("the_vehicle_model");
        thevyear = intent.getStringExtra("the_vehicle_year");
        thevfuel_capacity = intent.getStringExtra("the_vehicle_fuel_capacity");
        thevmaker = intent.getStringExtra("the_vehicle_maker");
        thevfueltype = intent.getStringExtra("the_vehicle_fuel_type");
        thevtype = intent.getStringExtra("the_vehicle_type");

        carnametxtView.setText(the_vname);
        messagetxtView.setText(note);
        the_datetxtView.setText(thedate);

        the_vmodel_text.setText(the_vmodel);
        thevyear_text.setText(thevyear);
        thevfuel_capacity_text.setText(thevfuel_capacity);
        thevmaker_text.setText(thevmaker);
        thevfueltype_text.setText(thevfueltype);
        thevtype_text.setText(thevtype);
    }
}
