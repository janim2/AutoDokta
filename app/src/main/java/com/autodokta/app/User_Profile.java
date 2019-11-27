package com.autodokta.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ImageView editiamge;
    Button submitbutton;
    CircleImageView profileimage;
    TextView fullname, email, username, firstname, lastname, address, phonenumber,textstatus;
    String sprofileimage,sfirstname,slastname,snumber,saddress,susername, soldpassword, snewpassword;

    EditText fnameeText, lnameeText, emaileText, usernameeText, addresseText, numbereText, oldPassword,
    newPassword;
    ProgressBar loading;

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fullname = (TextView)findViewById(R.id.fullname);
        email = (TextView)findViewById(R.id.email);
        username = (TextView)findViewById(R.id.username);
        address = (TextView)findViewById(R.id.address);
        phonenumber = (TextView)findViewById(R.id.mobile);
        textstatus = (TextView)findViewById(R.id.text_status);
        editiamge = (ImageView) findViewById(R.id.editimage);
        loading = (ProgressBar) findViewById(R.id.loading);

//        fnameeText = (EditText) findViewById(R.id.fnameedit_text);
//        lnameeText = (EditText) findViewById(R.id.lnameedit_text);
//        emaileText = (EditText) findViewById(R.id.emailedit_text);
//        usernameeText = (EditText) findViewById(R.id.usernameedit_text);
//        addresseText = (EditText) findViewById(R.id.addressedit_text);
//        numbereText = (EditText) findViewById(R.id.numberedit_text);

        oldPassword = (EditText) findViewById(R.id.oldpassword);
        newPassword = (EditText) findViewById(R.id.newpassword);
        profileimage = (CircleImageView) findViewById(R.id.profilecircleView);

        submitbutton = (Button) findViewById(R.id.submit);

        try{
            if(user!=null){
                getUserProfile();
            }
        }catch (NullPointerException e){

        }

        editiamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        activating the clickability of the image
                profileimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermission("android.permission.READ_EXTERNAL_STORAGE",
                                "External Storage", 20);
                        galleryAction(2000);

                    }
                });

                fullname.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
                phonenumber.setVisibility(View.GONE);

                editiamge.setVisibility(View.GONE);
//                fnameeText.setVisibility(View.VISIBLE);
//                lnameeText.setVisibility(View.VISIBLE);
//                emaileText.setVisibility(View.VISIBLE);
//                usernameeText.setVisibility(View.VISIBLE);
//                addresseText.setVisibility(View.VISIBLE);
//                numbereText.setVisibility(View.VISIBLE);
                oldPassword.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.VISIBLE);

