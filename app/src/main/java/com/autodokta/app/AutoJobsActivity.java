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
import android.widget.Toast;

import com.autodokta.app.Adapters.JobsAdapter;
import com.autodokta.app.Models.EventsModel;
import com.autodokta.app.Models.JobsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AutoJobsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jobs_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.upload_job:
                startActivity(new Intent(AutoJobsActivity.this, UploadJobActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //jobs recycler initializations
    private RecyclerView                    jobs_recycler;

    private RecyclerView.Adapter            jobs_adapter;

    private RecyclerView.LayoutManager      jobs_layout;

    private ArrayList<JobsModel>            jobs_list;

    private Accessories                     jobs_accessor;

    private TextView                        no_jobs,    no_internet;

    private DatabaseReference               jobs_reference;

    private String                          experience,     description,
                                            job_type,       regions,
                                            responsibility, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_jobs);

        getSupportActionBar().setTitle("All Jobs");
        jobs_accessor       =   new Accessories(getApplicationContext());
        no_jobs             =   findViewById(R.id.no_jobs);
        no_internet         =   findViewById(R.id.no_internet);

        initializeRecyclerView();
        if(isNetworkAvailable()){
            FetchJobsIDS();
        }
        else{
            no_jobs.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
        }

    }

    private void initializeRecyclerView() {
        jobs_list       = new ArrayList<>();
        jobs_recycler   = findViewById(R.id.jobs_recyclerview);
        jobs_recycler.setNestedScrollingEnabled(false);
        jobs_recycler.setHasFixedSize(false);
        jobs_layout     = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        jobs_recycler.setLayoutManager(jobs_layout);
        jobs_adapter    = new JobsAdapter(jobs_list,getApplicationContext());
        jobs_recycler.setAdapter(jobs_adapter);
    }

    private void FetchJobsIDS() {
        jobs_reference = FirebaseDatabase.getInstance().getReference("jobs");
        jobs_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        FetchJobs(child.getKey());
//                        Toast.makeText(AutoJobsActivity.this, child.getKey(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchJobs(final String job_key) {
        jobs_list.clear();
        jobs_reference = FirebaseDatabase.getInstance().getReference("jobs")
                .child(job_key);
        jobs_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }
                        if(child.getKey().equals("experience")){
                            experience = child.getValue().toString();
                        }
                        if(child.getKey().equals("job_type")){
                            job_type = child.getValue().toString();
                        }
                        if(child.getKey().equals("regions")){
                            regions = child.getValue().toString();
                        }
                        if(child.getKey().equals("responsibility")){
                            responsibility = child.getValue().toString();
                        }
                        if(child.getKey().equals("title")){
                            title = child.getValue().toString();
                        }
                    }
                    //adding values to model
                    JobsModel obj = new JobsModel(job_key,title,description,experience,job_type,regions,responsibility);
                    jobs_list.add(obj);
                    try {
                        jobs_adapter.notifyDataSetChanged();
                        no_internet.setVisibility(View.GONE);
                        no_jobs.setVisibility(View.GONE);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
