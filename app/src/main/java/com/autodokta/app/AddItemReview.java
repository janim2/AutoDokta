package com.autodokta.app;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.GONE;

public class AddItemReview extends AppCompatActivity {

    private ImageView star_1,star_2,star_3,star_4,star_5;
    private TextView rating_text, done_rating, howimproveTextView, reviewTitleTextView, nameTextView;
    private Button submit_button;
    private FirebaseUser firebaseUser;
    private String thepart_id,sfirstname,slastname,suggested_improve, review_title, fullname, value_of_rate = "0";
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_review);

        star_1 = findViewById(R.id.star_1);
        star_2 = findViewById(R.id.star_2);
        star_3 = findViewById(R.id.star_3);
        star_4 = findViewById(R.id.star_4);
        star_5 = findViewById(R.id.star_5);

        rating_text =  findViewById(R.id.rating_values);
        done_rating =  findViewById(R.id.done_rating);
        howimproveTextView =  findViewById(R.id.how_improveTextView);
        reviewTitleTextView =  findViewById(R.id.review_titleTextView);
        nameTextView =  findViewById(R.id.name_textView);
        submit_button =  findViewById(R.id.submit_button);
        loading =  findViewById(R.id.loading);
        thepart_id = getIntent().getStringExtra("toReview_partId");

        //here i try to manupulate the stars and how they behave
        star_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_1.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_2.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                star_3.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                star_4.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                star_5.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                rating_text.setText("I hate");
                value_of_rate = "1";
            }
        });

        star_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_1.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_2.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_3.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                star_4.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                star_5.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                rating_text.setText("I don't like it");
                value_of_rate = "2";
            }
        });

        star_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_1.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_2.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_3.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_4.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                star_5.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                rating_text.setText("Its OK");
                value_of_rate = "3";
            }
        });

        star_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_1.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_2.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_3.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_4.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_5.setImageDrawable(getResources().getDrawable(R.drawable.bare_star));
                rating_text.setText("I like it");
                value_of_rate = "4";
            }
        });

        star_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_1.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_2.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_3.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_4.setImageDrawable(getResources().getDrawable(R.drawable.star));
                star_5.setImageDrawable(getResources().getDrawable(R.drawable.star));
                rating_text.setText("It's perfect");
                value_of_rate = "5";
            }
        });
//        star login ends here

        //submit button logic starts here
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggested_improve = howimproveTextView.getText().toString().trim();
                fullname = nameTextView.getText().toString().trim();
                review_title = reviewTitleTextView.getText().toString().trim();

                if(!suggested_improve.equals("")){
                    if(!fullname.equals("")){
                        if(!review_title.equals("")){
                            if(isNetworkAvailable()){
                                loading.setVisibility(View.VISIBLE);
                                if(addToReview()){
                                    edit_product_rating();
                                    loading.setVisibility(View.GONE);
                                    done_rating.setVisibility(View.VISIBLE);
                                }
                            }else{
                                Toast.makeText(AddItemReview.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            reviewTitleTextView.setError("Required");
                        }
                    }else{
                        nameTextView.setError("Required");
                    }
                }else{
                    howimproveTextView.setError("Required");
                }


            }
        });
//        submit button logic ends here

        if(isNetworkAvailable()){
            getUserInfo();
        }else{
            Toast.makeText(AddItemReview.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    private void edit_product_rating() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference edit_rate = FirebaseDatabase.getInstance().getReference("allParts").child(thepart_id);
        edit_rate.child("rating").setValue(value_of_rate);
    }

    private boolean addToReview() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference addtoReview = FirebaseDatabase.getInstance().getReference("review");
        String reviewid = addtoReview.push().getKey();
        addtoReview.child(thepart_id).child(firebaseUser.getUid()).child("individual_rate").setValue(value_of_rate);
        addtoReview.child(thepart_id).child(firebaseUser.getUid()).child("title").setValue(review_title);
        addtoReview.child(thepart_id).child(firebaseUser.getUid()).child("message").setValue(suggested_improve);
        addtoReview.child(thepart_id).child(firebaseUser.getUid()).child("name").setValue(fullname);
        return true;
    }

    private void getUserInfo() {
        try {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference getUserInformation = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            getUserInformation.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("firstname")){
                                sfirstname = child.getValue().toString();
                            }

                            if(child.getKey().equals("lastname")){
                                slastname = child.getValue().toString();
                            }
                        }
                        if(sfirstname!=null){
                            nameTextView.setText(sfirstname + " " + slastname);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (NullPointerException e){

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
