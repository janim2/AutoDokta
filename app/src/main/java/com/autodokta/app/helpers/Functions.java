package com.autodokta.app.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.autodokta.app.CustomRequestActivity;
import com.autodokta.app.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Functions {
    Context     context;

    public Functions(Context context) {
        this.context = context;
    }

    public void ShowToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void SendNotifcationsToAllUsers(String title, String message, long time, String imageType, String request_id) {
        try{
            DatabaseReference allusers = FirebaseDatabase.getInstance().getReference("users");
            allusers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            SaveNotifications(child.getKey(), title, message, time, imageType, request_id);
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                        ShowToast("Cancelled");
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void SaveNotifications(String user_id, String title, String _message, long time, String imageType, String request_id){
        DatabaseReference add_notification = FirebaseDatabase.getInstance().getReference("notification");
        String notify_id = add_notification.push().getKey();
        add_notification.child(user_id).child(notify_id).child("title").setValue(title);

        add_notification.child(user_id).child(notify_id).child("message").setValue(_message);

        add_notification.child(user_id).child(notify_id).child("time").setValue(String.valueOf(time));

        add_notification.child(user_id).child(notify_id).child("imageType").setValue(imageType);

        add_notification.child(user_id).child(notify_id).child("request_id").setValue(request_id);

        add_notification.child(user_id).child(notify_id).child("status").setValue("temporary");

    }

    public void ShowNotificationDrawer(String title, String message, int the_show_image) {
        String  channel_id = "1";

//        Get an instance of the NotificationServiceManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id,"My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
//            set the user visible description of the channel
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,channel_id);
//        Create an intent that will fire when user taps the notification
        Intent intent = new Intent(context, Notifications.class);
//        last argument is a flag paaed which helps the system not to push any notification when
//        there is changes in the system,but to update the current one
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(the_show_image);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setNumber(1);
//        Automatically removes the notification when user taps it
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(Integer.parseInt(channel_id),mBuilder.build());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
