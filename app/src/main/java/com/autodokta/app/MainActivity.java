package com.autodokta.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewPager;
import androidx.core.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
//import com.algolia.search.saas.Client;
import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.SearchItems.ResultsListView;
import com.autodokta.app.Services.NotificationService;
import com.autodokta.app.helpers.Space;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();



    public static int notificationCountCart = 0;
    FirebaseAuth mAuth;
    public Menu menu;
    private MenuItem menuItem, profilemenuitem, register,garageMenuItem;
    private FrameLayout layout;
    private TextView cartnumberTextView, guest_or_user;
    public NavigationView navigationView;
    private int cart_number;

    // Constants
    private static final int LOAD_MORE_THRESHOLD = 5;
    private static final int HITS_PER_PAGE = 20;


    //    strings for loading the parts from the database
//    private ArrayList resultParts = new ArrayList<CarParts>();
//    private ArrayList resultParts = new ArrayList<CarParts>();
    ArrayList<CarParts> resultParts = new ArrayList<CarParts>();


    private RecyclerView PostRecyclerView;
    private RecyclerView.Adapter mPostAdapter;
    private RecyclerView.LayoutManager mPostLayoutManager;
    private String imageurl, name, views, description, price, sellersNumber, product_rating;
    private ProgressBar loading;
//    strings ends here

    //sliders variables starts here
    private static ViewPager viewPager;
    private static int currentPage = 0;
    //    private static final Integer[] pics = {R.drawable.coding,R.drawable.learning,R.drawable.python};
    private static final Integer[] pics = {R.drawable.slider_image1,R.drawable.slider_image2,
            R.drawable.slider_image3};
    private ArrayList<Integer> picsArr = new ArrayList<Integer>();
//    sliders variables ends here

    private ImageButton         custom_request_button;

    DatabaseReference reference;

    CardView equipmentCardView, motorsCardView,carsCardView,busesCardView,electronicsCardView,replacement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        cart_number = 0;
        mAuth = FirebaseAuth.getInstance();
        layout = (FrameLayout)findViewById(R.id.cartframe);
        cartnumberTextView = (TextView) findViewById(R.id.cartnumber);
//        guest_or_user = (TextView) findViewById(R.id.guest_or_user);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA
                        , Manifest.permission.INTERNET
                        , Manifest.permission.CALL_PHONE
                }, 555);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //getting the navigation view
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initializing the image slider
//        initialize();

//        getting the menu from the navigation item;
        menu = navigationView.getMenu();
//        getting the menuitem that i want to change
        menuItem = menu.findItem(R.id.logout);
        register = menu.findItem(R.id.register);
        profilemenuitem = menu.findItem(R.id.profile);
        garageMenuItem = menu.findItem(R.id.garage);
//        wishlistMenuItem = menu.findItem(R.id.wish_list);

        custom_request_button = findViewById(R.id.custom_request);
        custom_request_button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,CustomRequestActivity.class));
        });

        loading  = (ProgressBar)findViewById(R.id.loading);

        equipmentCardView = (CardView)findViewById(R.id.equipment);
        equipmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productTypeIntent = new Intent(MainActivity.this, CategorisedProduct.class);
                productTypeIntent.putExtra("product_type","Tools and equipment");
                try {
                    startActivity(productTypeIntent);
                }catch (ActivityNotFoundException ex){
                    Log.i(TAG, "Next Activity: " + ex.getMessage());
                }
            }
        });

        motorsCardView = (CardView)findViewById(R.id.motors);
        motorsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productTypeIntent = new Intent(MainActivity.this, CategorisedProduct.class);
                productTypeIntent.putExtra("product_type","Motorcycle and Powersports");
                try {
                    startActivity(productTypeIntent);
                }catch (ActivityNotFoundException ex){
                    Log.i(TAG, "Next Activity: " + ex.getMessage());
                }
            }
        });

        carsCardView = (CardView)findViewById(R.id.cars);
        carsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productTypeIntent = new Intent(MainActivity.this, CategorisedProduct.class);
                productTypeIntent.putExtra("product_type","Cars");
                try {
                    startActivity(productTypeIntent);
                }catch (ActivityNotFoundException ex){
                    Log.i(TAG, "Next Activity: " + ex.getMessage());
                }
            }
        });

        busesCardView = (CardView)findViewById(R.id.buses);
        busesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productTypeIntent = new Intent(MainActivity.this, CategorisedProduct.class);
                productTypeIntent.putExtra("product_type","Buses and microbuses");
                try {
                    startActivity(productTypeIntent);
                }catch (ActivityNotFoundException ex){
                    Log.i(TAG, "Next Activity: " + ex.getMessage());
                }
            }
        });

        electronicsCardView = (CardView)findViewById(R.id.electronics);
        electronicsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productTypeIntent = new Intent(MainActivity.this, CategorisedProduct.class);
                productTypeIntent.putExtra("product_type","Electronics and Accessories");
                try {
                    startActivity(productTypeIntent);
                }catch (ActivityNotFoundException ex){
                    Log.i(TAG, "Next Activity: " + ex.getMessage());
                }
            }
        });

        replacement = (CardView)findViewById(R.id.car_replacement);
        replacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productTypeIntent = new Intent(MainActivity.this, CategorisedProduct.class);
                productTypeIntent.putExtra("product_type","Replacement Part");
                try {
                    startActivity(productTypeIntent);
                }catch (ActivityNotFoundException ex){
                    Log.i(TAG, "Next Activity: " + ex.getMessage());
                }
            }
        });




        layout.setOnClickListener(v -> {
            if(mAuth.getCurrentUser()!=null){
                startActivity(new Intent(MainActivity.this,Cart.class));
            }else{
                Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_LONG).show();
            }
        });

