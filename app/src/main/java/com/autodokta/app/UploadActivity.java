package com.autodokta.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText product_name,product_price,seller,product_desc;
    CircleImageView imageView;
    Button product;
    String name_str,price_str,seller_str,desc_str,image_str;
    private Uri filepath;
//    ImageView uploaded;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseDatabase  firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


//        Using Toolbar as the appbar
        toolbar = (Toolbar)findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Ad Upload");

        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                going back to previous activity
                startActivity(new Intent(UploadActivity.this,MainActivity.class));
                finish();
            }
        });



//        Calling various view using their respective id's
        product_name = (EditText)findViewById(R.id.product_name);
        product_price = (EditText)findViewById(R.id.product_price);
        seller = (EditText)findViewById(R.id.seller_number);
        product_desc = (EditText)findViewById(R.id.product_desc);

//        choose = (Button)findViewById(R.id.choose_image);
        imageView = (CircleImageView)findViewById(R.id.choose_image);
        product = (Button)findViewById(R.id.ad_upload);

//        uploaded = (ImageView)findViewById(R.id.uploaded_image);

//        setting clickListener for the product button
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                first check if all fields are filled
//                convert various fields to string
                name_str = product_name.getText().toString();
                price_str = product_price.getText().toString();
                seller_str = seller.getText().toString();
                desc_str = product_desc.getText().toString();
//                image_str = choose.getText().toString();

//                display error message for empty fields
                if (checkEmptyFiled(name_str)||checkEmptyFiled(price_str)||checkEmptyFiled(seller_str)||checkEmptyFiled(desc_str)){
                    Toast.makeText(getApplicationContext(),"Fields required",Toast.LENGTH_SHORT).show();
                }else {

//                        choose.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                chooseImage();
//                            }
//                        });

                        uploadImageToFireStore();


                    }



                }

        });


    }



    //    method to check for empty fields
    public boolean checkEmptyFiled(String field){
       if (TextUtils.isEmpty(field)){
           return true;
       }else {
           return false;
       }

    }



    private void chooseImage(){
//        creating a new intent instance
        Intent intent = new Intent();
//                    get an image content
        intent.setType("image/*");
//                    image choser dialog that allows user to browse the device gallery to select the image
        intent.setAction(Intent.ACTION_GET_CONTENT);
//                    receiving the selected image
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
//                display selected image in the ImageView
                imageView.setImageBitmap(bitmap);

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void uploadImageToFireStore(){
//        creating firebase storage instance
        storage = FirebaseStorage.getInstance();
//        pointing to the uploaded file
        storageReference =storage.getReference();

        if (filepath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
            reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata().getReference() != null){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = String.valueOf(uri);
                                        saveDataToFirebase(url);


                                    }
                                });
                            }
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this,"uploaded",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this,"Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("uploaded: "+ (int)progress+"%");
                        }
                    });
        }
    }

    private void saveDataToFirebase(String url){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference().child("ads");
        Map<String,String> upload = new HashMap<String, String>();
        upload.put("title",name_str);
        upload.put("price",price_str);
        upload.put("seller_number",seller_str);
        upload.put("description",desc_str);
        upload.put("image_url",url);
        databaseReference.setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UploadActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(UploadActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        });

    }







}
