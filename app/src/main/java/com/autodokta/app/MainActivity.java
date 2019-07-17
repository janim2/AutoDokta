package com.autodokta.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.autodokta.app.fragments.ImageListFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;
    FirebaseAuth mAuth;
    Menu menu;
    MenuItem menuItem, profilemenuitem, garageMenuItem;
    FrameLayout layout;
    TextView cartnumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        layout = (FrameLayout)findViewById(R.id.cartframe);
        cartnumberTextView = (TextView) findViewById(R.id.cartnumber);

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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.getTabAt(0).setText(getString(R.string.item_1));
            tabLayout.getTabAt(1).setText(getString(R.string.item_2));
            tabLayout.getTabAt(2).setText(getString(R.string.item_3));
            tabLayout.getTabAt(3).setText(getString(R.string.item_4));
            tabLayout.getTabAt(4).setText(getString(R.string.item_5));
            tabLayout.getTabAt(5).setText(getString(R.string.item_6));
            tabLayout.getTabAt(6).setText(getString(R.string.item_7));
            tabLayout.getTabAt(7).setText(getString(R.string.item_8));
        }

//        getting the menu from the navigation item;
        menu = navigationView.getMenu();
//        getting the menuitem that i want to change
        menuItem = menu.findItem(R.id.logout);
        profilemenuitem = menu.findItem(R.id.profile);
        garageMenuItem = menu.findItem(R.id.garage);


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.cartframe:
//                if(mAuth.getCurrentUser()!=null){
//                    startActivity(new Intent(MainActivity.this,Cart.class));
//
//                }else{
//                 Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_LONG).show();
//                }
//                return true;
//        }
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
            getCount(mAuth.getCurrentUser());

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

    private void getCount(FirebaseUser user) {
        try {
            DatabaseReference getcartCount = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid());
            getcartCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey()!=null){
//                                Toast.makeText(MainActivity.this,child.getChildrenCount() + "",Toast.LENGTH_LONG).show();
                                cartnumberTextView.setText(child.getChildrenCount()+"");
                            }
                        }
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
}