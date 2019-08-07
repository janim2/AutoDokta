package com.autodokta.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.Adapters.Related_items_PartsAdapter;
import com.autodokta.app.helpers.Space;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import static android.widget.Toast.LENGTH_LONG;


public class ItemDetailsActivity extends AppCompatActivity {

    private String spartid, simage, sname, sprice, sdescription, sSellersNumber, which_wishlist = "0";
    private EditText no_such_product;
    private Button buyNow, submitted;
    private ImageView image, product_image, add_to_wishList;
    private TextView name, description, price;
    private ProgressBar loading;

    //  private   initialization of image loader
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String item = "";
    private ArrayList relatedParts = new ArrayList<CarParts>();
    private RecyclerView related_items_RecyclerView;
    private RecyclerView.Adapter related_items_mPostAdapter;
    private RecyclerView.LayoutManager related_items_mPostLayoutManager;
    private String related_item_imageurl, related_item_name, related_item_description, related_item_price, related_item_sellersNumber;

   //initializing the dialogue class
    private Dialog item_details_dialogue;

    private ImageButton close_button;

    private float downXpos = 0;
    private float downYpos = 0;
    private boolean touchcaptured = false;

    private Accessories accessories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accessories = new Accessories(ItemDetailsActivity.this);
        Intent intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();

        image = findViewById(R.id.image1);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        buyNow = findViewById(R.id.buyNow);
        no_such_product = findViewById(R.id.no_such_product);
        submitted = findViewById(R.id.submitted);
        loading = findViewById(R.id.loading);
        add_to_wishList = findViewById(R.id.ic_wishlist);

        item_details_dialogue = new Dialog(ItemDetailsActivity.this);

        spartid = intent.getStringExtra("partid");
        simage = intent.getStringExtra("theimage");
        sname = intent.getStringExtra("thename");
        sprice = intent.getStringExtra("theprice");
        sdescription = intent.getStringExtra("thedescription");
        sSellersNumber = intent.getStringExtra("thesellersNumber");

        //prep work before image is loaded is to load it into the cache
        DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                defaultDisplayImageOptions(theImageOptions).build();
        ImageLoader.getInstance().init(config);
//
        findoutifthere();
        String imagelink = simage;
        imageLoader.displayImage(imagelink,image);

        name.setText(sname);
        description.setText(sdescription);
        price.setText(sprice);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(ItemDetailsActivity.this, simage);
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = firebaseAuth.getCurrentUser();
                if(user!=null){
                       if(true){
                           ViewCartDialogue cartDialogue = new ViewCartDialogue();
                           cartDialogue.showDialog(ItemDetailsActivity.this,"Success! "+ sname, user.getUid(),spartid);
                       }
                       else{
                           buyNow.setText("Already Added To Cart");
                           buyNow.setClickable(false);
                       }
               }else{
                   ViewLoginDialogue dialogue = new ViewLoginDialogue();
                   dialogue.showDialog(ItemDetailsActivity.this,"Action Needed To Continue");
               }
            }
        });

        //submit a product that isnt there
        submitted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!no_such_product.getText().toString().equals("")){
                    send_notification_to_manager();
                }else{
                    no_such_product.setError("Required");
                }
            }
        });

//        adding to wishlist
            add_to_wishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        if(which_wishlist.equals("0")){
                            which_wishlist = "1";
                            add_to_wishList.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                            adding_to_wishList();
                        }
                        else if(which_wishlist.equals("1")){
                            which_wishlist = "0";
                            add_to_wishList.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_18dp));
                            remove_from_wishlist();
                        }
                    }else{
                        Toast.makeText(ItemDetailsActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                    }


                }
            });
//        related items pickup logic starts here

        related_items_RecyclerView = findViewById(R.id.recyclerView_relatedProducts);
        related_items_RecyclerView.setHasFixedSize(true);

