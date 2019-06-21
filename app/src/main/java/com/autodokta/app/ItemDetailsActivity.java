package com.autodokta.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ItemDetailsActivity extends AppCompatActivity {

    String spartid, simage, sname, sprice, sdescription;

    ImageView image;
    TextView name, description, price;

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();


        image = findViewById(R.id.image1);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);


        spartid = intent.getStringExtra("partid");
        simage = intent.getStringExtra("theimage");
        sname = intent.getStringExtra("thename");
        sprice = intent.getStringExtra("theprice");
        sdescription = intent.getStringExtra("thedescription");

        //prep work before image is loaded is to load it into the cache
        DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                defaultDisplayImageOptions(theImageOptions).build();
        ImageLoader.getInstance().init(config);
//
        String imagelink = simage;
        imageLoader.displayImage(imagelink,image);

        name.setText(sname);
        description.setText(sdescription);
        price.setText(sprice);

    }

    public void onClick(View v) {
        Intent i = new Intent(this, AboutUsActivity.class);
        startActivity(i);
    }

}
