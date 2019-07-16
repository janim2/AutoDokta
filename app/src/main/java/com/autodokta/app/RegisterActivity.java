package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
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
//                                       send verification email to the user
                                    sendVerificationEmail();

//                                    After email is sent,logout the user and finish this activity
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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

    }

}
