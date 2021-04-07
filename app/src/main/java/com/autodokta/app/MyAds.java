package com.autodokta.app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autodokta.app.Adapters.UserAdsAdapter;
import com.autodokta.app.Models.UserAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyAds extends AppCompatActivity {
    private static final String TAG = MyAds.class.getSimpleName();
    DatabaseReference databaseReference;

    List<UserAds> userAdsList = new ArrayList<>();
    RecyclerView recyclerView;
//    RecyclerView.Adapter adapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    UserAdsAdapter userAdsAdapter;
    FirebaseDatabase firebaseDatabase;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);

//        firebaseAuth = FirebaseAuth.getInstance();
//        disk persistence for offline cached of user's data
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        getSupportActionBar().setTitle("My Ads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView)findViewById(R.id.all_ads);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         String user = firebaseUser.getUid();




        if (user != null){
//
//            String userId = user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            Query userQuery = databaseReference.child("services").child(firebaseUser.getUid());
            userQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserAds userAds = snapshot.getValue(UserAds.class);
                            userAdsList.add(userAds);
//                            String postKey = dataSnapshot.getKey();

                        }

                        userAdsAdapter = new UserAdsAdapter(MyAds.this, userAdsList);
                        recyclerView.setAdapter(userAdsAdapter);
//                        String userKey = dataSnapshot.getKey();
//                        Log.i(TAG, "Post key: " + userKey);
                        setClick();



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "Database error: " + databaseError.getMessage());
                }
            });



        }else{
            Log.i(TAG,"Firebase User: " + firebaseUser.getUid());
        }

    }
//    end of OnCreate method

        private void setClick(){

        userAdsAdapter.setOnCallBack(new UserAdsAdapter.OnCallBack() {
            @Override
            public void onButtonDeleteClick(UserAds userAds) {
//                Log.i(TAG, "User: " + key);
//                Log.i(TAG, "Post key: " + userAds.getId());
                deleteAd(userAds);
            }

            @Override
            public void onButtonEditClick(UserAds userAds) {
//                    showDialogueUpdateNote(userAds);
            }
        });
    }
//
//    private void showDialogueUpdateNote(UserAds userAds) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.edit_dialogue_layout);
//
////        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.GREEN));
//        dialog.setCancelable(true);
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(layoutParams);
//
//        final EditText price = dialog.findViewById(R.id.dialog_price);
//        price.setText(userAds.getPrice());
//
//        Button updateBtn = dialog.findViewById(R.id.dialog_button);
//        updateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(price.getText())){
//                    price.setError("enter price");
//                }else {
//                    updatePost(userAds,price.getText().toString());
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        dialog.show();
//
//
//    }
//
//    private void updatePost(UserAds userAds, String text) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services");
//        reference.child(userAds.getId()).child("price").setValue(text).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(),"Update Successful", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private void deleteAd(UserAds user){

        databaseReference = FirebaseDatabase.getInstance().getReference("services");
        String postKey = databaseReference.getKey();
           Log.i(TAG, "Post keys: " + postKey);
        databaseReference.child(user.getId()).child(Objects.requireNonNull(postKey)).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(),"Delete Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
































