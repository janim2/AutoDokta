package com.autodokta.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_LONG;

public class RegisterActivity extends AppCompatActivity {

//    app crashes because of this
//    E/AndroidRuntime: FATAL EXCEPTION: TokenRefresher

    private EditText inputEmail, inputPassword, firstname, lastname,
            address, phone_number,company_name,company_town,company_email,company_phonenumber,company_password;

    private Button btnSignIn, btnSignUp, btnResetPassword;

    private ProgressBar progressBar;

    private FirebaseAuth auth;

    private DatabaseReference mDatabase;

    private String              sfirstname, slastname,saddress,stelephone,email,password;

    private String              scompany_name, scompany_town,scompany_email,scompany_telepone,scompany_password;

    private Spinner             user_type_spinner;

    private String              usertype_string;

    private String[]            user_type = {"Select Category", "Individual", "Company"};

    private LinearLayout        user_layout, company_layout;

    private CircleImageView     upload_image;

    private CardView            select_nationalID, select_company_cerificate;

    private TextView            national_ID_name, certificate_name;

    private int                 PICK_IMAGE = 100, PICK_PDF = 70;

    private Uri                 imagepath, pdfpath;

    private StorageReference    storageReference;

    private FirebaseStorage     storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user_type_spinner   =   findViewById(R.id.user_type_spinner);
        user_layout         =   findViewById(R.id.individual_form);
        company_layout      =   findViewById(R.id.company_layout);
        btnSignUp           =   findViewById(R.id.sign_up_button);

        //user type spinner
        user_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usertype_string = user_type[position];
                if(usertype_string.equals("Individual")){
                    user_layout.setVisibility(View.VISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    company_layout.setVisibility(View.GONE);
                }
                else if(usertype_string.equals("Company")){
                    user_layout.setVisibility(View.GONE);
                    company_layout.setVisibility(View.VISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);
                }
                else{
                    user_layout.setVisibility(View.GONE);
                    company_layout.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                usertype_string = "Select Category";
            }
        });
        ArrayAdapter<String> tutor_type = new ArrayAdapter<String>(this,R.layout.spinner_layout, user_type);
        user_type_spinner.setAdapter(tutor_type);

        upload_image    =   findViewById(R.id.upload_image);
        inputEmail      =   findViewById(R.id.email);
        inputPassword   =   findViewById(R.id.password);

        firstname           =   findViewById(R.id.firstname);
        lastname            =   findViewById(R.id.lastname);
        address             =   findViewById(R.id.address);
        phone_number        =   findViewById(R.id.phone_number);
        select_nationalID   =   findViewById(R.id.select_nationalID);
        national_ID_name    =   findViewById(R.id.national_ID_name);

        //company edittexts
        company_name                =   findViewById(R.id.company_name);
        company_town                =   findViewById(R.id.company_town);
        company_email               =   findViewById(R.id.company_email);
        company_phonenumber         =   findViewById(R.id.company_phone);
        select_company_cerificate   =   findViewById(R.id.select_company_cerificate);
        certificate_name            =   findViewById(R.id.certificate_name);
        company_password            =   findViewById(R.id.company_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        //setting onclick for the profile image
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        //setting onclick for the national ID
        select_nationalID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPDF();
            }
        });

        //setting onclick for the company cerificate
        select_company_cerificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPDF();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usertype_string.equals("Individual")){
                    //logic for a individual starts here
                    sfirstname  =   firstname.getText().toString().trim();
                    slastname   =   lastname.getText().toString().trim();
                    saddress    =   address.getText().toString().trim();
                    stelephone  =   phone_number.getText().toString().trim();
                    email       =   inputEmail.getText().toString().trim();
                    password    =   inputPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(sfirstname)) {
                        firstname.setError("Required");
                        return;
                    }

                    if (TextUtils.isEmpty(slastname)) {
                        lastname.setError("Required");
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
                }
                else if(usertype_string.equals("Company")){
                    //logic for a company starts here
                    scompany_name       =   company_name.getText().toString().trim();
                    scompany_email      =   company_email.getText().toString().trim();
                    scompany_town       =   company_town.getText().toString().trim();
                    scompany_telepone   =   company_phonenumber.getText().toString().trim();
                    scompany_password   =   company_password.getText().toString().trim();

                    if (TextUtils.isEmpty(scompany_name)) {
                        company_name.setError("Required");
                        return;
                    }

                    if (TextUtils.isEmpty(scompany_email)) {
                        company_email.setError("Required");
                        return;
                    }

                    if (TextUtils.isEmpty(scompany_town)) {
                        company_town.setError("Required");
                        return;
                    }

                    if (TextUtils.isEmpty(scompany_telepone)) {
                        company_phonenumber.setError("Required");
                        return;
                    }

                    if (TextUtils.isEmpty(scompany_password)) {
                        company_password.setError("Required");
                        return;
                    }

                    if (scompany_password.length() < 6) {
                        company_password.setError("Password too short");
                        return;
                    }
                }

                progressBar.setVisibility(View.VISIBLE);
                CreateUserWithEmailPassword(usertype_string);

            }
        });
