package com.autodokta.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.ui.helpers.InstantSearch;

import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.ImageAdapter;
import com.autodokta.app.Adapters.PartsAdapter;
//import com.autodokta.app.SearchItems.ResultsListView;
import com.autodokta.app.helpers.Space;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.autodokta.app.fragments.ImageListFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;
    FirebaseAuth mAuth;
    public Menu menu;
    private MenuItem menuItem, profilemenuitem, garageMenuItem;
    private FrameLayout layout;
    private TextView cartnumberTextView, guest_or_user;
    public NavigationView navigationView;
    private int cart_number;

    // Constants
    private static final int LOAD_MORE_THRESHOLD = 5;
    private static final int HITS_PER_PAGE = 20;


//    strings for loading the parts from the database
    private ArrayList resultParts = new ArrayList<CarParts>();
    private RecyclerView PostRecyclerView;
    private RecyclerView.Adapter mPostAdapter;
    private RecyclerView.LayoutManager mPostLayoutManager;
    private String imageurl, name, description, price, sellersNumber, product_rating;
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


        initialize();

//        getting the menu from the navigation item;
        menu = navigationView.getMenu();
//        getting the menuitem that i want to change
        menuItem = menu.findItem(R.id.logout);
        profilemenuitem = menu.findItem(R.id.profile);
        garageMenuItem = menu.findItem(R.id.garage);
//        wishlistMenuItem = menu.findItem(R.id.wish_list);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this,Cart.class));

                }else{
                    Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_LONG).show();
                }
            }
        });

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
//        items ends here
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem wish_list = menu.findItem(R.id.wish_list);
        MenuItem chat_system = menu.findItem(R.id.chat);
        MenuItem upload = menu.findItem(R.id.upload);

        if(mAuth.getCurrentUser() != null){
            wish_list.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(MainActivity.this,WishList.class));
                    return false;
                }
            });

            chat_system.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(MainActivity.this,Chat.class));
                    return false;
                }
            });

            upload.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(MainActivity.this,UploadActivity.class));
                    return false;
                }
            });
        }else{
            wish_list.setVisible(false);
            chat_system.setVisible(false);
            upload.setVisible(false);
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
            getNumber_in_Cart();
//            guest_or_user.setText("Welcome user");

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


                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ImageListFragment fragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 6);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 7);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 8);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment);

        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        int id = menuItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_item5) {
//            manager.beginTransaction().replace(R.id.content_main,new AboutUs()).commit();
            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            return true;
        }

        if (id == R.id.logout) {
            AlertDialog.Builder logout = new AlertDialog.Builder(MainActivity.this, R.style.Myalert);
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

       if (id == R.id.sell){
//           if user is not logged in
           if (mAuth.getCurrentUser() ==  null){
//               redirect user to the login activity
               startActivity(new Intent(getApplicationContext(),LoginActivity.class));
           }else {
               startActivity(new Intent(getApplicationContext(),UploadActivity.class));
           }
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

        partdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
//        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("carparts").child("Offers").child(key);
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("allParts").child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            price = child.getValue().toString();
                        }

                        if(child.getKey().equals("buyersNumber")){
                            sellersNumber = child.getValue().toString();
                        }

                        if(child.getKey().equals("rating")){
                            product_rating = child.getValue().toString();
                        }

                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    String partid = key;
                    boolean isNew = false;
                    CarParts obj = new CarParts(partid,imageurl,name,description,price, isNew,
                            sellersNumber,"",product_rating);
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


    private void initialize(){

        for (int i=0; i<pics.length; i++){
            picsArr.add(pics[i]);
        }

        viewPager = (ViewPager)findViewById(R.id.viewPage);
        viewPager.setAdapter(new ImageAdapter(picsArr,MainActivity.this));
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

//        Auto start of viewPager
//        We use handler to update the android image slider on a new thread.Enquing an action to be performed
//        on a different thread
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == pics.length){
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++,true);
            }
        };

//        Using the timer to  schedule task for repeated execution in a background thread
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },5000,5000);

    }

}