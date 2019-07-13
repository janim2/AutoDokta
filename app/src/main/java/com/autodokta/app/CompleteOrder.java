package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompleteOrder extends AppCompatActivity {
    ImageView goBack;
    Intent intent;
    String totalSale, address;
    TextView itemPrizes, totaltotal, addressTextView, chageaddress;
    Float totaltotalint;
    ProgressBar loading;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Button modifyCart,next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        getSupportActionBar().setTitle("Delivery");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        intent = getIntent();
        totalSale = intent.getStringExtra("totalSale");

//        goBack = (ImageView)findViewById(R.id.goorback);
        itemPrizes = (TextView) findViewById(R.id.itemprizes);
        totaltotal = (TextView) findViewById(R.id.totaltotal);
        loading = (ProgressBar) findViewById(R.id.loading);
        addressTextView = (TextView) findViewById(R.id.address);
        modifyCart = (Button) findViewById(R.id.modifyCart);
        next = (Button) findViewById(R.id.next);
        chageaddress = (TextView) findViewById(R.id.changeaddress);

        getAddress();

        itemPrizes.setText("GHC "+totalSale+".00");

        try {
            totaltotalint = Float.valueOf(totalSale) + 10;
            totaltotal.setText("GHC "+ totaltotalint+""+"0");
        }catch (NullPointerException e){

        }
//        goBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Cart.class);
//                startActivity(intent);
//            }
//        });

//        setting onclick for next button

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNext = new Intent(CompleteOrder.this,PaymentMethod.class);
                startNext.putExtra("totalSaleToPayment",totalSale);
               startActivity(startNext);
            }
        });

//        setting onclick for modify cart button
        modifyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chageaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochageaddress = new Intent(CompleteOrder.this,ChangeAdress.class);
                gotochageaddress.putExtra("wheredidicomefrom","completeOrder");
                startActivity(gotochageaddress);
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
                                address = child.getValue().toString();
                                addressTextView.setText(address);

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
