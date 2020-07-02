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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadJobs extends AppCompatActivity {

    private EditText jobtitle,responsibilities,description;
    private CircleImageView circleImageView;
    private Spinner regions,job_type,experience;
    Button uploadBtn;

    private ArrayList<String>ghanaRegions;
    private ArrayList<String>type_of_job;
    private ArrayList<String>working_experience;

    String title_str,responsibility_str,desc_str,regions_str,job_type_str,experience_str;

    private Uri imagePath;
    private int PICK_IMAGE = 100;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    LocalDate localDate;
    SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_jobs);

        ghanaRegions =new ArrayList<>();
        ghanaRegions.add("Ashanti");
        ghanaRegions.add("Brong-Ahafo");
        ghanaRegions.add("Central");
        ghanaRegions.add("Eastern");
        ghanaRegions.add("Greater Accra");
        ghanaRegions.add("Northern");
        ghanaRegions.add("Upper East");
        ghanaRegions.add("Upper West");
        ghanaRegions.add("Volta");
        ghanaRegions.add("Western");

        type_of_job = new ArrayList<>();
        type_of_job.add("Full-time");
        type_of_job.add("Part-time");
        type_of_job.add("Temporary");
        type_of_job.add("Contract");
        type_of_job.add("Internship");

        working_experience = new ArrayList<>();
        working_experience.add("less than 1 year");
        working_experience.add("1 year");
        working_experience.add("2 years");
        working_experience.add("3 years");
        working_experience.add("3 years+");

        jobtitle = (EditText)findViewById(R.id.job_name);
        responsibilities = (EditText)findViewById(R.id.job_responsibilities);
        description=(EditText)findViewById(R.id.job_desc);
        circleImageView = (CircleImageView)findViewById(R.id.job_image);
        uploadBtn= (Button)findViewById(R.id.job_upload);

        regions = (Spinner)findViewById(R.id.job_regions);
        ArrayAdapter regionsAdapter = new ArrayAdapter(UploadJobs.this,android.R.layout.simple_spinner_item,ghanaRegions);
        regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regions.setAdapter(regionsAdapter);

        regions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemPosition = ghanaRegions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        job_type = (Spinner)findViewById(R.id.job_type);
        ArrayAdapter jobTypeAdapter = new ArrayAdapter(UploadJobs.this,android.R.layout.simple_spinner_item,type_of_job);
        jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        job_type.setAdapter(jobTypeAdapter);
        job_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemPosition = type_of_job.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        experience = (Spinner)findViewById(R.id.experience);
        ArrayAdapter expAdapter = new ArrayAdapter(UploadJobs.this,android.R.layout.simple_spinner_item,working_experience);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experience.setAdapter(expAdapter);
        experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPosition = working_experience.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title_str = jobtitle.getText().toString().trim();
                    responsibility_str = responsibilities.getText().toString().trim();
                    desc_str = description.getText().toString().trim();
                    regions_str = regions.getSelectedItem().toString();
                    job_type_str = job_type.getSelectedItem().toString();
                    experience_str = experience.getSelectedItem().toString();

                    if (emptyField(title_str)||emptyField(responsibility_str)||emptyField(desc_str)||emptyField(regions_str)
                        || emptyField(job_type_str) || emptyField(experience_str)){
                        Toast.makeText(UploadJobs.this,"Fields Required",Toast.LENGTH_SHORT).show();
                    }else if (imagePath == null){
                        Toast.makeText(UploadJobs.this,"Select Image",Toast.LENGTH_SHORT).show();
                    }else {
                        uploadToFirestore();
                    }



                }
            });





    }

    private boolean emptyField(String field){
        if (TextUtils.isEmpty(field)){
            return true;
        }else{
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
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
//                display selected image in the ImageView
                circleImageView.setImageBitmap(bitmap);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void uploadToFirestore(){
        //        creating firebase storage instance
        storage = FirebaseStorage.getInstance();
//        pointing to the uploaded file
        storageReference = storage.getReference();

        if (imagePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
            reference.putFile(imagePath)
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
                            Toast.makeText(UploadJobs.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        databaseReference = firebaseDatabase.getReference().child("jobs");
        final Map<String, String> upload = new HashMap<String, String>();
        upload.put("title", title_str);
        upload.put("responsibility", responsibility_str);
        upload.put("description", desc_str);
        upload.put("experience", experience_str);
        upload.put("regions", regions_str);
        upload.put("image_url", url);
        localDate = LocalDate.now();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
        String date_string = simpleDateFormat.format(localDate);
        upload.put("ad_date",date_string);

        databaseReference.setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder uploaded = new AlertDialog.Builder(UploadJobs.this);
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
                    Toast.makeText(UploadJobs.this, "Upload Failed", Toast.LENGTH_SHORT).show();
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
