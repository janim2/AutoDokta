package com.autodokta.app.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.autodokta.app.MainActivity;
import com.autodokta.app.R;
import com.autodokta.app.helpers.Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {

    DatabaseReference   notificationss_ref;

    FirebaseAuth        myauth                     =    FirebaseAuth.getInstance();

    String              title, message, time, status, imageType;

    Functions           functions;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationss_ref = FirebaseDatabase.getInstance().getReference("notification")
                .child(myauth.getUid());
        notificationss_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        ScanThroughMessages(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


    private void ScanThroughMessages(String notification_id) {
        notificationss_ref = FirebaseDatabase.getInstance().getReference("notification")
                .child(myauth.getUid()).child(notification_id);
        notificationss_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("title")){
                            title = child.getValue().toString();
                        }
                        if(child.getKey().equals("message")){
                            message = child.getValue().toString();
                        }
                        if(child.getKey().equals("time")){
                            time = child.getValue().toString();
                        }
                        if(child.getKey().equals("imageType")){
                            imageType = child.getValue().toString();
                        }
                        if(child.getKey().equals("status")){
                            status = child.getValue().toString();
                        }
                    }
                    //sending notification if notification status is temporal
//                    try {
//                    if(status.equals("temporary")){
//                            NotificationIsNowActive(notification_id);
//                        }
//                    }catch (NullPointerException e){
//                        e.printStackTrace();
//                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void NotificationIsNowActive(String notification_id) {
        notificationss_ref = FirebaseDatabase.getInstance().getReference("notification")
                .child(myauth.getUid()).child(notification_id);
        notificationss_ref.child("status").setValue("active").addOnCompleteListener(task -> ShowNotificationOnPhone());
    }

    private void ShowNotificationOnPhone() {
        functions = new Functions(getApplicationContext());
        functions.ShowNotificationDrawer("New Request", "A buyer has requested for a product. Press call to get in touch with buyer if you have the product.",
                R.mipmap.ic_launcher_round);
    }
}