//        updateUIDefault();

        //items to load from the database starts here
        loading  = (ProgressBar)findViewById(R.id.loading);

        PostRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        PostRecyclerView.setHasFixedSize(true);

        mPostLayoutManager = new GridLayoutManager(MainActivity.this,2, LinearLayoutManager.VERTICAL,false);
//        mPostLayoutManager = new LinearLayoutManager(getActivity());
        PostRecyclerView.setLayoutManager(mPostLayoutManager);

        getPartsIds();

        mPostAdapter = new PartsAdapter(getParts(),MainActivity.this);

        PostRecyclerView.addItemDecoration(new Space(2,20,true,0));

        PostRecyclerView.setAdapter(mPostAdapter);

    }






//    private void updateUIDefault(){
//        PostRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewProducts);
//        PostRecyclerView.setHasFixedSize(true);
//
//        mPostLayoutManager = new GridLayoutManager(MainActivity.this,2, LinearLayoutManager.VERTICAL,false);
////        mPostLayoutManager = new LinearLayoutManager(getActivity());
//        PostRecyclerView.setLayoutManager(mPostLayoutManager);
//
////        getPartsIds(productCategory);
//
//        reference = FirebaseDatabase.getInstance().getReference().child("allParts");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    CarParts carParts = snapshot.getValue(CarParts.class);
//                    resultParts.add(carParts);
//                }
//
//                //        mPostAdapter = new PartsAdapter(getParts(),MainActivity.this);
//                mPostAdapter = new PartsAdapter(resultParts,MainActivity.this);
//
//                PostRecyclerView.addItemDecoration(new Space(2,20,true,0));
//
//                PostRecyclerView.setAdapter(mPostAdapter);
//                loading.setVisibility(View.GONE);
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("database error:" + databaseError.getMessage());
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem wish_list      = menu.findItem(R.id.wish_list);
        MenuItem chat_system    = menu.findItem(R.id.chat);
        MenuItem upload_        = menu.findItem(R.id.upload);
        MenuItem custom_request = menu.findItem(R.id.custom_requests);

        if(mAuth.getCurrentUser() != null){
            wish_list.setOnMenuItemClickListener(item -> {
                startActivity(new Intent(MainActivity.this,WishList.class));
                return false;
            });

            chat_system.setOnMenuItemClickListener(item -> {
                startActivity(new Intent(MainActivity.this,Chat.class));
                return false;
            });

            upload_.setOnMenuItemClickListener(item -> {
                startActivity(new Intent(MainActivity.this,UploadActivity.class));
                return false;
            });

            custom_request.setOnMenuItemClickListener(item -> {
                startActivity(new Intent(MainActivity.this,ViewCustomRequestActivity.class));
                return false;
            });
        }else{
            wish_list.setVisible(false);
            chat_system.setVisible(false);
            upload_.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this,Search_Activity.class));
                break;

            case R.id.action_notifications:
                if(mAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this,Notifications.class));
                }else{
                    Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean fabricInitialized = Fabric.isInitialized();
        if (fabricInitialized) {
            Crashlytics.log("Error Message");
        }

//        checking to see if user is logged In
        if(mAuth.getCurrentUser()!=null){
            Intent i = new Intent(MainActivity.this, NotificationService.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startService(i);

            getNumber_in_Cart();
//            guest_or_user.setText("Welcome user");
            register.setVisible(false);

        }else{
            layout.setVisibility(View.GONE);
            profilemenuitem.setVisible(false);
            garageMenuItem.setVisible(false);

            menuItem.setTitle("Login");
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.logout:
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            return true;
                    }

                    return false;
                }
            });
        }
    }

    private void getNumber_in_Cart(){
        if(mAuth.getCurrentUser()!=null) {
            DatabaseReference numberofpersons = FirebaseDatabase.getInstance().getReference("cart");
            numberofpersons.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                getCount(child.getKey());
                            }

                            cartnumberTextView.setText("0");

//                        Toast.makeText(MainActivity.this,child.getKey(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getCount(String key) {
        try {
            DatabaseReference getcartCount = FirebaseDatabase.getInstance().getReference("cart").child(key);
            getcartCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey()!=null){
                                cart_number += 1;
//                                Toast.makeText(MainActivity.this,child+"",Toast.LENGTH_LONG).show();
//                                Toast.makeText(MainActivity.this,child.getKey(),Toast.LENGTH_LONG).show();
//                                cartnumberTextView.setText(child.getChildrenCount()+"");
                            }
                        }
                        cartnumberTextView.setText(String.valueOf(cart_number));

                    }else{
                        Toast.makeText(MainActivity.this,"Cannot get ID",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Log.e("Firebase","Cannot count");
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        int id = menuItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_item5) {
            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            return true;
        }

        if (id == R.id.auto_event) {
            startActivity(new Intent(MainActivity.this, AutoEventsActivity.class));
            return true;
        }

        if (id == R.id.auto_jobs) {
            startActivity(new Intent(MainActivity.this, AutoJobsActivity.class));
            return true;
        }

        if (id == R.id.register) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            return true;
        }

        if (id == R.id.logout) {
            AlertDialog.Builder logout = new AlertDialog.Builder(MainActivity.this);
            logout.setTitle("Logging Out?");
            logout.setMessage("Are you sure you want to logout");
            logout.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        FirebaseAuth.getInstance().signOut();
                    }catch(NullPointerException e){

                    }
                    menuItem.setTitle("Login");
                    Toast.makeText(MainActivity.this,"Logout Successfull",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            logout.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            logout.show();
            return true;
        }


        if (id == R.id.contact_us){
            startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
            return true;
        }

        if (id == R.id.terms_conditions){
            startActivity(new Intent(MainActivity.this,TermsActivity.class));
            return true;
        }

        if (id == R.id.garage){
            startActivity(new Intent(MainActivity.this,Garage.class));
            return true;
        }

        if (id == R.id.services){
            startActivity(new Intent(MainActivity.this,ServicesActivity.class));
            return true;
        }

        if (id == R.id.profile){
            startActivity(new Intent(MainActivity.this,User_Profile.class));
        }

        if (id == R.id.drone){
            startActivity(new Intent(MainActivity.this, Drone.class));
        }




        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 555:
                if (grantResults.length <= 0) {
                    Toast.makeText(this, "Please accept these requests", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getPartsIds() {
//        DatabaseReference partdatabase = FirebaseDatabase.getInstance().getReference().child("carparts").child("Offers");
        DatabaseReference partdatabase = FirebaseDatabase.getInstance().getReference().child("allParts");

        partdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        FetchParts(child.getKey());
                    }
                }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void FetchParts(final String key) {
        resultParts.clear();
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("allParts").child(key);
        postData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image_url")){
                            imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("title")){
                            name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            price = child.getValue().toString();
                        }

                        if(child.getKey().equals("seller_number")){
                            sellersNumber = child.getValue().toString();
                        }

                        if(child.getKey().equals("rating")){
                            product_rating = child.getValue().toString();
                        }

                        if(child.getKey().equals("views")){
                            views = child.getValue().toString();
                        }

                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    String partid = key;
                    boolean isNew = false;
                    CarParts obj = new CarParts(partid,imageurl,views,name,description,price,
                            sellersNumber,product_rating,"", "",isNew);
                    resultParts.add(obj);
                    PostRecyclerView.setAdapter(mPostAdapter);
                    mPostAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }


    public ArrayList<CarParts> getParts(){

        return  resultParts;
    }

//    private void initialize(){
//
//        for (int i=0; i<pics.length; i++){
//            picsArr.add(pics[i]);
//        }
//
////        viewPager = (ViewPager)findViewById(R.id.viewPage);
//        viewPager.setAdapter(new ImageAdapter(picsArr,MainActivity.this));
//        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(viewPager);
//
////        Auto start of viewPager
////        We use handler to update the android image slider on a new thread.Enquing an action to be performed
////        on a different thread
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            @Override
//            public void run() {
//                if (currentPage == pics.length){
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++,true);
//            }
//        };
//
////        Using the timer to  schedule task for repeated execution in a background thread
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        },5000,5000);
//
//    }

}