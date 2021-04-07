package com.autodokta.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autodokta.app.Models.CategorisedServicesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CategorisedDetails extends AppCompatActivity {
    private static final String TAG = CategorisedDetails.class.getSimpleName();
    Button call;
    TextView  fullDescription, titleTextView;
    TextView priceTextView,mobile,location;
    ImageView imageView;
    Intent getService;

    String imgString,titleString,priceString,shortDescString,idString,locString,mobileString;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorised_details);

        getSupportActionBar().setTitle("Service Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();




        getService = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();

        imageView = (ImageView)findViewById(R.id.categorisedImage);
        titleTextView = (TextView)findViewById(R.id.name);
        priceTextView = (TextView)findViewById(R.id.categorisedPrice);
        fullDescription = (TextView)findViewById(R.id.categorisedDesc);
        mobile = (TextView)findViewById(R.id.userMobile);
        call = (Button)findViewById(R.id.call);
        location = (TextView)findViewById(R.id.userLocation);


        imgString = getService.getStringExtra("img");
        titleString = getService.getStringExtra("title");
        priceString = getService.getStringExtra("price");
        shortDescString = getService.getStringExtra("short_description");
        idString = getService.getStringExtra("id");
        mobileString = getService.getStringExtra("seller_number");




        //prep work before image is loaded is to load it into the cache
        DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                defaultDisplayImageOptions(theImageOptions).build();
        ImageLoader.getInstance().init(config);

        String imageLink = imgString;
        imageLoader.displayImage(imageLink,imageView);

        titleTextView.setText(titleString);
        priceTextView.setText("GHS " + priceString);
        fullDescription.setText(shortDescString);

//        FullDescription();

//        logic for clicking call button
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch data based on the service id.
                mobile.setText(mobileString);

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void FullDescription(){

        //fetch data based on the service id.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        fetchFullDescription(child.getKey());


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchFullDescription(String key){
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference("services").child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        if (child.getKey().equals("full description")){
//                            fullDescription.setText(child.getValue().toString());
                        }

                        if (child.getKey().equals("location")){
//                            location.setText(child.getValue().toString());
                        }

//                        if (child.getKey().equals("seller_number")){
//                            String seller = child.getValue().toString();
//                            call = (Button)findViewById(R.id.call);
//                            call.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                    callIntent.setData(Uri.parse(seller));
//                                    if (callIntent.resolveActivity(getPackageManager()) != null){
//                                        startActivity(callIntent);
//                                    }
////
//                                }
//                            });
//                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


























}
