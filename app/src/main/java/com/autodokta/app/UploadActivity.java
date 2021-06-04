package com.autodokta.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText            product_name,   product_price,
                                seller,         product_desc,location;

    private CircleImageView     imageView;

    private Button              upload_product, chooseImage;

    private String              name_str,       price_str,
                                seller_str,     desc_str, image_str,productTypeStr,locatStr,regionStr;

    private FirebaseStorage     storage;

    private StorageReference    storageReference;

    private DatabaseReference   databaseReference;

    private FirebaseDatabase    firebaseDatabase;

    private Uri                 imagepath;

//    private int                 PICK_IMAGE_     = 100;

    private Spinner productType,region;

    private static final int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList();
    private int upload_count = 0;
    ArrayList urlStrings;
    TextView alert;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        getSupportActionBar().setTitle("Upload Item");

        //Calling various view using their respective id's
        product_name    =   findViewById(R.id.product_name);
        product_price   =   findViewById(R.id.product_price);
        seller          =   findViewById(R.id.seller_number);
        product_desc    =   findViewById(R.id.product_desc);
        location = (EditText)findViewById(R.id.seller_location);
        productType =  (Spinner)findViewById(R.id.product_type_spinner);
        productType.setOnItemSelectedListener(this);

        //        Create and arrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.product_type,
                android.R.layout.simple_spinner_item);
//        specifies the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        apply the adapter to the spinner
        productType.setAdapter(adapter);

//        region spinner
        region = (Spinner)findViewById(R.id.seller_region);
        region.setOnItemSelectedListener(this);

        //        Create and arrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this,R.array.region_spinner,
                android.R.layout.simple_spinner_item);
//        specifies the layout to use when the list of choices appears
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        apply the adapter to the spinner
        region.setAdapter(regionAdapter);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Images Uploading. Please wait..");


        chooseImage      =   findViewById(R.id.choose_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultipleImages();
            }
        });

        upload_product  =   findViewById(R.id.ad_upload);

        //setting clickListener for the product button
        upload_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first check if all fields are filled
                //convert various fields to string
                name_str    = product_name.getText().toString();
                price_str   = product_price.getText().toString();
                seller_str  = seller.getText().toString();
                desc_str    = product_desc.getText().toString();
                locatStr = location.getText().toString();

                //display error message for empty fields
                if (checkEmptyFiled(name_str) || checkEmptyFiled(price_str) ||
                        checkEmptyFiled(seller_str) || checkEmptyFiled(desc_str) || checkEmptyFiled(locatStr)) {
                    Toast.makeText(getApplicationContext(), "Fields required", Toast.LENGTH_SHORT).show();
                }
                else if(imagepath == null){
                    Toast.makeText(UploadActivity.this, "Please select image", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadImageToFireStore();
                }
            }

        });
    }

    //    method to check for empty fields
    public boolean checkEmptyFiled(String field) {
        if (TextUtils.isEmpty(field)) {
            return true;
        } else {
            return false;
        }
    }

    private void selectMultipleImages() {
        //creating a new intent instance
//        Intent intent = new Intent();
//        //get an image content
//        intent.setType("image/*");
//        //image choser dialog that allows user to browse the device gallery to select the image
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        //receiving the selected image
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imagepath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
////                display selected image in the ImageView
//                imageView.setImageBitmap(bitmap);
//
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }


        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {


                if (data.getClipData() != null) {

                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    while (currentImageSlect < countClipData) {

                        imagepath = data.getClipData().getItemAt(currentImageSlect).getUri();
                        ImageList.add(imagepath);
                        currentImageSlect = currentImageSlect + 1;
                    }

                    alert.setVisibility(View.VISIBLE);
                    alert.setText("You have selected" + ImageList.size() + "Images");
                    chooseImage.setVisibility(View.GONE);



                } else {
                    Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        retrieving selected item


        Spinner spinner1 = (Spinner)parent;
        Spinner spinner2 = (Spinner)parent;

        if (spinner1.getId() == R.id.seller_region){
            regionStr = parent.getItemAtPosition(position).toString();
        }

        if (spinner2.getId() == R.id.product_type_spinner){
            productTypeStr = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(UploadActivity.this,"select field", Toast.LENGTH_LONG).show();
    }



    private void uploadImageToFireStore() {
//        creating firebase storage instance
        storage = FirebaseStorage.getInstance();
//        pointing to the uploaded file
        storageReference = storage.getReference();

//        progressDialog.show();
//        alert.setText("If Loading Takes too long please press the button again");

        urlStrings = new ArrayList();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        final StorageReference imageFolder = storageReference.child("ImagesFolder");
        for (upload_count =0; upload_count<ImageList.size(); upload_count++){
            Uri individualImage = ImageList.get(upload_count);
            final StorageReference imageName = imageFolder.child("Image" + individualImage.getLastPathSegment());
            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata().getReference() != null){
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveDataToFirebase(String.valueOf(uri));


                            }
                        });
                    }
                }
            });


        }

//        if (imagepath != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//
//            final StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
//            reference.putFile(imagepath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            if (taskSnapshot.getMetadata().getReference() != null) {
//                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        String url = String.valueOf(uri);
//                                        saveDataToFirebase(url);
//
//                                    }
//                                });
//                            }
//                            progressDialog.dismiss();
////                            Toast.makeText(UploadActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(UploadActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            progressDialog.setMessage("uploaded: " + (int) progress + "%");
//                        }
//                    });
//        }


    }

    private void saveDataToFirebase(String url) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("allParts");
        final Map<String, String> upload = new HashMap<String, String>();
        upload.put("title",         name_str);
        upload.put("price",         price_str);
        upload.put("seller_number", seller_str);
        upload.put("description",   desc_str);
        upload.put("location",   locatStr);
        upload.put("image_url",     url);
        upload.put("uploader_id",   FirebaseAuth.getInstance().getUid());
        upload.put("views",      "0");
        upload.put("rating",     "0");
        upload.put("product_type",     productTypeStr);
        upload.put("region",    regionStr);
        databaseReference.push().setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder uploaded = new AlertDialog.Builder(UploadActivity.this);
                    uploaded.setMessage("Your item "+name_str+" has been uploaded.");
                    uploaded.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    uploaded.show();
//                    Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
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