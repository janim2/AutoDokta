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

    private String              sfirstname, slastname,saddress,stelephone,email,password,individualImage;

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

        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();

//        user_type_spinner   =   findViewById(R.id.user_type_spinner);
        user_layout         =   findViewById(R.id.individual_form);
//        company_layout      =   findViewById(R.id.company_layout);
        btnSignUp           =   findViewById(R.id.sign_up_button);


        inputEmail      =   findViewById(R.id.email);
        inputPassword   =   findViewById(R.id.password);

        firstname           =   findViewById(R.id.firstname);
        lastname            =   findViewById(R.id.lastname);
        address             =   findViewById(R.id.address);
        phone_number        =   findViewById(R.id.phone_number);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);






        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

//

                    if (password.length() < 6) {
                        inputPassword.setError("Password too short");
                        return;
                    }




                progressBar.setVisibility(View.VISIBLE);
                CreateUserWithEmailPassword(sfirstname,slastname,saddress,stelephone);

            }
        });
//    end of registration logic
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void CreateUserWithEmailPassword(String fName, String lName, String address, String mobile){

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//
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
//
                                SaveIndividualDetails(auth.getCurrentUser().getUid(),fName,lName,address,mobile);

                                }
                        }
                    });



    }




    private void SaveIndividualDetails(String user, final String fname, String lname, String address, String phoneNumber) {
        DatabaseReference add_individual = mDatabase.child("users").child(user);
        add_individual.child("firstname").setValue(fname);
        add_individual.child("lastname").setValue(lname);
        add_individual.child("address").setValue(address);
        add_individual.child("number").setValue(phoneNumber);

        Toast.makeText(RegisterActivity.this, "Registration successful", LENGTH_LONG).show();

        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }


    }




