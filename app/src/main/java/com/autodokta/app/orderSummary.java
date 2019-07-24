package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class orderSummary extends AppCompatActivity {

    ImageView paymentImage;
    TextView totalPayment, totalAmount, thingsprize,address, changetheaddress, changepayment;
    Button confirm, gotoCart;
    String totalPrize,customeraddress,spaymenttype;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    Intent summaryIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        getSupportActionBar().setTitle("Order Summary");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        summaryIntent = getIntent();
        paymentImage = (ImageView) findViewById(R.id.paymentlogo);
        totalPayment = (TextView) findViewById(R.id.paymenttypetext);
        totalAmount = (TextView) findViewById(R.id.summarytotal);
        thingsprize = (TextView) findViewById(R.id.sumaryprizes);
        address = (TextView) findViewById(R.id.summary_address);
        changetheaddress = (TextView) findViewById(R.id.changetheaddress);
        changepayment = (TextView) findViewById(R.id.changepayment);
        confirm = (Button) findViewById(R.id.confirm);
        gotoCart = (Button) findViewById(R.id.modifyCart);

        getAddress();

        try{
            totalPrize = summaryIntent.getStringExtra("orderTotal");
            spaymenttype = summaryIntent.getStringExtra("paymentType");
            thingsprize.setText("GHC "+String.valueOf(Float.valueOf(totalPrize) + ".00"));
            totalAmount.setText("GHC "+String.valueOf(Float.valueOf(totalPrize) + 10.00)+"0");
        }catch (NullPointerException e){

        }

        if(spaymenttype.contains("auto")){
            paymentImage.setImageResource(R.mipmap.ic_launcher);
            totalPayment.setText("Pay with AutoDokta Pay.");
        }
        else{
            paymentImage.setImageResource(R.drawable.mtnlogo);
            totalPayment.setText("Pay with MTN Mobile Money.");
        }

        changetheaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochangeaddress = new Intent(orderSummary.this,ChangeAdress.class);
                gotochangeaddress.putExtra("wheredidicomefrom","orderSummary");
                startActivity(gotochangeaddress);
            }
        });

        changepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Integer d = random.nextInt(34354466);
                String order_number = 303+d+"";

                try{
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("orders").child(user.getUid()).child(order_number).child("itemPrize").setValue(totalPrize);
                        mDatabase.child("orders").child(user.getUid()).child(order_number).child("orderStatus").setValue("Processing");
//                      mDatabase.child("orders").child(user.getUid()).child(order_number).child("payment").setValue("No");
                        Intent completeOrder = new Intent(orderSummary.this, orderComplete.class);
                        completeOrder.putExtra("orderNumber",order_number);
                        startActivity(completeOrder);
//                mDatabase.child("orders").child(user.getUid()).child(order_number).child("itemid").setValue(itemid);

                }
                catch (NullPointerException e){

                }

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
                                customeraddress = child.getValue().toString();
                                address.setText(customeraddress);
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
