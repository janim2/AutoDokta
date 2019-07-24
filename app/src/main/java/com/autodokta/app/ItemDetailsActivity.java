package com.autodokta.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.Adapters.Related_items_PartsAdapter;
import com.autodokta.app.helpers.Space;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Random;


public class ItemDetailsActivity extends AppCompatActivity {

    String spartid, simage, sname, sprice, sdescription, sSellersNumber;

    Button buyNow;
    ImageView image;
    TextView name, description, price;

//    initialization of image loader
    ImageLoader imageLoader = ImageLoader.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    FirebaseUser user;
    String item = "";

    ArrayList relatedParts = new ArrayList<CarParts>();
    RecyclerView related_items_RecyclerView;
    RecyclerView.Adapter related_items_mPostAdapter;
    RecyclerView.LayoutManager related_items_mPostLayoutManager;
    String related_item_imageurl, related_item_name, related_item_description, related_item_price, related_item_sellersNumber;

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

//        related items pickup logic starts here

        related_items_RecyclerView = findViewById(R.id.recyclerView_relatedProducts);
        related_items_RecyclerView.setHasFixedSize(true);

//        related_items_mPostLayoutManager = new LinearLayoutManager(ItemDetailsActivity.this);
//        related_items_RecyclerView.setLayoutManager(related_items_mPostLayoutManager);

        getRelatedItems_ID();
        related_items_mPostAdapter = new Related_items_PartsAdapter(getrelatedParts(),ItemDetailsActivity.this);
        related_items_RecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
        related_items_RecyclerView.addItemDecoration(new Space(2,20,true,0));
        related_items_RecyclerView.setAdapter(related_items_mPostAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String sharebody = simage;
                share.putExtra(Intent.EXTRA_SUBJECT,sname);
                share.putExtra(Intent.EXTRA_TEXT,sharebody);
                startActivity(Intent.createChooser(share,"Share via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRelatedItems_ID() {
        if(spartid.contains("offer")){
            getItemIDs("Offers");
        }
        if(spartid.contains("brake")){
            getItemIDs("Brakes");
        }
        if(spartid.contains("engine")){
            getItemIDs("Engines");
        }
        if(spartid.contains("exterior")){
            getItemIDs("ExteriorParts");
        }
        if(spartid.contains("light")){
            getItemIDs("HeadLights");
        }
        if(spartid.contains("interior")){
            getItemIDs("InteriorParts");
        }
        if(spartid.contains("tool")){
            getItemIDs("Tools");
        }
        if(spartid.contains("wheel")){
            getItemIDs("Wheels");
        }
    }

    private void getItemIDs(final String which_item) {
        try{
            DatabaseReference partdatabase = FirebaseDatabase.getInstance().getReference().child("carparts").child(which_item);

            //limiting number of items to be fetched
            Query query = partdatabase.limitToLast(3);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                            //randomly selecting items to display
//                            int ads = (int) dataSnapshot.getChildrenCount();
//                            int rand = new Random().nextInt(ads);
                        for(DataSnapshot child : dataSnapshot.getChildren()){
//                            for(DataSnapshot datas: child.getChildren()){
//                                for(int i= 0; i < rand;i++){
                                    FetchParts(child.getKey(), which_item);
//                                }
                            }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ItemDetailsActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }

    }

    private void FetchParts(final String key, String which_of_the_items) {
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("carparts").child(which_of_the_items).child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            related_item_imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            related_item_name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            related_item_description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            related_item_price = child.getValue().toString();
                        }

                        if(child.getKey().equals("buyersNumber")){
                            related_item_sellersNumber = child.getValue().toString();
                        }


                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    String partid = key;
                    boolean isNew = false;
                    CarParts obj = new CarParts(partid,related_item_imageurl,related_item_name,related_item_description,
                            related_item_price, isNew, related_item_sellersNumber);
                    relatedParts.add(obj);
                    related_items_RecyclerView.setAdapter(related_items_mPostAdapter);
                    related_items_mPostAdapter.notifyDataSetChanged();
//                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemDetailsActivity.this,"Cancelled",Toast.LENGTH_LONG).show();

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

    public ArrayList<CarParts> getrelatedParts(){
        return  relatedParts;
    }


}
