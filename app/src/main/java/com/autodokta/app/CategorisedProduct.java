package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.autodokta.app.Adapters.CategorisedProductAdapter;
import com.autodokta.app.Adapters.CategorisedServicesAdapter;
import com.autodokta.app.Models.CategorisedProductModel;
import com.autodokta.app.Models.CategorisedServicesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategorisedProduct extends AppCompatActivity {

    DatabaseReference databaseReference;
    List<CategorisedProductModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorised_product);

        getSupportActionBar().setTitle("Categorised Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent receivedIntent = getIntent();
        String productType = receivedIntent.getStringExtra("product_type");
        System.out.println("product type : " + productType);

        recyclerView = (RecyclerView)findViewById(R.id.rvSortedProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("allParts");




        databaseReference.orderByChild("product_type").equalTo(productType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    CategorisedProductModel model = dataSnapshot1.getValue(CategorisedProductModel.class);
                    modelList.add(model);
                }

                adapter = new CategorisedProductAdapter(CategorisedProduct.this,modelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("database error:" + databaseError.getMessage());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
