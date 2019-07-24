package com.autodokta.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.ExpandablelistAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentMethod extends AppCompatActivity {

//    ExpandableListView listView;
//    ExpandableListAdapter listAdapter;
//    List<String> listDataHeader;
//
//    HashMap<String, List<String>> listHashMap;

    RadioButton autoRbutton,mtnRadio;
    LinearLayout autoRLayout,mtnLayout;
    Button next, modifyCart;
    TextView itemtotal, totaltotal;
    Intent paymentIntent;
    String totalSale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        getSupportActionBar().setTitle("Payment");

//        autoRbutton = (RadioButton) findViewById(R.id.autodpayRadio);
        mtnRadio = (RadioButton) findViewById(R.id.mtnRadio);
//        autoRLayout = (LinearLayout) findViewById(R.id.atpayLayout);
        mtnLayout = (LinearLayout) findViewById(R.id.mtnlayout);

        next = (Button) findViewById(R.id.next);
        modifyCart = (Button) findViewById(R.id.modifyCart);
        itemtotal = (TextView) findViewById(R.id.itemprizes);
        totaltotal = (TextView) findViewById(R.id.totaltotal);

        paymentIntent = getIntent();

        try{
            totalSale = paymentIntent.getStringExtra("totalSaleToPayment");
            itemtotal.setText("GHC " + totalSale+".00");
            totaltotal.setText("GHC " + String.valueOf(Float.valueOf(totalSale) + 10.00)+"0");
        }catch (NullPointerException e){

        }

//        autoRbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autoRbutton.isChecked();
//                autoRLayout.setVisibility(View.VISIBLE);
//                mtnLayout.setVisibility(View.GONE);
//            }
//        });

        mtnRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtnRadio.isChecked();
//                autoRLayout.setVisibility(View.GONE);
                mtnLayout.setVisibility(View.VISIBLE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(mtnRadio.isChecked() ){ //|| autoRbutton.isChecked()){
//                    if(autoRbutton.isChecked()){
//                        Intent ordersummary = new Intent(PaymentMethod.this,orderSummary.class);
//                        ordersummary.putExtra("orderTotal", totalSale);
//                        ordersummary.putExtra("paymentType","autoDokta");
//                        startActivity(ordersummary);
//                    }

                    if(mtnRadio.isChecked()){
                        Intent ordersummary = new Intent(PaymentMethod.this,orderSummary.class);
                        ordersummary.putExtra("orderTotal", totalSale);
                        ordersummary.putExtra("paymentType","mtn");
                        startActivity(ordersummary);
                    }else{
                    Toast.makeText(PaymentMethod.this,"Select Payment Method",Toast.LENGTH_LONG).show();
                }

            }
        });

        modifyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

//        listView = (ExpandableListView)findViewById(R.id.paymentlistView);
////        initiateData();
//        listAdapter = new ExpandablelistAdapter(PaymentMethod.this,listDataHeader,listHashMap);
//        listView.setAdapter(listAdapter);
    }

//    private void initiateData() {
//
//        listDataHeader = new ArrayList<>();
//        listHashMap = new HashMap<>();
//
//        listDataHeader.add("AutoDokta Pay");
//        listDataHeader.add("MTN Mobile Money");
//
//        List<String> autoDPay = new ArrayList<>();
//        autoDPay.add("Password");
//
//        List<String> mtnMomo = new ArrayList<>();
//        mtnMomo.add("MTN Mobile Money Payment");
//    }
}
