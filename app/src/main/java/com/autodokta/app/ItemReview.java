package com.autodokta.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemReview extends AppCompatActivity {

    private TextView star_text;
    private ImageView star1, star2, star3, star4, star5;
    String rating_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_review);
        getSupportActionBar().setTitle("Customer Feedback");

        rating_number = getIntent().getStringExtra("item_rating");

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
}
