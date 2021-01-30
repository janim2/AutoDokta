package com.autodokta.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autodokta.app.Adapters.EventsAdapter;
import com.autodokta.app.Models.EventsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AutoEventsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_event,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.upload_events:
                startActivity(new Intent(AutoEventsActivity.this, UploadEvent.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //events recycler initializations
    private RecyclerView                    events_recycler;

    private RecyclerView.Adapter            events_adapter;

    private RecyclerView.LayoutManager      events_layout;

    private ArrayList<EventsModel>          events_list;

    private Accessories                     events_accessor;

    private DatabaseReference               events_reference;

    private String                          description,  image_url,
                                            location,     prize,
                                            rate,         title;

    private TextView                        no_internet,  no_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_events);

        getSupportActionBar().setTitle("All Events");
        events_accessor    =   new Accessories(getApplicationContext());
        no_events          =   findViewById(R.id.no_events);
        no_internet        =   findViewById(R.id.no_internet);

        initializeRecyclerView();
        if(isNetworkAvailable()){
            FetchEventIDS();
        }
        else{
            no_events.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
        }
    }

    private void FetchEventIDS() {
        events_reference = FirebaseDatabase.getInstance().getReference("events");
        events_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        FetchEvents(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchEvents(final String event_key) {
        events_list.clear();
        events_reference = FirebaseDatabase.getInstance().getReference("events")
                .child(event_key);
        events_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }
                        if(child.getKey().equals("image_url")){
                            image_url = child.getValue().toString();
                        }
                        if(child.getKey().equals("location")){
                            location = child.getValue().toString();
                        }
                        if(child.getKey().equals("prize")){
                            prize = child.getValue().toString();
                        }
                        if(child.getKey().equals("rate")){
                            rate = child.getValue().toString();
                        }
                        if(child.getKey().equals("title")){
                            title = child.getValue().toString();
                        }
                    }
                    //adding values to model
                    EventsModel obj = new EventsModel(  event_key, image_url, title, location,
                                                        description, rate,prize);
                    events_list.add(obj);
                    try {
                        events_adapter.notifyDataSetChanged();
                        no_internet.setVisibility(View.GONE);
                        no_events.setVisibility(View.GONE);
//                        refresh.setRefreshing(false);
                    }catch (ClassCastException e){
                        e.printStackTrace();
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {
        events_list       = new ArrayList<>();
        events_recycler   = findViewById(R.id.events_recyclerview);
        events_recycler.setNestedScrollingEnabled(false);
        events_recycler.setHasFixedSize(false);
        events_layout     = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        events_recycler.setLayoutManager(events_layout);
        events_adapter    = new EventsAdapter(events_list,getApplicationContext());
        events_recycler.setAdapter(events_adapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
