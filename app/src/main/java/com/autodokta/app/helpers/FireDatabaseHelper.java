package com.autodokta.app.helpers;

import com.autodokta.app.Models.UserAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FireDatabaseHelper {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private List<UserAds> userAdsList = new ArrayList<>();

    public interface DataStatus{

        void DataIsLoaded(List<UserAds> userAdsList, List<String> key);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }


    public FireDatabaseHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("services");
    }

    public void updatePost(String key, UserAds userAds, final DataStatus dataStatus){



        reference.child(key).setValue(userAds).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });

    }

    public void deletePost(String key, final DataStatus dataStatus){
        reference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }

//    public void Title(String title){
//
//        reference.child("services").child("title").setValue(title);
//
//    }
}
