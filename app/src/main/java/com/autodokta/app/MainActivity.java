package com.autodokta.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.autodokta.app.fragments.ImageListFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
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

        if (id == R.id.help_center){
            startActivity(new Intent(MainActivity.this,HelpCenterActivity.class));
            return true;
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