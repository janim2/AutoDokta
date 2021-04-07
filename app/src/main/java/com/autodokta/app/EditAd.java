package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autodokta.app.Models.UserAds;
import com.autodokta.app.helpers.FireDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EditAd extends AppCompatActivity {

    private static final String TAG = EditAd.class.getSimpleName();

    private  EditText ad_title, ad_price,location;
    private String titleStr, priceStr, fullStr, locStr, idStr;

    Bundle bundle;
    private Button edit;

    DatabaseReference reference;
    FirebaseDatabase database;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ad);




        ad_title = (EditText)findViewById(R.id.edit_title);
        ad_price = (EditText)findViewById(R.id.edit_price);

        location = (EditText)findViewById(R.id.edit_location);
        edit = (Button)findViewById(R.id.edit_button);

        bundle = getIntent().getExtras();
        if (bundle != null){

            idStr = bundle.getString("key");
            Log.i(TAG, "Intent id: " + idStr);
            System.out.println("id: " + idStr);
            titleStr = bundle.getString("title");
            priceStr = bundle.getString("price");
            locStr = bundle.getString("location");
        }







        ad_title.setText(titleStr);
        ad_price.setText(priceStr);
        location.setText(locStr);

        String title = ad_title.getText().toString();
        Log.i(TAG,"title: " + title);
        String price = ad_price.getText().toString();
        Log.i(TAG, "price: " + price);
        String locat = location.getText().toString();
        Log.i(TAG, "location: " + locat);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                UserAds userAds = new UserAds();
//
//                userAds.setTitle(title);
//                userAds.setPrice(price);
//                userAds.setLocation(locat);
//
//                System.out.println("intent id: " + idStr);
//                System.out.println("title: " + titleStr);
//
                overridePost(idStr,title,price,locat);








            }
        });


    }

//    end of oncreate method
    private void overridePost(String key, String title, String price, String location){

       database = FirebaseDatabase.getInstance();
       reference = database.getReference().child("services");
        reference.child(key).child("title").setValue(title);
        reference.child(key).child("price").setValue(price);
        reference.child(key).child("location").setValue(location);

        Toast.makeText(EditAd.this, "update successful", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
