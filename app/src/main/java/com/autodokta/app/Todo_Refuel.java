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

public class Todo_Refuel extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    ProgressBar loading;
    Spinner fuel;
    EditText odometer_input,gas_input,dateEditText;
    String dateString,odoString,gasString,fuelString;
    Button save;
    DatabaseReference databaseReference;
    TextView success_message, date_input;
    Button saverefuel;
    FirebaseUser firebaseUser;
    String[] fuel_list = {"Diesel","CNG","Electric","Ethanol","Gas Midgrade","Gas Premium","Gasoline","LPG"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo__refuel);
        getSupportActionBar().setTitle("Refuel");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();// initialization of user

        loading = (ProgressBar) findViewById(R.id.loading);
        date_input = (TextView) findViewById(R.id.date_input);
        odometer_input = (EditText)findViewById(R.id.odometer_input);
        gas_input = (EditText)findViewById(R.id.gas_input);
        fuel = (Spinner)findViewById(R.id.fuel_spinner);
        success_message = (TextView)findViewById(R.id.success_message);
        saverefuel = (Button) findViewById(R.id.save);


        //setting the adapter for the fuel spinner
        fuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fuelString = fuel_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fuelString = "Diesel";
            }
        });
        final ArrayAdapter<String> the_refuel = new ArrayAdapter<String>(Todo_Refuel.this,android.R.layout.simple_list_item_1,fuel_list);
        fuel.setAdapter(the_refuel);


        //setting onclick for setting the date from the date picker
        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] thedate = {date_input};
                showDatePicker(null,thedate);
            }
        });

        //setting the onclick listener for the save button
        saverefuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    validateFields_and_upload();
            }
        });

    }

    private void validateFields_and_upload() {
        success_message.setVisibility(View.GONE);

        //getting values from edittexts
        dateString = date_input.getText().toString();
        odoString = odometer_input.getText().toString().trim();
        gasString = gas_input.getText().toString().trim();

        if(!dateString.equals("Select date")){
            if(!odoString.equals("")){
                if(!gasString.equals("")){
                    if(isNetworkAvailable()){
                        loading.setVisibility(View.VISIBLE);
                        if(save_refuel()){
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
                    gas_input.setError("Required");

                }
            }else{
                odometer_input.setError("Required");
            }

        }else{
            success_message.setTextColor(getResources().getColor(R.color.red));
            success_message.setText("Date Required");
            success_message.setVisibility(View.VISIBLE);
        }
    }

    public boolean save_refuel(){
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("todo_all");
            String id = getIntent().getStringExtra("specific_todo_id");
            reference.child(firebaseUser.getUid()).child("todo_refuel").child(id).child("date").setValue(dateString);
            reference.child(firebaseUser.getUid()).child("todo_refuel").child(id).child("odometer").setValue(odoString);
            reference.child(firebaseUser.getUid()).child("todo_refuel").child(id).child("gas").setValue(gasString);
            reference.child(firebaseUser.getUid()).child("todo_refuel").child(id).child("fuel_type").setValue(fuelString);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(Todo_Refuel.this, this, year, month, day);
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
