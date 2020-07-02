package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.autodokta.app.Adapters.Events;
import com.autodokta.app.Adapters.EventsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AutoEvents extends AppCompatActivity {

    private ArrayList<Events>events;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_events);
        getSupportActionBar().setTitle("Auto Events");

        recyclerView = (RecyclerView)findViewById(R.id.event_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AutoEvents.this));
        events = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("events");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Events events1 = dataSnapshot.getValue(Events.class);
                    events.add(events1);
                    eventsAdapter = new EventsAdapter(events);
                    recyclerView.setAdapter(eventsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Database Error: " +  databaseError.getMessage());
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedOption = item.getItemId();
        if (selectedOption == R.id.events){
            startActivity(new Intent(AutoEvents.this,UploadEvent.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
