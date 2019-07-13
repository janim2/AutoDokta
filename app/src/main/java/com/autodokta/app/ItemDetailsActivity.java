package com.autodokta.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class ItemDetailsActivity extends AppCompatActivity {

    String spartid, simage, sname, sprice, sdescription, sSellersNumber;

    Button buyNow;
    ImageView image;
    TextView name, description, price;

    ImageLoader imageLoader = ImageLoader.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    FirebaseUser user;
    String item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();

        image = findViewById(R.id.image1);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        buyNow = findViewById(R.id.buyNow);


        spartid = intent.getStringExtra("partid");
        simage = intent.getStringExtra("theimage");
        sname = intent.getStringExtra("thename");
        sprice = intent.getStringExtra("theprice");
        sdescription = intent.getStringExtra("thedescription");
        sSellersNumber = intent.getStringExtra("thesellersNumber");

        //prep work before image is loaded is to load it into the cache
        DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                defaultDisplayImageOptions(theImageOptions).build();
        ImageLoader.getInstance().init(config);
//
        findoutifthere();
        String imagelink = simage;
        imageLoader.displayImage(imagelink,image);

        name.setText(sname);
        description.setText(sdescription);
        price.setText(sprice);

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null){
                       if(true){
                           ViewCartDialogue cartDialogue = new ViewCartDialogue();
                           cartDialogue.showDialog(ItemDetailsActivity.this,"Success! "+ sname, user.getUid(),spartid);
                       }
                       else{
                           buyNow.setText("Already Added To Cart");
                           buyNow.setClickable(false);
                       }
               }else{
                   ViewLoginDialogue dialogue = new ViewLoginDialogue();
                   dialogue.showDialog(ItemDetailsActivity.this,"Action Needed To Continue");
               }
            }
        });

    }

    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + sSellersNumber));
        startActivity(intent);
    }

    public boolean findoutifthere() {
        user = firebaseAuth.getCurrentUser();

        if(user!=null){

            reference = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid()).child(spartid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals(spartid)){
                                item = child.getValue().toString();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return true;
        }
        return true;
    }


}
