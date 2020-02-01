package com.autodokta.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentMethod extends AppCompatActivity {

//    ExpandableListView listView;
//    ExpandableListAdapter listAdapter;
//    List<String> listDataHeader;
//
//    HashMap<String, List<String>> listHashMap;

    RadioButton autoRbutton,mtnRadio, mobile_money_payment;
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
        mobile_money_payment = (RadioButton) findViewById(R.id.mobile_money_payment);
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

//                    if(mtnRadio.isChecked()){
//                        Intent ordersummary = new Intent(PaymentMethod.this,orderSummary.class);
//                        ordersummary.putExtra("orderTotal", totalSale);
//                        ordersummary.putExtra("paymentType","mtn");
//                        startActivity(ordersummary);
//                    }else{
//                    Toast.makeText(PaymentMethod.this,"Select Payment Method",Toast.LENGTH_LONG).show();
//                }

                if(mobile_money_payment.isChecked()){
                    Flutterwave_payment();
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

    private void Flutterwave_payment(){

        new RavePayManager(PaymentMethod.this).setAmount(Double.valueOf(totalSale))
                .setCountry("GH")
                .setCurrency("GHS")
                .setPublicKey("FLWPUBK-9f910be2cca606f52d4d0914badb51ec-X")
                .setEncryptionKey("cdfdb7d775ffbd5216cf6884")
                .setfName("Jesse")
                .setlName("Anim")
                .setEmail("iam@gmail.com")
                .setNarration("AutoDokta Payment")
                .acceptGHMobileMoneyPayments(false)
                .acceptCardPayments(true)
                .allowSaveCardFeature(true)
                .onStagingEnv(true)
                .initialize();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
