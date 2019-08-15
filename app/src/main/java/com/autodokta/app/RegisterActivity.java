package com.autodokta.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

public class RegisterActivity extends AppCompatActivity {

//    app crashes because of this
//    E/AndroidRuntime: FATAL EXCEPTION: TokenRefresher

    private EditText inputEmail, inputPassword, firstname, lastname, username,
            address, phone_number;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;

    String sfirstname,slastname,susername,saddress,stelephone,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        username = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.address);
        phone_number = (EditText) findViewById(R.id.phone_number);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

//        btnResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RegisterActivity.this, ResetPasswordActivity.class));
//            }
//        });

//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sfirstname = firstname.getText().toString().trim();
                slastname = lastname.getText().toString().trim();
                susername = username.getText().toString().trim();
                saddress = address.getText().toString().trim();
                stelephone = phone_number.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(sfirstname)) {
                    firstname.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(slastname)) {
                    lastname.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(susername)) {
                    username.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(saddress)) {
                    address.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(stelephone)) {
                    phone_number.setError("Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Required");
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Password too short");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    onAuthSuccess(auth.getCurrentUser().getUid(),sfirstname,slastname,susername,saddress,stelephone);
                                    finish();
                                }
                            }
                        });

            }
        });
//    end of registration logic
    }

    private void onAuthSuccess(String user,String fname, String lname, String username, String address, String phoneNumber) {
        mDatabase.child("users").child(user).child("firstname").setValue(fname);
        mDatabase.child("users").child(user).child("lastname").setValue(lname);
        mDatabase.child("users").child(user).child("username").setValue(username);
        mDatabase.child("users").child(user).child("address").setValue(address);
        mDatabase.child("users").child(user).child("number").setValue(phoneNumber);
        mDatabase.child("users").child(user).child("profileimage").setValue("none");
        Add_this_notification_to_database("Auto Dokta Registration","Welcome, " +fname
                        +" to the best car parts sales platform. Feel free to contact customter support in times of need"
                ,new Date().getTime(),"WN");
        send_notification_to_user("Auto Dokta Registration","Welcome, " +fname
                +" to the best car parts sales platform. Feel free to contact customter support in times of need"
        ,R.mipmap.ic_launcher_round);
        sendRegistrationEmail("register",fname);
    }

    private void sendRegistrationEmail(String status, String name) {
        new Sending_mail("https://knust-martial-arts.000webhostapp.com/AutoDoktaService.php"
                ,status,name).execute();
    }

    class Sending_mail extends AsyncTask<Void, Void, String> {

        String url_location,status, name;

        public Sending_mail(String url_location,String status, String name) {
            this.url_location = url_location;
            this.status = status;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                String data = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

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
            if (s.equals("Registration Successful")){
                Toast.makeText(RegisterActivity.this, s, LENGTH_LONG).show();
            }
            Toast.makeText(RegisterActivity.this, s, LENGTH_LONG).show();

        }

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