//        related_items_mPostLayoutManager = new LinearLayoutManager(ItemDetailsActivity.this);
//        related_items_RecyclerView.setLayoutManager(related_items_mPostLayoutManager);

        getRelatedItems_ID();
        related_items_mPostAdapter = new Related_items_PartsAdapter(getrelatedParts(),ItemDetailsActivity.this);
        related_items_RecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downXpos = motionEvent.getX();
                        downYpos = motionEvent.getY();
                        touchcaptured = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        recyclerView.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xdisplacement = Math.abs(motionEvent.getX() - downXpos);
                        float ydisplacement = Math.abs(motionEvent.getY() - downYpos);
                        if( !touchcaptured && xdisplacement > ydisplacement && xdisplacement > 10) {
                            recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                            touchcaptured = true;
                        }
                        break;
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
        related_items_RecyclerView.addItemDecoration(new Space(2,20,true,0));
        related_items_RecyclerView.setAdapter(related_items_mPostAdapter);

    }

    private void remove_from_wishlist() {
        DatabaseReference the_reference = FirebaseDatabase.getInstance().getReference("wishlist").child(user.getUid()).child(spartid);
        the_reference.removeValue();
        Toast.makeText(ItemDetailsActivity.this,"Removed from Wishlist",Toast.LENGTH_LONG).show();
        accessories.put(spartid+"wished_or_not","no");
    }

    private void adding_to_wishList() {
        DatabaseReference the_reference = FirebaseDatabase.getInstance().getReference("wishlist").child(user.getUid());
        the_reference.child(spartid).child("number").setValue("1");
        Toast.makeText(ItemDetailsActivity.this,"Added to Wishlist",Toast.LENGTH_LONG).show();
        accessories.put(spartid+"wished_or_not","yes");
    }

    @Override
    protected void onStart() {
        super.onStart();
        String is_whiched = accessories.getString(spartid+"wished_or_not");
        if(is_whiched!=null){
            if(is_whiched.equals("yes")){
                add_to_wishList.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            }else{
                add_to_wishList.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_18dp));
            }
        }else {
            add_to_wishList.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_18dp));
        }

    }

    private void send_notification_to_manager() {
        new Sending_mail("https://knust-martial-arts.000webhostapp.com/AutoDoktaService.php","commend","Customer",
                no_such_product.getText().toString()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String sharebody = simage;
                share.putExtra(Intent.EXTRA_SUBJECT,sname);
                share.putExtra(Intent.EXTRA_TEXT,sharebody);
                startActivity(Intent.createChooser(share,"Share via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRelatedItems_ID() {
        if(spartid.contains("offer")){
            getItemIDs("Offers");
        }
        if(spartid.contains("brake")){
            getItemIDs("Brakes");
        }
        if(spartid.contains("engine")){
            getItemIDs("Engines");
        }
        if(spartid.contains("exterior")){
            getItemIDs("ExteriorParts");
        }
        if(spartid.contains("light")){
            getItemIDs("HeadLights");
        }
        if(spartid.contains("interior")){
            getItemIDs("InteriorParts");
        }
        if(spartid.contains("tool")){
            getItemIDs("Tools");
        }
        if(spartid.contains("wheel")){
            getItemIDs("Wheels");
        }
    }

    private void getItemIDs(final String which_item) {
        try{
            DatabaseReference partdatabase = FirebaseDatabase.getInstance().getReference().child("carparts").child(which_item);

            //limiting number of items to be fetched
            Query query = partdatabase.limitToLast(3);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                            //randomly selecting items to display
//                            int ads = (int) dataSnapshot.getChildrenCount();
//                            int rand = new Random().nextInt(ads);
                        for(DataSnapshot child : dataSnapshot.getChildren()){
//                            for(DataSnapshot datas: child.getChildren()){
//                                for(int i= 0; i < rand;i++){
                                    FetchParts(child.getKey(), which_item);
//                                }
                            }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();

                    }
                }
//                add wishlist; to navigation menu
//                when click of heart button add to wishlist;
//                make heart icon bigger

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ItemDetailsActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (NullPointerException e){

        }

    }

    private void FetchParts(final String key, String which_of_the_items) {
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("carparts").child(which_of_the_items).child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            related_item_imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            related_item_name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            related_item_description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            related_item_price = child.getValue().toString();
                        }

                        if(child.getKey().equals("buyersNumber")){
                            related_item_sellersNumber = child.getValue().toString();
                        }


                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    String partid = key;
                    boolean isNew = false;
                    CarParts obj = new CarParts(partid,related_item_imageurl,related_item_name,related_item_description,
                            related_item_price, isNew, related_item_sellersNumber);
                    relatedParts.add(obj);
                    related_items_RecyclerView.setAdapter(related_items_mPostAdapter);
                    related_items_mPostAdapter.notifyDataSetChanged();
//                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemDetailsActivity.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + sSellersNumber));
        startActivity(intent);
    }

    public boolean findoutifthere() {
        user = firebaseAuth.getCurrentUser();

        if(user!=null){

            reference = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid()).child(spartid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals(spartid)){
                                item = child.getValue().toString();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return true;
        }
        return true;
    }

    public ArrayList<CarParts> getrelatedParts(){
        return  relatedParts;
    }


    private void showImagePopup(FragmentActivity activity,String imageString) {
        item_details_dialogue.setContentView(R.layout.custom_image_dialogue);
            close_button = (ImageButton)item_details_dialogue.findViewById(R.id.btnClose);
          product_image = (ImageView)item_details_dialogue.findViewById(R.id.popup_image);
//        id_and_gender = (TextView)view_student_dialogue.findViewById(R.id.id_and_gender);
//        dobthetext = (TextView)view_student_dialogue.findViewById(R.id.dobthetext);
//        dob_date = (TextView)view_student_dialogue.findViewById(R.id.dob_date);
//        addedontext = (TextView)view_student_dialogue.findViewById(R.id.addedontheext);
//        addedondate = (TextView)view_student_dialogue.findViewById(R.id.addedondate);

//        st_name.setText(name);
//        id_and_gender.setText(id_gender);
//        dob_date.setText(dob);
//        addedondate.setText(addedon);

        //prep work before image is loaded is to load it into the cache
        DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                defaultDisplayImageOptions(theImageOptions).build();
        ImageLoader.getInstance().init(config);

        imageLoader.displayImage(imageString,product_image);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_details_dialogue.dismiss();
            }
        });
        item_details_dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.whiteTextColor)));
        item_details_dialogue.show();
    }

    class Sending_mail extends AsyncTask<Void, Void, String> {

        String url_location, status,name, message;

        public Sending_mail(String url_location,String status, String name, String message) {
            this.url_location = url_location;
            this.status = status;
            this.name = name;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                URL url = new URL(url_location);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&" +
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String fetch;
                while ((fetch = bufferedReader.readLine()) != null) {
                    stringBuffer.append(fetch);
                }
                String string = stringBuffer.toString();
                inputStream.close();
                return string;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return "please check internet connection";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.setVisibility(View.GONE);
            if (s.equals("Product Commendation Sent")){
                Toast.makeText(ItemDetailsActivity.this, s, LENGTH_LONG).show();
            }
            Toast.makeText(ItemDetailsActivity.this, s, LENGTH_LONG).show();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
