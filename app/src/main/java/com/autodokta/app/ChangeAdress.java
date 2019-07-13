package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

    EditText editaddress;
    Button editbutton,editcart;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String oldaddress,newaddress;
    ProgressBar loading;
    Intent changeAddressIntent;
    String wherefrom;

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
        editaddress = (EditText) findViewById(R.id.editaddress);
        loading = (ProgressBar) findViewById(R.id.loading);

        try {
            wherefrom = changeAddressIntent.getStringExtra("wheredidicomefrom");

        }catch (NullPointerException e){

        }
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loading.setVisibility(View.VISIBLE);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    newaddress = editaddress.getText().toString().trim();
                    mDatabase.child("users").child(user.getUid()).child("address").setValue(newaddress);
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
                            if(child.getKey().equals("address")){
                                oldaddress = child.getValue().toString();
                                try {
                                    editaddress.setText(oldaddress);
                                }catch (NullPointerException e){

                                }
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
