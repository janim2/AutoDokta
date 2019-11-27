package com.autodokta.app;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ServicesActivity extends AppCompatActivity {

    Button submitted;
    LinearLayout car_insuranceLayout, aircondtioningLayout, autoMechanicLayout,brakepadLayout, brakefluidLayout,punctureLayout, engineoilLayout, exhausstLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        final Intent putServiceIntent = new Intent(ServicesActivity.this,ServiceOrderDetails.class);

        getSupportActionBar().setTitle("Services");

        submitted = (Button)findViewById(R.id.submitted);
        car_insuranceLayout = (LinearLayout) findViewById(R.id.car_insurance);
        aircondtioningLayout = (LinearLayout) findViewById(R.id.air_conditioning);
        autoMechanicLayout = (LinearLayout) findViewById(R.id.auto_mechanic);
//        brakepadLayout = (LinearLayout)findViewById(R.id.brakereplcement);
        brakefluidLayout = (LinearLayout)findViewById(R.id.bfluidreplacememt);
        punctureLayout = (LinearLayout)findViewById(R.id.puncture);
        engineoilLayout = (LinearLayout)findViewById(R.id.engineoil);
//        exhausstLayout = (LinearLayout)findViewById(R.id.exhaust);

        car_insuranceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putServiceIntent.putExtra("servicetype","You are about to order for the initiation and Processing Of Car Insurance Documents.");
                putServiceIntent.putExtra("serviceDescription","An auto insurance is a policy purchased by vehicle owners to mitigate costs associated with getting into an auto accident. Instead of paying out of pocket for auto accidents, people pay annual premiums to an auto insurance company; the company then pays all or most of the costs associated with an auto accident or other vehicle damage.");
                startActivity(putServiceIntent);

            }
        });


        aircondtioningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putServiceIntent.putExtra("servicetype","Air conditioning mechanic is on standby to receive your order");
                putServiceIntent.putExtra("serviceDescription","Air conditioning (often referred to as AC, A/C, or air con) is the process of removing heat and moisture from the interior of an occupied space to improve the comfort of occupants. Air conditioning can be used in both domestic and commercial environments.");
                startActivity(putServiceIntent);

            }
        });

        autoMechanicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putServiceIntent.putExtra("servicetype","Automatic diagnoses of your vehicle.");
                putServiceIntent.putExtra("serviceDescription","Keeps equipment available for use by inspecting and testing vehicles; completing preventive maintenance such as, engine tune-ups, oil changes, tire rotation and changes, wheel balancing, replacing filters. ... Maintains vehicle records by recording service and repairs.");
                startActivity(putServiceIntent);

            }
        });

        brakefluidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                putServiceIntent.putExtra("servicetype","You are about to order for a Change of your Brake Fluid");
                putServiceIntent.putExtra("serviceDescription","Brake Fluid helps in lubricating the braking system. Reduction of this liquid is bad as brake failure risk is increased.");
                startActivity(putServiceIntent);
            }
        });

        punctureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putServiceIntent.putExtra("servicetype","Fixing or getting a Tire Change is just a click away");
                putServiceIntent.putExtra("serviceDescription","A flat tire (British English: flat tyre) is a deflated pneumatic tire, which can cause the rim of the ... of the tire by a sharp object, such as a nail, letting air escape. Depending on the size of the puncture, the tire may deflate slowly or rapidly.");
                startActivity(putServiceIntent);
            }
        });

        engineoilLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putServiceIntent.putExtra("servicetype","Your engine would be grateful after this oil change. Make your change proud");
                putServiceIntent.putExtra("serviceDescription","Engine oil becomes dirty over time reducing engine efficiency in the long run. Regular oil change is important.");
                startActivity(putServiceIntent);
            }
        });

//        exhausstLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                putServiceIntent.putExtra("servicetype","You are about to order for an Exhaust adjustment or Repair");
//                startActivity(putServiceIntent);
//            }
//        });


        submitted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServicesActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
            }
        });
    }
}
