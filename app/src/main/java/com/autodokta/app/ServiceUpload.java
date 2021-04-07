package com.autodokta.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.autodokta.app.Adapters.CategorisedServicesAdapter;
import com.autodokta.app.Models.UserAds;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceUpload extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {



    private static final String TAG = ServiceUpload.class.getSimpleName();
    private EditText service_name,service_price,service_seller,service_short_desc,service_full_desc,service_location;
    private CircleImageView circleImageView;
    private Button serviceUpload;
    private Spinner spinner;
    private String name_str,price_str,seller_str,short_str,full_str, loc_str, img_str,selectedItem;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageUri;
    private  int PICK_IMAGE = 100;
    private FirebaseAuth firebaseAuth;
    UserAds userAds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_upload);

       getSupportActionBar().setTitle("Service Upload");

       firebaseAuth = FirebaseAuth.getInstance();
       String userId = firebaseAuth.getCurrentUser().getUid();



//        calling various views uing their respective id's
        service_name = (EditText)findViewById(R.id.service_name);
        service_price = (EditText)findViewById(R.id.service_price);
        service_seller = (EditText)findViewById(R.id.service_seller_number);
        service_short_desc = (EditText)findViewById(R.id.service_short_desc);
        service_full_desc = (EditText)findViewById(R.id.service_full_desc);
        service_location = (EditText)findViewById(R.id.service_loc);

        spinner = (Spinner)findViewById(R.id.service_type);
        spinner.setOnItemSelectedListener(this);
//        Create and arrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.service_type_array,
                android.R.layout.simple_spinner_item);
//        specifies the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        apply the adapter to the spinner
        spinner.setAdapter(adapter);



        circleImageView = (CircleImageView)findViewById(R.id.service_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();
            }
        });

        serviceUpload = (Button)findViewById(R.id.service_upload);
        //attach a click listener to the upload button
        serviceUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get field values as a string
                name_str = service_name.getText().toString();
                price_str = service_price.getText().toString();
                seller_str = service_seller.getText().toString();
                short_str = service_short_desc.getText().toString();
                full_str = service_full_desc.getText().toString();
                loc_str = service_location.getText().toString();

                if (checkEmptyFields(name_str)|| checkEmptyFields(price_str)||checkEmptyFields(seller_str)||
                checkEmptyFields(short_str)||checkEmptyFields(full_str)||checkEmptyFields(loc_str)){
                    Toast.makeText(getApplicationContext(),"Fileds are required",Toast.LENGTH_SHORT).show();
                }else if(imageUri == null) {
                    Toast.makeText(ServiceUpload.this, "Image seclection required",Toast.LENGTH_SHORT).show();
                }else {
                    uploadImageToFireStore(userId,name_str,price_str,seller_str,short_str,full_str,loc_str);
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private  boolean checkEmptyFields(String field){
        if (TextUtils.isEmpty(field)) {
            return true;
        } else {
            return false;
        }
    }

    private void chooseImage(){
        //creating a new intent instance
        Intent intent = new Intent();
        //get an image content
        intent.setType("image/*");
        //image choser dialog that allows user to browse the device gallery to select the image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //receiving the selected image
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                display selected image in the ImageView
                circleImageView.setImageBitmap(bitmap);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void uploadImageToFireStore(String id, String title,String price,String seller, String shortDesc, String fullDesc,String location){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        if (imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference reference = storageReference.child("services-images/"+ UUID.randomUUID().toString());
            Log.i(TAG,"reference: " + reference);

            reference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {
                                  String imageUrl = uri.toString();
                                  Log.i(TAG,":image url" + imageUrl);
//                                  System.out.println("image url: " + imageUrl);
                                  saveData(id,title,price,seller,shortDesc,fullDesc,location,imageUrl,selectedItem);
                              }
                          });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
    }

    private void saveData(String id, String title, String price, String seller, String shortDesc,String fullDesc,String loc, String url,String itemSelected){
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        userAds = new UserAds(id,title,shortDesc,fullDesc,price,url,loc,seller,itemSelected);
        databaseReference.child("services").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(userAds)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ServiceUpload.this,"Upload Successful",Toast.LENGTH_LONG).show();
//                    service_name.getText().clear();
                            clearForm((ViewGroup)findViewById(R.id.service_form));
                        }else {
                            Toast.makeText(ServiceUpload.this,"Upload Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Exception: " +e.getMessage());
            }
        });


//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference().child("services");
//        firebaseAuth = FirebaseAuth.getInstance();
//        final Map<String, String> upload = new HashMap<>();
//        upload.put("title",         name_str);
//        upload.put("price",         price_str);
//        upload.put("seller_number", seller_str);
//        upload.put("short_description",   short_str);
//
//        upload.put("location",   loc_str);
//        upload.put("image_url",     url);
//        upload.put("service_type",     selectedItem);
////        upload.put("uploader_id",   firebaseAuth.getCurrentUser().getUid());
//        upload.put("views",      "0");
//        upload.put("rating",     "0");
//        upload.put("full description",   full_str);
//        databaseReference.push().child(firebaseAuth.getCurrentUser().getUid()).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//
//                    Toast.makeText(ServiceUpload.this,"Upload Successful",Toast.LENGTH_LONG).show();
////                    service_name.getText().clear();
//                    clearForm((ViewGroup)findViewById(R.id.service_form));
//
////                    AlertDialog.Builder uploaded = new AlertDialog.Builder(ServiceUpload.this);
////                    uploaded.setCancelable(true);
////                    uploaded.setMessage("Your item "+name_str+" has been uploaded.");
////                    uploaded.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            dialog.dismiss();
////
////                        }
////                    });
//
////                    uploaded.show();
//
//
//
//
//
//
//                }else {
//                    Toast.makeText(ServiceUpload.this,"Upload Failed",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("Exception: " +e.getMessage());
//            }
//        });

    }

    private void clearForm(ViewGroup group){

//        ViewGroup  viewGroup = (ViewGroup)findViewById(R.id.service_form);
        for (int i=0, count = group.getChildCount(); i<count; i++){
            View view = group.getChildAt(i);
            if (view instanceof  EditText){
                ((EditText)view).setText("");
            }



            if (view instanceof ViewGroup && ((ViewGroup)view).getChildCount()>0){
                clearForm((ViewGroup)view);
            }
        }

    }


}
