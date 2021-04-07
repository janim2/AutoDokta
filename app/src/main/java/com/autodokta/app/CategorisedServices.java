package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.autodokta.app.Adapters.CategorisedServicesAdapter;
import com.autodokta.app.Models.CategorisedServicesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategorisedServices extends AppCompatActivity {

    DatabaseReference databaseReference;
    ProgressBar progressBar;
    List<CategorisedServicesModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorised_services);

        getSupportActionBar().setTitle("Categorised Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent receivedIntent = getIntent();
        String serviceType = receivedIntent.getStringExtra("service_type");
        System.out.println("selected service: " + serviceType);



       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       if (firebaseUser != null){
           recyclerView = (RecyclerView)findViewById(R.id.services_recyclerView);
           recyclerView.setHasFixedSize(true);
           recyclerView.setLayoutManager(new LinearLayoutManager(this));

           databaseReference = FirebaseDatabase.getInstance().getReference().child("services").child(firebaseUser.getUid());




           databaseReference.orderByChild("service_type").equalTo(serviceType).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                       CategorisedServicesModel model = dataSnapshot1.getValue(CategorisedServicesModel.class);
                       modelList.add(model);
                   }

                   adapter = new CategorisedServicesAdapter(CategorisedServices.this,modelList);
                   recyclerView.setAdapter(adapter);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   System.out.println("database error:" + databaseError.getMessage());
               }
           });
       }else {
           System.out.println("User is null");
       }




    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
