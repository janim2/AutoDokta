package com.autodokta.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetails extends AppCompatActivity {

    TextView title,description;
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().setTitle("Details");

//        initialize views
        title = (TextView)findViewById(R.id.detailsTitle);
        description = (TextView)findViewById(R.id.detailsDesc);
        imageView = (ImageView)findViewById(R.id.detailsImage);

//        get data from intent
        byte[] bytes = getIntent().getByteArrayExtra("image");
        String mTitle = getIntent().getStringExtra("title");
        String mDescription = getIntent().getStringExtra("description");
        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

//        pass data to  views
        title.setText(mTitle);
        description.setText(mDescription);
        imageView.setImageBitmap(bitmap);


    }
}
