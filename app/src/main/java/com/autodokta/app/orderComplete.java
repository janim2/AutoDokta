package com.autodokta.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class orderComplete extends AppCompatActivity {

    Button seeorders, continueShopping;
    Intent orderCompleteIntent;
    String ordernumber;
    TextView ordernumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        seeorders = (Button)findViewById(R.id.seeorders);
        continueShopping = (Button) findViewById(R.id.continuetoShop);
        ordernumberTextView = (TextView) findViewById(R.id.ordernumber);

        orderCompleteIntent = getIntent();

        try {
            ordernumber = orderCompleteIntent.getStringExtra("orderNumber");
            ordernumberTextView.setText("Order Number "+ordernumber);
        }catch (NullPointerException e){

        }

        seeorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(orderComplete.this,Garage.class));
            }
        });

        continueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}