//    end of registration logic
    }

    private void SelectPDF(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF);
    }

    private void SelectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    private void CreateUserWithEmailPassword(String user_type){
        //create user
        if(user_type.equals("Individual")){
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthUserCollisionException collision){
                                    Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                saveImage();
                                }
                        }
                    });
        }else if(user_type.equals("Company")){
            auth.createUserWithEmailAndPassword(scompany_email, scompany_password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthUserCollisionException collision){
                                    Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            } else {
                               savePDF("");
                            }
                        }
                    });
        }

    }

    private void saveImage(){
        if (imagepath != null) {
                StorageReference verification_image = storageReference.child("images/verification/" + auth.getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());

                // Get the data from an ImageView as bytes
                upload_image.setDrawingCacheEnabled(true);
                upload_image.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) upload_image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                //ends here

                verification_image.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                savePDF(imageUrl);
                            });
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Upload failed", LENGTH_LONG).show();
                }).addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    Toast.makeText(RegisterActivity.this, "Uploading " + ((int) progress) +"%", LENGTH_LONG).show();
                });
            }
            else{
//                    Toast.makeText(RegisterActivity.this, "Profile image required", Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
                    SaveIndividualDetails("","",auth.getCurrentUser().getUid(),sfirstname,
                            slastname,saddress,stelephone);
        }
    }


    private void savePDF(String ImageUrl){
        if(pdfpath != null){
            StorageReference verification_pdf = storageReference.child("documents/verification/" + auth.getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());
            verification_pdf.putFile(pdfpath).addOnSuccessListener(taskSnapshot1 -> {
                if(taskSnapshot1.getMetadata() != null){
                    if (taskSnapshot1.getMetadata().getReference() != null) {
                        Task<Uri> result_ = taskSnapshot1.getStorage().getDownloadUrl();
                        result_.addOnSuccessListener(uri1 -> {
                            String pdfUrl = uri1.toString();
                            if(usertype_string.equals("Individual")){
                                SaveIndividualDetails(ImageUrl,pdfUrl,auth.getCurrentUser().getUid(),sfirstname,
                                        slastname,saddress,stelephone);
                            }
                            else{
                                SaveCompanyDetails(auth.getCurrentUser().getUid(),pdfUrl,scompany_name,
                                        scompany_email,scompany_telepone,scompany_town);
                            }
                        });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, "Upload failed", Toast.LENGTH_LONG).show();
        });
    }else{
//            Toast.makeText(RegisterActivity.this, "Upload National ID", Toast.LENGTH_LONG).show();

        }
    }

    private void SaveIndividualDetails(String profile_image, String pdfurl, String user, final String fname, String lname, String address, String phoneNumber) {
        DatabaseReference add_individual = mDatabase.child("users").child(user);
        add_individual.child("firstname").setValue(fname);
        add_individual.child("lastname").setValue(lname);
        add_individual.child("address").setValue(address);
        add_individual.child("number").setValue(phoneNumber);
        add_individual.child("user_type").setValue(usertype_string);
        add_individual.child("nationalID").setValue(pdfurl);
        add_individual.child("profileimage").setValue(profile_image).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Add_this_notification_to_database("Auto Dokta Registration","Welcome, " +fname
                                +" to the best car parts sales platform. Feel free to contact customter support in times of need"
                        ,new Date().getTime(),"WN");
                send_notification_to_user("Auto Dokta Registration","Welcome, " +fname
                                +" to the best car parts sales platform. Feel free to contact customter support in times of need"
                        ,R.mipmap.ic_launcher_round);
                FirebaseAuth.getInstance().signOut();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(RegisterActivity.this,
                        "Registration successful. Email verification link has been sent to your email", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void SaveCompanyDetails(String user, String business_cert, final String company_name, String company_email, String company_telephone,
                                    String company_town) {
        DatabaseReference comp_reference = FirebaseDatabase.getInstance().getReference("users").child(user);

        comp_reference.child("name").setValue(company_name);
        comp_reference.child("email").setValue(company_email);
        comp_reference.child("telephone").setValue(company_telephone);
        comp_reference.child("town").setValue(company_town);
        comp_reference.child("user_type").setValue(usertype_string);
        comp_reference.child("business_cert").setValue(business_cert);
        comp_reference.child("profileimage").setValue("none").addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Add_this_notification_to_database("Auto Dokta Registration","Welcome, " +company_name
                                +" to the best car parts sales platform. Feel free to contact customter support in times of need"
                        ,new Date().getTime(),"WN");
                send_notification_to_user("Auto Dokta Registration","Welcome, " +company_name
                                +" to the best car parts sales platform. Feel free to contact customter support in times of need"
                        ,R.mipmap.ic_launcher_round);
                FirebaseAuth.getInstance().signOut();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(RegisterActivity.this,
                        "Registration successful. Email verification link has been sent to your email", Toast.LENGTH_LONG).show();

            }
        });

//        sendRegistrationEmail("register",company_name);
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
        String user_id   = FirebaseAuth.getInstance().getCurrentUser().getUid();

        assert notify_id != null;
        add_notification.child(user_id).child(notify_id).child("title").setValue(title);

        add_notification.child(user_id).child(notify_id).child("message").setValue(_message);

        add_notification.child(user_id).child(notify_id).child("time").setValue(String.valueOf(time));

        add_notification.child(user_id).child(notify_id).child("imageType").setValue(imageType);

        add_notification.child(user_id).child(notify_id).child("imageType").setValue(imageType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imagepath = data.getData();
            try {
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
                    upload_image.setImageBitmap(bitmap);
                }catch (OutOfMemoryError e){

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if(requestCode == PICK_PDF && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            pdfpath = data.getData();
            try{
                String uriString = pdfpath.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = this.getContentResolver().query(pdfpath, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            if(usertype_string.equals("Individual")){
                                national_ID_name.setText(displayName);
                            }
                            else{
                                certificate_name.setText(displayName);
                            }
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                    if(usertype_string.equals("Individual")){
                        national_ID_name.setText(displayName);
                    }
                    else{
                        certificate_name.setText(displayName);
                    }
                }
            }
            catch (OutOfMemoryError e){
            }
        }

    }
}
