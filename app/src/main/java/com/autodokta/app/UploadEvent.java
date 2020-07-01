package com.autodokta.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText title,location,description;
    Button ad_upload;
    private Spinner rate;
    private Uri imagepath;
    CircleImageView imageView;
    private String[] rate_arr = {"Free","Paid"};
    private LocalDate localDate;
    String title_str,loc_str,desc_str,spinner_str;
    private int PICK_IMAGE = 100;

    StorageReference storageReference;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);

        title = (EditText)findViewById(R.id.event_name);
        location = (EditText)findViewById(R.id.event_location);
        description = (EditText)findViewById(R.id.event_desc);
        ad_upload = (Button)findViewById(R.id.event_upload);

        imageView = (CircleImageView)findViewById(R.id.event_image);

//        setting click listener for imageview
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

//        Rate spinner
        rate = (Spinner)findViewById(R.id.rate_spinner);
        rate.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,rate_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rate.setAdapter(adapter);

        title_str = title.getText().toString();
        loc_str = location.getText().toString();
        desc_str = description.getText().toString();
        spinner_str = rate.getSelectedItem().toString();

        ad_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmptyField(title_str) || checkEmptyField(loc_str) || checkEmptyField(desc_str)|| checkEmptyField(spinner_str)){
                    Toast.makeText(UploadEvent.this,"Field  required",Toast.LENGTH_SHORT).show();
                }else if (imagepath == null){
                    Toast.makeText(UploadEvent.this,"Select image",Toast.LENGTH_SHORT).show();
                }else{
                    uploadToFireStore();
                }
            }
        });




    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = rate_arr[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
//                display selected image in the ImageView
                imageView.setImageBitmap(bitmap);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
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


    private boolean checkEmptyField(String field){
        if (TextUtils.isEmpty(field)){
            return true;
        }else {
            return false;
        }
    }

    private void uploadToFireStore() {
//        creating firebase storage instance
        storage = FirebaseStorage.getInstance();
//        pointing to the uploaded file
        storageReference = storage.getReference();

        if (imagepath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
            reference.putFile(imagepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = String.valueOf(uri);
                                        saveDataToFirebase(url);

                                    }
                                });
                            }
                            progressDialog.dismiss();
//                            Toast.makeText(UploadActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadEvent.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("uploaded: " + (int) progress + "%");
                        }
                    });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveDataToFirebase(String url){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("events");
        final Map<String, String> upload = new HashMap<String, String>();
        upload.put("title", title_str);
        upload.put("location", loc_str);
        upload.put("description", desc_str);
        upload.put("rate", spinner_str);
        upload.put("image_url", url);
        localDate = LocalDate.now();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
        String date_string = simpleDateFormat.format(localDate);
        upload.put("ad_date",date_string);

        databaseReference.setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder uploaded = new AlertDialog.Builder(UploadEvent.this);
                    uploaded.setMessage("Your item "+title_str+" has been uploaded.");
                    uploaded.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    uploaded.show();
//                    Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadEvent.this, "Upload Failed", Toast.LENGTH_SHORT).show();
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



