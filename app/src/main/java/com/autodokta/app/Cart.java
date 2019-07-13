package com.autodokta.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.CartPartsAdapter;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.helpers.Space;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class Cart extends AppCompatActivity {

    ProgressBar loading;
    TextView noCart,totalPrizeText;
    RecyclerView cartRecyclerView;
    RecyclerView.LayoutManager mCartLayoutManager;
    RecyclerView.Adapter mCartAdapter;
    ArrayList resultParts = new ArrayList<CarParts>();
    FirebaseAuth firebaseAuth;
    String imageurl, name, description, price, sellersNumber,quantity;
    Button cacheout,calltoorder;
    ImageView goBack;

    int totalPrize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle("Cart");

        loading = (ProgressBar) findViewById(R.id.loading);
        noCart = (TextView) findViewById(R.id.emptycart);

        firebaseAuth = FirebaseAuth.getInstance();

        cartRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewCart);
        cacheout = (Button) findViewById(R.id.cacheout);
        calltoorder = (Button) findViewById(R.id.calltoorder);
        totalPrizeText = (TextView) findViewById(R.id.totalprize);
//        goBack = (ImageView) findViewById(R.id.goback);

        cartRecyclerView.setHasFixedSize(true);

        mCartLayoutManager = new LinearLayoutManager(Cart.this);
        cartRecyclerView.setLayoutManager(mCartLayoutManager);

        getCartIds();


        mCartAdapter = new CartPartsAdapter(getParts(),Cart.this);

//        cartRecyclerView.addItemDecoration(new Space(2,20,true,0));

        cartRecyclerView.setAdapter(mCartAdapter);

        cacheout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this, CompleteOrder.class);
                intent.putExtra("totalSale",totalPrize+"");
                startActivity(intent);
            }
        });

        calltoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+233266711801"));
                startActivity(intent);
            }
        });

//        goBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
    }


    boolean fetchtheQuantity(String key){
        getQuantity(key);
        return true;
    }

    public void getCartIds() {
        loading.setVisibility(View.VISIBLE);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        DatabaseReference cartdatabase = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid());
        cartdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){

                        if(fetchtheQuantity(child.getKey())){
                            FetchPartsFromParts(child.getKey());
                        }else{
                            Toast.makeText(Cart.this,"Couldn't fetch quantity",Toast.LENGTH_LONG).show();
                        }
//                        getQuantity(child.getKey());
                    }
                }else{
                    Toast.makeText(Cart.this,"Cannot get ID",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Cart.this,"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getQuantity(String key) {

        try{
            FirebaseUser user = firebaseAuth.getCurrentUser();

            DatabaseReference cartdatabase = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid()).child(key);
            cartdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("quantity")){
                                quantity = child.getValue().toString();
                            }
                        }
                    }else{
                        Toast.makeText(Cart.this,"Cannot get ID",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Cart.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });

        }catch (NullPointerException e){

        }


    }

    private void FetchPartsFromParts(final String key) {
        if(key.contains("brake")){
            FetchingValues(key,"Brakes");
        }

        if(key.contains("engine")){
            FetchingValues(key,"Engines");

        }

        if(key.contains("exterior")){
            FetchingValues(key,"Exteriorparts");

        }

        if(key.contains("light")){
            FetchingValues(key,"HeadLights");

        }

        if(key.contains("interior")){
            FetchingValues(key,"InteriorParts");

        }
        if(key.contains("offer")){
            FetchingValues(key,"Offers");
        }

        if(key.contains("tool")){
            FetchingValues(key,"Tools");
        }

         if(key.contains("wheel")){
             FetchingValues(key,"Wheels");
         }
    }

    public ArrayList<CarParts> getParts(){
        return  resultParts;
    }

    private void FetchingValues(final String key, String carPart) {
        try{

            DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("carparts").child(carPart).child(key);
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
                                try {
                                    if(quantity.equals(null)){
                                        quantity = "1";
                                    }
                                    totalPrize += Integer.valueOf(quantity) * Float.valueOf(price.replace("GHC ",""));

                                }catch (NullPointerException e){

                                }
                            }

                            if(child.getKey().equals("buyersNumber")){
                                sellersNumber = child.getValue().toString();
                            }


                            else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                            }
                        }

                        String partid = key;
                        boolean isNew = false;
                        CarParts obj = new CarParts(partid,imageurl,name,description,price, isNew, sellersNumber,quantity);
                        resultParts.add(obj);
                        cartRecyclerView.setAdapter(mCartAdapter);
                        mCartAdapter.notifyDataSetChanged();
                        loading.setVisibility(View.GONE);
                        noCart.setVisibility(View.GONE);
//                    Toast.makeText(Cart.this,String.valueOf(totalPrize),Toast.LENGTH_LONG).show();

//                    totalPrizeText.setText(totalPrize);

                    }
                    cacheout.setVisibility(View.VISIBLE);
                    calltoorder.setVisibility(View.VISIBLE);
                    totalPrizeText.setText("Total Prize: " +"GHC " +totalPrize+""+".00");



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Cart.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });

        }catch(NullPointerException e){
            Log.e("Firebase","Null Value");
        }

    }




}




