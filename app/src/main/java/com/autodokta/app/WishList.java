package com.autodokta.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.Adapters.WishListAdapter;
import com.autodokta.app.helpers.Space;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishList extends AppCompatActivity {

//    strings for loading the parts from the database
    private ArrayList wishlistParts = new ArrayList<CarParts>();
    private RecyclerView wishlistRecyclerView;
    private RecyclerView.Adapter mwishlistAdapter;
    private RecyclerView.LayoutManager mwishlistLayoutManager;
    private String imageurl, name, views, description, price, sellersNumber,product_rating;
    private ProgressBar loading;
    private TextView nowishItems;
//    strings ends here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        getSupportActionBar().setTitle("WishList");

        nowishItems = (TextView)findViewById(R.id.no_wish_items);

        nowishItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getwishlistParts();
            }
        });

        //items to load from the database starts here
        loading  = (ProgressBar)findViewById(R.id.loading);
        nowishItems  = (TextView) findViewById(R.id.no_wish_items);

        wishlistRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        wishlistRecyclerView.setHasFixedSize(true);

//        mwishlistLayoutManager = new GridLayoutManager(WishList.this,2, LinearLayoutManager.VERTICAL,false);
        mwishlistLayoutManager = new LinearLayoutManager(WishList.this);
        wishlistRecyclerView.setLayoutManager(mwishlistLayoutManager);

        if(isNetworkAvailable()){
            getwishlistPartsIds();
        }else{
            loading.setVisibility(View.GONE);
            Toast.makeText(WishList.this,"No internet connection",Toast.LENGTH_LONG).show();
        }

        mwishlistAdapter = new WishListAdapter(getwishlistParts(),WishList.this);

        wishlistRecyclerView.addItemDecoration(new Space(2,20,true,0));

        wishlistRecyclerView.setAdapter(mwishlistAdapter);
//        items ends here
    }

    private void getwishlistPartsIds() {
        loading.setVisibility(View.VISIBLE);
        nowishItems.setVisibility(View.GONE);
        DatabaseReference wishlistdatabase = FirebaseDatabase.getInstance().getReference().child("wishlist").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        wishlistdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        //fetching wish list parts from the allParts database node
                        FetchWishListParts(child.getKey());
                    }
                }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WishList.this,"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void FetchWishListParts(final String key) {
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("allParts").child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            price = child.getValue().toString();
                        }

                        if(child.getKey().equals("buyersNumber")){
                            sellersNumber = child.getValue().toString();
                        }

                        if(child.getKey().equals("rating")){
                            product_rating = child.getValue().toString();
                        }

                        if(child.getKey().equals("views")){
                            views = child.getValue().toString();
                        }


                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    String partid = key;
                    boolean isNew = false;
                    CarParts obj = new CarParts(partid,imageurl,views,name,description,price, sellersNumber,product_rating, "","", isNew);
                    wishlistParts.add(obj);
                    wishlistRecyclerView.setAdapter(mwishlistAdapter);
                    mwishlistAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WishList.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList<CarParts> getwishlistParts(){
        return  wishlistParts;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
