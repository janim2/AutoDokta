package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeAdress extends AppCompatActivity {

    EditText editfirstname,editlastname,editphonenumber,editaddress;
    Spinner region, city;
    Button editbutton,editcart;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String oldaddress,newaddress,oldfirstname,newfirstname,oldlastname,newlastname,oldnumber,newnumber,sregion,scity;
    ProgressBar loading;
    Intent changeAddressIntent;
    String wherefrom;
    String[] region_list = {"Select Region","Region not listed","Ahafo Region","Ashanti Region","Bono East Region","Brong Ahafo Region","Central Region"," Eastern Region",
            "Greater Accra Region","North East Region","Northern Region","Oti Region","Savannah Region","Upper East",
            "Upper West Region","Volta Region","Western North Region","Western Region"};
    String[] city_list = {"Accra","Cape Coast"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_adress);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        changeAddressIntent = getIntent();
        getAddress();
        editbutton = (Button)findViewById(R.id.editbutton);
        editcart = (Button)findViewById(R.id.editCart);
        editfirstname = (EditText) findViewById(R.id.firstname_address);
        editlastname = (EditText) findViewById(R.id.lastname_address);
        editphonenumber = (EditText) findViewById(R.id.mobile_number_address);
        editaddress = (EditText) findViewById(R.id.editaddress);
        region = (Spinner) findViewById(R.id.state_change);
        city = (Spinner) findViewById(R.id.address_city);
        loading = (ProgressBar) findViewById(R.id.loading);

        try {
            wherefrom = changeAddressIntent.getStringExtra("wheredidicomefrom");

        }catch (NullPointerException e){

        }

        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sregion = region_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sregion = "Greater Accra";
            }
        });
        ArrayAdapter<String> regions = new ArrayAdapter<String>(ChangeAdress.this,android.R.layout.simple_list_item_1,region_list);
        region.setAdapter(regions);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scity = city_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                scity = "East Legon";
            }
        });
        ArrayAdapter<String> thecity = new ArrayAdapter<String>(ChangeAdress.this,android.R.layout.simple_list_item_1,city_list);
        city.setAdapter(thecity);


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loading.setVisibility(View.VISIBLE);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    newaddress = editaddress.getText().toString().trim();
                    newfirstname = editfirstname.getText().toString().trim();
                    newlastname = editlastname.getText().toString().trim();
                    newnumber = editphonenumber.getText().toString().trim();
                    mDatabase.child("users").child(user.getUid()).child("address").setValue(newaddress);
                    mDatabase.child("users").child(user.getUid()).child("firstname").setValue(newfirstname);
                    mDatabase.child("users").child(user.getUid()).child("lastname").setValue(newlastname);
                    mDatabase.child("users").child(user.getUid()).child("number").setValue(newnumber);
                    mDatabase.child("users").child(user.getUid()).child("region").setValue(sregion);
                    mDatabase.child("users").child(user.getUid()).child("city").setValue(scity);

                    loading.setVisibility(View.GONE);
                    Toast.makeText(ChangeAdress.this,"Address Change Successful",Toast.LENGTH_LONG).show();

                    if(wherefrom.equals("completeOrder")){
                        Intent intent = new Intent(getApplicationContext(), CompleteOrder.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    if(wherefrom.equals("orderSummary")){
                        Intent intent = new Intent(getApplicationContext(), orderSummary.class);
                        intent.putExtra("paymentType","mtn");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }catch (NullPointerException e){

                }
            }
        });

        editcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getAddress() {
        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("address")) {
                                oldaddress = child.getValue().toString();
                            }
                            if(child.getKey().equals("firstname")) {
                                oldfirstname = child.getValue().toString();
                            }
                            if(child.getKey().equals("lastname")) {
                                oldlastname = child.getValue().toString();
                            }
                            if(child.getKey().equals("number")) {
                                oldnumber = child.getValue().toString();
                            }
                                try {
                                    editaddress.setText(oldaddress);
                                    editfirstname.setText(oldfirstname);
                                    editlastname.setText(oldlastname);
                                    editphonenumber.setText(oldnumber);
                                }catch (NullPointerException e){


                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }catch (NullPointerException e){

        }
    }
}
