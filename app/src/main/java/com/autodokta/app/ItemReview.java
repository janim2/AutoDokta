package com.autodokta.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.Adapters.ReviewAdapter;
import com.autodokta.app.Adapters.Reviews;
import com.autodokta.app.helpers.Space;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemReview extends AppCompatActivity {

    private TextView star_text, no_reviewsTextView, rating_count;
    private ImageView star1, star2, star3, star4, star5;
    String rating_number, item_id;

    //    strings for loading the parts from the database
    private ArrayList review_result = new ArrayList<Reviews>();
    private RecyclerView reviewRecyclerView;
    private RecyclerView.Adapter mReviewAdapter;
    private RecyclerView.LayoutManager mReviewLayoutManager;
    private String individual_rate, name, message, title;
    private ProgressBar loading;
    private int rate_count_int = 0;
//    strings ends here


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_review);
        getSupportActionBar().setTitle("Customer Feedback");

        item_id = getIntent().getStringExtra("item_id");
        rating_number = getIntent().getStringExtra("item_rating");
        no_reviewsTextView  = (TextView) findViewById(R.id.no_reviews);
        rating_count  = (TextView) findViewById(R.id.rating_count);

        //items to load from the database starts here
        loading  = (ProgressBar)findViewById(R.id.loading);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        reviewRecyclerView.setHasFixedSize(true);

//        mReviewLayoutManager = new GridLayoutManager(MainActivity.this,2, LinearLayoutManager.VERTICAL,false);
        mReviewLayoutManager = new LinearLayoutManager(ItemReview.this);
        reviewRecyclerView.setLayoutManager(mReviewLayoutManager);

        if(isNetworkAvailable()){
            getReviewIds();
        }else {
            Toast.makeText(ItemReview.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }


        mReviewAdapter = new ReviewAdapter(getReviews(),ItemReview.this);

        reviewRecyclerView.addItemDecoration(new Space(2,20,true,0));

        reviewRecyclerView.setAdapter(mReviewAdapter);
//        items ends here

        star_text = findViewById(R.id.rating_text);
        star1 = findViewById(R.id.star_one);
        star2 = findViewById(R.id.star_two);
        star3 = findViewById(R.id.star_three);
        star4 = findViewById(R.id.star_four);
        star5 = findViewById(R.id.star_five);

        star_text.setText(rating_number + " / 5");

        if(rating_number.equals("4")){
            star5.setVisibility(View.GONE);
        }

        else if(rating_number.equals("3")){
            star5.setVisibility(View.GONE);
            star4.setVisibility(View.GONE);
        }

        else if(rating_number.equals("2")){
            star5.setVisibility(View.GONE);
            star4.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
        }

        else if(rating_number.equals("1")){
            star5.setVisibility(View.GONE);
            star4.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
            star2.setVisibility(View.GONE);
        }
    }

    private void getReviewIds() {
        loading.setVisibility(View.VISIBLE);
        DatabaseReference partdatabase = FirebaseDatabase.getInstance().getReference("review").child(item_id);

        partdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().length() > 15){
                            rating_count.setText(String.valueOf(rate_count_int += 1));
                            getReviewsNow(child.getKey());
                        }
                    }
                }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemReview.this,"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getReviewsNow(String key) {
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference("review").child(item_id).child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("individual_rate")){
                            individual_rate = child.getValue().toString();
                        }

                        if(child.getKey().equals("message")){
                            message = child.getValue().toString();
                        }

                        if(child.getKey().equals("title")){
                            title = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            name = child.getValue().toString();
                        }

                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();
                        }
                    }

                    Reviews obj = new Reviews(individual_rate,message,title,name);
                    review_result.add(obj);
                    reviewRecyclerView.setAdapter(mReviewAdapter);
                    mReviewAdapter.notifyDataSetChanged();
                    no_reviewsTextView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemReview.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList<Reviews> getReviews(){
        return  review_result;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
