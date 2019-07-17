package com.autodokta.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    EditText nameEdt,messageEdt;
    DatePicker datePicker;
    FirebaseDatabase database;
    Button addButton;
    FirebaseUser user;
    TextView todo_date;
    String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        user = FirebaseAuth.getInstance().getCurrentUser();
        addButton = (Button)findViewById(R.id.add_task);
        todo_date = (TextView) findViewById(R.id.todo_date);
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

    }

    private void saveTodo() {
        // first section
        // get the data to save in our firebase db
        nameEdt = (EditText) findViewById(R.id.name);
        String nameString = nameEdt.getText().toString();
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, "enter title", Toast.LENGTH_SHORT).show();
            return;
        }

        messageEdt = (EditText) findViewById(R.id.message);
        String messageString = messageEdt.getText().toString();
        if (TextUtils.isEmpty(messageString)) {
            Toast.makeText(this, "enter message", Toast.LENGTH_SHORT).show();
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
        todo.setName(nameString);
        todo.setMessage(messageString);
        todo.setDate(dateString);
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
