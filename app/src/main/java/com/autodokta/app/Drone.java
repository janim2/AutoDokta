package com.autodokta.app;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static android.widget.Toast.LENGTH_LONG;

public class Drone extends AppCompatActivity {


    private EditText firstname, lastname,phone_number, pickup, drop_off,item;

    private Button  requestDroneBtn;

    private ProgressBar progressBar;

    private FirebaseAuth auth;

    private DatabaseReference mDatabase;

    private String              sfirstname, slastname,stelephone,spick,sdropOff,sitem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone);

        getSupportActionBar().setTitle("Drone Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        requestDroneBtn           =   findViewById(R.id.drone_request_btn);


        firstname           =   findViewById(R.id.droneFirstName);
        lastname            =   findViewById(R.id.droneLastName);
        phone_number        =   findViewById(R.id.dronePhone);
        pickup        =   findViewById(R.id.pickup);
        drop_off = findViewById(R.id.drop_off);
        item = findViewById(R.id.delivery_item);



        progressBar = (ProgressBar) findViewById(R.id.progressBar);















        requestDroneBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                //logic for a individual starts here
                sfirstname  =   firstname.getText().toString().trim();
                slastname   =   lastname.getText().toString().trim();
                stelephone  =   phone_number.getText().toString().trim();
                spick = pickup.getText().toString().trim();
                sdropOff = drop_off.getText().toString().trim();
                sitem = item.getText().toString().trim();



                if (TextUtils.isEmpty(sfirstname)) {
                    firstname.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(slastname)) {
                    lastname.setError("Required");
                    return;
                }



                if (TextUtils.isEmpty(stelephone)) {
                    phone_number.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(spick)){
                    pickup.setError("Enter pickup location");
                    return;
                }

                if (TextUtils.isEmpty(sdropOff)){
                    drop_off.setError("Enter drop off location");
                    return;
                }

                if (TextUtils.isEmpty(sitem)){
                    item.setError("Enter delivery item");
                    return;
                }

                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");

                String formattedDate = localDateTime.format(formatter);
                String requestDate = formattedDate;

                saveDeliveryInfo(sfirstname,slastname,stelephone,spick,sdropOff,sitem, requestDate);
                progressBar.setVisibility(View.GONE);











            }
        });
//    end of registration logic
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }






    private void saveDeliveryInfo(String fname, String lname, String phoneNumber, String pickup, String dropOff, String item, String requestDate) {
        DatabaseReference info = mDatabase.child("DeliveryRequest").push();
        info.child("firstname").setValue(fname);
        info.child("lastname").setValue(lname);
        info.child("number").setValue(phoneNumber);
        info.child("pickup_location").setValue(pickup);
        info.child("dropOff_location").setValue(dropOff);
        info.child("delivery_item").setValue(item);
        info.child("delivery_date").setValue(requestDate);
        Toast.makeText(Drone.this, "Request successful.Will call you shortly", LENGTH_LONG).show();
        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.droneForm);
        clearForm(viewGroup);

    }

    private void clearForm(ViewGroup group){

//        ViewGroup  viewGroup = (ViewGroup)findViewById(R.id.service_form);
        for (int i=0, count = group.getChildCount(); i<count; i++){
            View view = group.getChildAt(i);
            if (view instanceof  EditText){
                ((EditText)view).setText("");
            }



            if (view instanceof ViewGroup && ((ViewGroup)view).getChildCount()>0){
                clearForm((ViewGroup)view);
            }
        }

    }







}
