package com.autodokta.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.CustomRequestsAdapter;
import com.autodokta.app.Adapters.Notify;
import com.autodokta.app.Adapters.NotifyAdapter;
import com.autodokta.app.Models.CustomRequestsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ViewCustomRequestActivity extends AppCompatActivity {

    //variables to use for getting values from the datebase
    private ArrayList               viewcustomRequestsArray = new ArrayList<Notify>();

    private RecyclerView            viewcustomRequests_RecyclerView;

    private RecyclerView.Adapter    viewcustomRequests_Adapter;

    private String                  request_id, budget, description, item_name, requestername,
                                    requester_id, userid;

    private TextView                loadingTextView;
    //variables ends here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_custom_request);
        getSupportActionBar().setTitle("All Custom Requests");
        try {
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }catch (NullPointerException e){

        }
        loadingTextView = findViewById(R.id.loading);
        viewcustomRequests_RecyclerView = findViewById(R.id.custom_request_recyclerView);
        //reviews adapter settings starts here
        if(isNetworkAvailable()){
            getviewcustomRequests_ID();
        }else{
            Toast.makeText(ViewCustomRequestActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        viewcustomRequests_RecyclerView.setHasFixedSize(true);

        viewcustomRequests_Adapter = new CustomRequestsAdapter(getviewcustomRequestsFromDatabase(),ViewCustomRequestActivity.this);
        viewcustomRequests_RecyclerView.setAdapter(viewcustomRequests_Adapter);
//        reviews adapter ends here
    }

    private void getviewcustomRequests_ID() {
        try{
            DatabaseReference viewcustomRequests = FirebaseDatabase.getInstance().getReference("requests");

            //limiting number of items to be fetched
            Query query = viewcustomRequests.orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_viewcustomRequests(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewCustomRequestActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }
    }

    private void Fetch_viewcustomRequests(String key) {
        DatabaseReference getviewcustomRequests = FirebaseDatabase.getInstance().getReference("requests").child(key);
        getviewcustomRequests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("budget")){
                            budget = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }

                        if(child.getKey().equals("item_name")){
                            item_name = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            requestername = child.getValue().toString();
                        }
                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                        CustomRequestsModel obj = new CustomRequestsModel(key,budget,description,item_name, requestername, requester_id);
                        viewcustomRequestsArray.add(obj);
                        viewcustomRequests_RecyclerView.setAdapter(viewcustomRequests_Adapter);
                        viewcustomRequests_Adapter.notifyDataSetChanged();

                    loadingTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewCustomRequestActivity.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList<CustomRequestsModel> getviewcustomRequestsFromDatabase(){
        return  viewcustomRequestsArray;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}