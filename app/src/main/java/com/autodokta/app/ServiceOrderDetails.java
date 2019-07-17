package com.autodokta.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceOrderDetails extends AppCompatActivity {

    Button order;
    TextView servicetype, servicedescriptionTextView;
    Intent getService;

    String servicename, servicedescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_details);

        getSupportActionBar().setTitle("Order Service");
        getService = getIntent();

        order = (Button) findViewById(R.id.order);
        servicetype = (TextView) findViewById(R.id.servicetype);
        servicedescriptionTextView = (TextView) findViewById(R.id.service_description);


        servicename = getService.getStringExtra("servicetype");
        servicedescription = getService.getStringExtra("serviceDescription");

        servicetype.setText(servicename);
        servicedescriptionTextView.setText(servicedescription);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceOrderDetails.this, "Coming Soon",Toast.LENGTH_LONG).show();
            }
        });
    }
}

