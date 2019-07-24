package com.autodokta.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Todo_inner_Service extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText odometer,location,note;
    String dateString,odoString,locationString,serviceString,noteString;
    Spinner service;
    Button saveButton;
    ProgressBar loading;
    TextView dateText,success_message;
    FirebaseUser firebaseUser;

    String[] service_list = {"Air Conditioning","Air Filter","Battery","Belts","Brake Fluid",
            "Brake Pad","Cooling System","Engine Repair","Exhaust System","Fuel Pump","Heating System",
            "Oil Change","Radiator","Wheel Alignment","Steering System","Transmission Fluid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_inner__service);
        getSupportActionBar().setTitle("Service");

        service = (Spinner)findViewById(R.id.service_spinner);
        odometer = (EditText)findViewById(R.id.odometer_input);
        location = (EditText)findViewById(R.id.location);
        note = (EditText)findViewById(R.id.notes_input);
        saveButton = (Button)findViewById(R.id.save);
        dateText = (TextView)findViewById(R.id.date_input);
        success_message = (TextView)findViewById(R.id.success_message);
        loading = (ProgressBar)findViewById(R.id.loading);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // initialization of user

        //setting the spinner adapter for the service
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serviceString = service_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                serviceString = "None";
            }
        });
        final ArrayAdapter<String> the_service = new ArrayAdapter<String>(Todo_inner_Service.this,android.R.layout.simple_list_item_1,service_list);
        service.setAdapter(the_service);

        //setting the onclick for the date;
        //setting onclick for setting the date from the date picker
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] thedate = {dateText};
                showDatePicker(null,thedate);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    private void validateFields() {
        success_message.setVisibility(View.GONE);
        //getting the strings from the edittext
        dateString = dateText.getText().toString();
        odoString = odometer.getText().toString().trim();
        locationString = location.getText().toString().trim();
        noteString = note.getText().toString().trim();

        if(!dateString.equals("Select date")){
            if(!odoString.equals("")){
                if(!locationString.equals("")){
                    if(!noteString.equals("")){
                        if(isNetworkAvailable()){
                            loading.setVisibility(View.VISIBLE);
                            if(save_service()){
                                loading.setVisibility(View.GONE);
                                success_message.setTextColor(getResources().getColor(R.color.colorPrimary));
                                success_message.setText("Upload Successful");
                                success_message.setVisibility(View.VISIBLE);
                            }else{
                                loading.setVisibility(View.GONE);
                                success_message.setTextColor(getResources().getColor(R.color.red));
                                success_message.setText("Upload Failed");
                                success_message.setVisibility(View.VISIBLE);
                            }
                        }else{
                            success_message.setTextColor(getResources().getColor(R.color.red));
                            success_message.setText("No internet connection");
                            success_message.setVisibility(View.VISIBLE);
                        }
                    }else{
                        note.setError("Required");
                    }
                }else{
                    location.setError("Required");

                }
            }else{
                odometer.setError("Required");
            }

        }else{
            success_message.setTextColor(getResources().getColor(R.color.red));
            success_message.setText("Date Required");
            success_message.setVisibility(View.VISIBLE);
        }
    }


    public boolean save_service(){
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todo_all");
            String id = getIntent().getStringExtra("specific_todo_id");
            reference.child(firebaseUser.getUid()).child("todo_service").child(id).child("date").setValue(dateString);
            reference.child(firebaseUser.getUid()).child("todo_service").child(id).child("odometer").setValue(odoString);
            reference.child(firebaseUser.getUid()).child("todo_service").child(id).child("service_type").setValue(serviceString);
            reference.child(firebaseUser.getUid()).child("todo_service").child(id).child("location").setValue(locationString);
            reference.child(firebaseUser.getUid()).child("todo_service").child(id).child("notes").setValue(noteString);

        }catch (NullPointerException e){

        }
        return true;
    }

    //method to open datePicker
    EditText[] editText;
    TextView[] textView;
    public void showDatePicker(EditText[] editText, TextView[] textView){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        this.editText = editText;
        this.textView = textView;

        DatePickerDialog datePickerDialog = new DatePickerDialog(Todo_inner_Service.this, this, year, month, day);
        datePickerDialog.show();
    }

    int yr, mt, dy, hr, min;
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yr = year;
        mt = month + 1;
        dy = dayOfMonth;
        if(editText != null){
            for (int i = 0; i < editText.length; i++){
                editText[i].setText(yr + "-" + mt + "-" + dy);
            }
        }
        if(textView != null){
            for (int i = 0; i < textView.length; i++){
                textView[i].setText(yr + "-" + mt + "-" + dy);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
