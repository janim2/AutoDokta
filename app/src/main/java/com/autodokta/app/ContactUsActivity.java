package com.autodokta.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.autodokta.app.Adapters.ContactModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity {

    private Button button;
    //    private TextInputLayout name,email,description,orderNumber;
    private EditText nameEdt,emailEdt,descEdt,orderEdt;
    private Spinner subject;

    private String nameString,emailString,descString,orderString,subjectString;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setTitle("Contact Us");

        nameEdt = (EditText)findViewById(R.id.nameEditText);
        emailEdt = (EditText)findViewById(R.id.emailEditText);
        descEdt = (EditText)findViewById(R.id.descEditText);
        orderEdt = (EditText)findViewById(R.id.orderEditText);
        subject = (Spinner)findViewById(R.id.subject);
        button = (Button) findViewById(R.id.submit_button);

        List<String> list = new ArrayList<String>();
        list.add("None");
        list.add("I want to confirm my order");
        list.add("I want to cancel my order");
        list.add("I have a Payment Issue");
        list.add("I want to return my order");
        list.add("I have some other request");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(arrayAdapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    saveData();
                }else{
                    Toast.makeText(ContactUsActivity.this,"No Internet COnnection",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //  Reference to firebase database
    private void saveData(){


        nameString = nameEdt.getText().toString().trim();
        emailString = emailEdt.getText().toString().trim();
        descString = descEdt.getText().toString().trim();
        orderString = orderEdt.getText().toString().trim();
        subjectString = subject.getSelectedItem().toString();

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(emailString) || TextUtils.isEmpty(descString) || TextUtils.isEmpty(orderString)
                || TextUtils.isEmpty(subjectString)){
            Toast.makeText(getApplicationContext(),"Please fill all fields",Toast.LENGTH_SHORT).show();
        }else {
//            save contact details to firebase database
            reference = FirebaseDatabase.getInstance().getReference("contact");
            String contactId = reference.push().getKey();
            ContactModel model = new ContactModel(contactId,nameString,emailString,subjectString,descString,orderString);
            reference.child(contactId).setValue(model);

            nameEdt.setText("");
            emailEdt.setText("");
            orderEdt.setText("");
            descEdt.setText("");
            Add_this_notification_to_database("Contact Us","Thank you for contacting customer service. You will be attended to shortly"
            ,new Date().getTime(),"ACN");
            send_notification_to_user("Contact Us","Thank you for contacting customer service. You will be attended to shortly"
            ,R.drawable.ic_book_black_24dp);
            Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void send_notification_to_user(String title, String message, int the_show_image) {
        String  channel_id = "1";

//        Get an instance of the NotificationServiceManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id,"My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
//            set the user visible description of the channel
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channel_id);
//        Create an intent that will fire when user taps the notification
        Intent intent = new Intent(this,Notifications.class);
//        last argument is a flag paaed which helps the system not to push any notification when
//        there is changes in the system,but to update the current one
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(the_show_image);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setNumber(1);
//        Automatically removes the notification when user taps it
        mBuilder.setAutoCancel(true);

        notificationManager.notify(Integer.parseInt(channel_id),mBuilder.build());
    }

    private void Add_this_notification_to_database(String title, String _message, long time, String imageType) {
        DatabaseReference add_notification = FirebaseDatabase.getInstance().getReference("notification");
        String notify_id = add_notification.push().getKey();
        add_notification.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(notify_id).child("title").setValue(title);

        add_notification.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(notify_id).child("message").setValue(_message);

        add_notification.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(notify_id).child("time").setValue(String.valueOf(time));

        add_notification.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(notify_id).child("imageType").setValue(imageType);
    }
}
