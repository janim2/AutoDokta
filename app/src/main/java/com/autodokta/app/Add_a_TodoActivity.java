package com.autodokta.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Add_a_TodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText nameEdt,messageEdt,vehicle_name,car_model, fuel_capacity;
    Spinner year,vehicle_spinner,manufacturer_spinner,fuel_spinner;
    DatePicker datePicker;
    FirebaseDatabase database;
    Button addButton;
    FirebaseUser user;
    TextView todo_date;
    String dateString, svehicle, sfuel, smanufacturer, scar_year;
    String[] vehicle_list = {"car","motocycle","bus","truck"};

    String[] fuel_list= {"Liquids","Liquefied petroleum gas","Compressed natural gas","Electrical"};

    String[] manufacturer_list = {"American Motors","Astra","Audi","BMW","Bugatti","Chevrolet","Chrysler",
            "Daewoo","Ford","Foton","Hummer","Hyundai","Jaguar","Jeep","Kawaski"};

    String[] year_list = {"1950","1951","1952","1953","1954","1955","1956","1957","1958","1959","1960",
            "1961","1962","1963","1964","1965","1966","1967","1968","1969","1970","1971","1972",
            "1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984",
            "1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996",
            "1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008",
            "2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        user = FirebaseAuth.getInstance().getCurrentUser();
        addButton = (Button)findViewById(R.id.add_task);
        todo_date = (TextView) findViewById(R.id.todo_date);
        vehicle_name = (EditText) findViewById(R.id.vehicle_name_edt);
        car_model = (EditText) findViewById(R.id.model);
        year = (Spinner) findViewById(R.id.year_spinner);
        fuel_capacity = (EditText) findViewById(R.id.capacity);
        vehicle_spinner = (Spinner) findViewById(R.id.vehicle_spinner);
        manufacturer_spinner = (Spinner) findViewById(R.id.manufacturer_spinner);
        fuel_spinner = (Spinner) findViewById(R.id.fuel_spinner);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTodo();
            }
        });

        todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] date_view = {todo_date};
                showDatePicker(null,date_view);
            }
        });

        //seeing the adapter for the year spinner
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scar_year = year_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                scar_year = "2000";
            }
        });
        ArrayAdapter<String> the_year = new ArrayAdapter<String>(Add_a_TodoActivity.this,android.R.layout.simple_list_item_1,year_list);
        year.setAdapter(the_year);


//        setting the adapater for the vehicle spinner
        vehicle_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                svehicle = vehicle_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                svehicle = "car";
            }
        });
        ArrayAdapter<String> the_vehicle = new ArrayAdapter<String>(Add_a_TodoActivity.this,android.R.layout.simple_list_item_1,vehicle_list);
        vehicle_spinner.setAdapter(the_vehicle);

//        setting the adapter for the manufacturer spinner
        manufacturer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smanufacturer = manufacturer_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                smanufacturer = "American Motors";
            }
        });
        ArrayAdapter<String> the_manufacturer = new ArrayAdapter<String>(Add_a_TodoActivity.this,android.R.layout.simple_list_item_1,manufacturer_list);
        manufacturer_spinner.setAdapter(the_manufacturer);


        //setting the adapter for the fuel spinner
        fuel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sfuel = fuel_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sfuel = "Liquefied petroleum gas";
            }
        });
        ArrayAdapter<String> the_fuel = new ArrayAdapter<String>(Add_a_TodoActivity.this,android.R.layout.simple_list_item_1,fuel_list);
        fuel_spinner.setAdapter(the_fuel);



    }

    private void saveTodo() {
        // first section
        // get the data to save in our firebase db
//        nameEdt = (EditText) findViewById(R.id.name);
//        String nameString = nameEdt.getText().toString();
//        if (TextUtils.isEmpty(nameString)) {
//            Toast.makeText(this, "enter title", Toast.LENGTH_SHORT).show();
//            return;
//        }

        messageEdt = (EditText) findViewById(R.id.message);
        String messageString = messageEdt.getText().toString();
        if (TextUtils.isEmpty(messageString)) {
            Toast.makeText(this, "enter message", Toast.LENGTH_SHORT).show();
            return;
        }

        String vehicle_nameString = vehicle_name.getText().toString();
        if (TextUtils.isEmpty(vehicle_nameString)) {
            Toast.makeText(this, "enter vehicle name", Toast.LENGTH_SHORT).show();
            return;
        }

        String vehicle_model = car_model.getText().toString();
        if (TextUtils.isEmpty(vehicle_model)) {
            Toast.makeText(this, "enter car model", Toast.LENGTH_SHORT).show();
            return;
        }


        String car_fuel_capacity = fuel_capacity.getText().toString();
        if (TextUtils.isEmpty(car_fuel_capacity)) {
            Toast.makeText(this, "enter fuel capacity", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!todo_date.equals("Select date")){
            dateString = todo_date.getText().toString();
        }else{
            Toast.makeText(Add_a_TodoActivity.this,"Please select date",Toast.LENGTH_LONG).show();
        }


//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        Calendar calendar = Calendar.getInstance();
//        if (calendar != null){
//
//        calendar.set(Calendar.MONTH,datePicker.getMonth());
//        calendar.set(Calendar.YEAR,datePicker.getYear());
//        calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
//
//        }
//
//        String dateString = formatter.format(calendar);


//        second section
//        save it in the firebase db
//        getting database reference
        database = FirebaseDatabase.getInstance();
//        writing data to the child with each unique key

        String key = database.getReference("todoList").child(user.getUid()).push().getKey();

        Todo todo = new Todo();
//        todo.setName(nameString);

        todo.setMessage(messageString);
        todo.setDate(dateString);
        todo.setVehicle_nameString(vehicle_nameString);
        todo.setVehicle_model(vehicle_model);
        todo.setCar_year(scar_year);
        todo.setCar_fuel_capacity(car_fuel_capacity);
        todo.setVehicle_type(svehicle);
        todo.setVehicle_fuel_type(sfuel);
        todo.setVehicle_manufacturer(smanufacturer);

//        converting the model class into a hashmap

        try{
            if(user != null){
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, todo.toFirebaseObject());
                database.getReference("todoList").child(user.getUid()).updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            finish();
                        } else {
                            startActivity(new Intent(Add_a_TodoActivity.this,Garage.class));
                            Toast.makeText(getApplicationContext(), "task added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (NullPointerException e){

        }
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(Add_a_TodoActivity.this, this, year, month, day);
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

}