//                try {
//                    fnameeText.setText(sfirstname);
//                    lnameeText.setText(slastname);
//                    emaileText.setText(user.getEmail());
//                    usernameeText.setText(susername);
//                    addresseText.setText(saddress);
//                    numbereText.setText(snumber);
//                }catch (NullPointerException e){
//
//                }

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sfirstname = fnameeText.getText().toString().trim();
//                slastname = lnameeText.getText().toString().trim();
//                String emaill = emaileText.getText().toString().trim();
//                susername = usernameeText.getText().toString().trim();
//                saddress = addresseText.getText().toString().trim();
//                snumber = numbereText.getText().toString().trim() ;
                soldpassword = oldPassword.getText().toString().trim();
                snewpassword = newPassword.getText().toString().trim();

                if(user!=null){
                    DatabaseReference edit = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                    loading.setVisibility(View.VISIBLE);
                    if(!soldpassword.equals("")){
                        if(!snewpassword.equals("")){
                            changePassword(soldpassword,snewpassword);
                        }else{
                            newPassword.setError("Required");
                        }
                    }else{
                        oldPassword.setError("Required");
                    }
                    uploadFile(getBitmap(profileimage));
                    //editting the user data
//                    edit.child("firstname").setValue(sfirstname);
//                    edit.child("lastname").setValue(slastname);
//                    edit.child("username").setValue(susername);
//                    edit.child("address").setValue(saddress);
//                    edit.child("number").setValue(snumber);
                    edit.child("profileimage").setValue(user.getUid()+".jpg");
//                    user.updateEmail(emaill);

//                    fnameeText.setVisibility(View.GONE);
//                    lnameeText.setVisibility(View.GONE);
//                    emaileText.setVisibility(View.GONE);
//                    usernameeText.setVisibility(View.GONE);
//                    addresseText.setVisibility(View.GONE);
//                    numbereText.setVisibility(View.GONE);
                    oldPassword.setVisibility(View.GONE);
                    newPassword.setVisibility(View.GONE);
                    submitbutton.setVisibility(View.GONE);

                    fullname.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    phonenumber.setVisibility(View.VISIBLE);

                    editiamge.setVisibility(View.VISIBLE);

//                    setting the texts
                    fullname.setText(sfirstname + " " + slastname);
                    email.setText(user.getEmail());
                    username.setText(susername);
                    address.setText(saddress);
                    phonenumber.setText(snumber);
                }

            }
        });

    }

    private void changePassword(String oldpass, final String newPass) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),oldpass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                loading.setVisibility(View.GONE);
                                textstatus.setVisibility(View.VISIBLE);
                                textstatus.setTextColor(getResources().getColor(R.color.red));
                                textstatus.setText("Something went wrong. Please try again later");
                            }else {
                                loading.setVisibility(View.GONE);
                                textstatus.setVisibility(View.VISIBLE);
                                textstatus.setTextColor(getResources().getColor(R.color.colorGreen));
                                textstatus.setText("Password Successfully Modified");
                            }
                        }
                    });
                }else{
                    loading.setVisibility(View.GONE);
                    textstatus.setVisibility(View.VISIBLE);
                    textstatus.setTextColor(getResources().getColor(R.color.red));
                    textstatus.setText("Authentication Failed. Old password incorrect");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            profileimage.setImageURI(uri);
        }
    }

    private void getUserProfile() {
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.getKey().equals("address")){
                        saddress = child.getValue().toString();
                    }
                    if(child.getKey().equals("firstname")){
                        sfirstname = child.getValue().toString();
                    }
                    if(child.getKey().equals("lastname")){
                        slastname = child.getValue().toString();
                    }
                    if(child.getKey().equals("number")){
                        snumber = child.getValue().toString();
                    }
                    if(child.getKey().equals("username")){
                        susername = child.getValue().toString();
                    }
                    if(child.getKey().equals("profileimage")){
                        sprofileimage = child.getValue().toString();
                    }


                    try {

                        if(sprofileimage.equals("none")){

                        }else{
                            DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                                    cacheOnDisk(true).build();
                            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
                                    defaultDisplayImageOptions(theImageOptions).build();
                            ImageLoader.getInstance().init(config);
//
//                            String profileimagelink = "gs://cars-9c22a.appspot.com/profileimages/" + sprofileimage;
                            String profileimagelink = sprofileimage;
                            imageLoader.displayImage(profileimagelink,profileimage);
                        }
                        fullname.setText(sfirstname + " " + slastname);
                        email.setText(user.getEmail());
                        username.setText(susername);
                        address.setText(saddress);
                        phonenumber.setText(snumber);
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public boolean checkPermission(String permission, String msg, int MY_PERMISSIONS_REQUEST){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(User_Profile.this, permission) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)User_Profile.this, permission)){
//                    showDialog(msg, User_Profile.this, permission, MY_PERMISSIONS_REQUEST);
                        Toast.makeText(User_Profile.this,permission + "not Granted",Toast.LENGTH_LONG).show();
                }else{
                    ActivityCompat.requestPermissions((Activity)User_Profile.this, new String[]{permission},
                            MY_PERMISSIONS_REQUEST);
                    return false;
                }
            }else{
                return true;
            }
        }else{
            return true;
        }
        return false;
    }

    public Bitmap getBitmap(CircleImageView theimageview){
        try{
            Bitmap encodingImage = Bitmap.createBitmap(theimageview.getDrawable().getIntrinsicWidth(),theimageview.getDrawable().getIntrinsicHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(encodingImage);
            theimageview.getDrawable().setBounds(0,0,canvas.getWidth(), canvas.getHeight());
            theimageview.getDrawable().draw(canvas);
            return encodingImage;
        }

        catch (OutOfMemoryError e){
            return null;
        }
    }


    public void galleryAction(final int RESULT_LOAD_IMAGE){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(User_Profile.this instanceof Activity){
            ((Activity) User_Profile.this).startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }else{
            Toast.makeText(User_Profile.this, "Error: Context should be an instance of activity", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadFile(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://cars-9c22a.appspot.com/");
        StorageReference profileImagesRef = storageRef.child("profileimages/" + user.getUid() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                sendMsg("" + downloadUrl, 2);
                Log.d("downloadUrl-->", "" + downloadUrl);
            }
        });

    }

}
