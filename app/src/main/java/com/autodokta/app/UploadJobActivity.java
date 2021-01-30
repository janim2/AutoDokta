package com.autodokta.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class UploadJobActivity extends AppCompatActivity {


    private EditText        jobtitle,       responsibilities,
                            description;

    private Spinner         regions_spinner,type_spinner,
                            experience_spinner;

    private Button          uploadBtn;

    private String[]        ghanaRegions = {"Select region",
                                            "Ashanti",          "Brong-Ahafo",
                                            "Central",          "Eastern",
                                            "Greater Accra",    "Northern",
                                            "Upper East",       "Upper West",
                                            "Volta",            "Western"
           };

    private String[]        type_of_job = {  "Select job type",
                                             "Full-time",       "Part-time",
                                             "Temporary",       "Contract",
                                             "Internship"
                            };

    private String[]        working_experience = {"Select working experience",
                                                "less than 1 year",     "1 year",
                                                "2 years",              "3 years",
                                                "3 years+"
                            };

    private String          title_str,      responsibility_str,
                            desc_str,       regions_str,
                            job_type_str,   experience_str;

    private FirebaseDatabase    upload_job;

    private DatabaseReference   uploadjob_reference;

    private LocalDate           localDate;

    private SimpleDateFormat    simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_job);

        getSupportActionBar().setTitle("Upload Job");

        jobtitle            =   findViewById(R.id.job_name);
        regions_spinner     =   findViewById(R.id.job_regions);
        type_spinner        =   findViewById(R.id.job_type);
        experience_spinner  =   findViewById(R.id.experience);
        responsibilities    =   findViewById(R.id.job_responsibilities);
        description         =   findViewById(R.id.job_desc);
        uploadBtn           =   findViewById(R.id.job_upload);

        //regions spinner
        regions_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regions_str = ghanaRegions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                regions_str = "Greater Accra";
            }
        });
        ArrayAdapter<String> regions_ = new ArrayAdapter<String>(this,R.layout.spinner_layout,ghanaRegions);
        regions_spinner.setAdapter(regions_);

        //experience spinner
        experience_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                experience_str = working_experience[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                experience_str = "1 year";
            }
        });
        ArrayAdapter<String> experience_ = new ArrayAdapter<String>(this,R.layout.spinner_layout,working_experience);
        experience_spinner.setAdapter(experience_);

        //job type spinner
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job_type_str = type_of_job[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                job_type_str = "Temporary";
            }
        });
        ArrayAdapter<String> job_type = new ArrayAdapter<String>(this,R.layout.spinner_layout,type_of_job);
        type_spinner.setAdapter(job_type);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_str               =   jobtitle.getText().toString().trim();
                responsibility_str      =   responsibilities.getText().toString().trim();
                desc_str                =   description.getText().toString().trim();

                if (emptyField(title_str)||emptyField(responsibility_str)||emptyField(desc_str)){
                    Toast.makeText(UploadJobActivity.this,"Fields Required",Toast.LENGTH_SHORT).show();
                }
                else if (regions_str.equals("Select region")){
                    Toast.makeText(UploadJobActivity.this,"Select region",Toast.LENGTH_SHORT).show();
                }
                else if (experience_str.equals("Select working experience")){
                    Toast.makeText(UploadJobActivity.this,"Select experience",Toast.LENGTH_SHORT).show();
                }
                else if (job_type_str.equals("Select job type")){
                    Toast.makeText(UploadJobActivity.this,"Select job type",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isNetworkAvailable()){
                        saveDataToFirebase(title_str, responsibility_str,desc_str, regions_str,experience_str,job_type_str);
                    }
                    else {
                        Toast.makeText(
                                UploadJobActivity.this,"No internet connection", Toast.LENGTH_LONG
                        ).show();
                    }
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

    private void saveDataToFirebase(String title, String responsibility, String description, String regions,
                                    String experience, String job_type_str){
        upload_job          = FirebaseDatabase.getInstance();
        uploadjob_reference = upload_job.getReference().child("jobs");
        final Map<String, String> upload = new HashMap<String, String>();
        upload.put("title",             title);
        upload.put("responsibility",    responsibility);
        upload.put("description",       description);
        upload.put("regions",           regions);
        upload.put("experience",        experience);
        upload.put("job_type",          job_type_str);
//        localDate = LocalDate.now();
//        simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
//        String date_string = simpleDateFormat.format(localDate);
//        upload.put("ad_date",date_string);

        uploadjob_reference.push().setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder uploaded = new AlertDialog.Builder(UploadJobActivity.this);
                    uploaded.setMessage("Job "+title_str+" has been uploaded.");
                    uploaded.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    uploaded.show();
//                    Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadJobActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
