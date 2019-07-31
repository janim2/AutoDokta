package com.autodokta.app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_LONG;


public class ServiceOrderDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button order;
    TextView servicetype, servicedescriptionTextView, thedate, nameTview, locationTview, phone_numberTview;
    Intent getService;

    String servicename, servicedescription;
    DatabaseReference reference;
    FirebaseUser user;
    String dateString,nameString,locationString,phoneString,
    saddress,sfirstname,slastname,snumber;
    ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_details);

        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle("Order Service");
        getService = getIntent();

        order = (Button) findViewById(R.id.order);
        servicetype = (TextView) findViewById(R.id.servicetype);
        servicedescriptionTextView = (TextView) findViewById(R.id.service_description);
        thedate = (TextView) findViewById(R.id.date_select);
        nameTview = (TextView) findViewById(R.id.nameTview);
        locationTview = (TextView) findViewById(R.id.locationTview);
        phone_numberTview = (TextView) findViewById(R.id.phonenumberTview);
        loading = (ProgressBar) findViewById(R.id.loading);

        servicename = getService.getStringExtra("servicetype");
        servicedescription = getService.getStringExtra("serviceDescription");

        servicetype.setText(servicename);
        servicedescriptionTextView.setText(servicedescription);

        getUserinfo();

        thedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] the_date = {thedate};
                showDatePicker(null,the_date);
            }
        });

        if(servicename.contains("Insurance")){
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateString = thedate.getText().toString();
                    nameString = nameTview.getText().toString();
                    locationString = locationTview.getText().toString();
                    phoneString = phone_numberTview.getText().toString();
                    addToDatabase(dateString,nameString,locationString,phoneString);
                }
            });
        }



    }

    private void addToDatabase(String dateString, String nameString, String locationString, String phoneString) {
        try {
            DatabaseReference addinsuranceService = FirebaseDatabase.getInstance().getReference("Insurance");
            String id = addinsuranceService.getKey();
            addinsuranceService.child(user.getUid()).child(id).child("name").setValue(nameString);
            addinsuranceService.child(user.getUid()).child(id).child("date").setValue(dateString);
            addinsuranceService.child(user.getUid()).child(id).child("location").setValue(locationString);
            addinsuranceService.child(user.getUid()).child(id).child("phone_number").setValue(phoneString);
            sendOrderEmail();
        }catch (NullPointerException e){

        }
       }

    private void sendOrderEmail() {
        new Sending_mail("https://knust-martial-arts.000webhostapp.com/AutoDoktaService.php",
                dateString,locationString,nameString,phoneString).execute();
    }

    private void getUserinfo() {
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("address")) {
                            saddress = child.getValue().toString();
                        }
                        if (child.getKey().equals("firstname")) {
                            sfirstname = child.getValue().toString();
                        }
                        if (child.getKey().equals("lastname")) {
                            slastname = child.getValue().toString();
                        }
                        if (child.getKey().equals("number")) {
                            snumber = child.getValue().toString();
                        }

                        nameTview.setText(sfirstname + " " + slastname);
                        locationTview.setText(saddress);
                        phone_numberTview.setText(snumber);

                        }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
}
    @SuppressLint("StaticFieldLeak")
    class Sending_mail extends AsyncTask<Void, Void, String> {

        String url_location,date, address, name, number;

        public Sending_mail(String url_location,String date, String address, String name,String number) {
            this.url_location = url_location;
            this.date = date;
            this.address = address;
            this.name = name;
            this.number = number;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
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

                String data = URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                        URLEncoder.encode("number", "UTF-8") + "=" + URLEncoder.encode(number, "UTF-8");

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
            loading.setVisibility(View.GONE);
            if (s.equals("Order Sent")){
                Toast.makeText(ServiceOrderDetails.this, s, LENGTH_LONG).show();
            }
            Toast.makeText(ServiceOrderDetails.this, s, LENGTH_LONG).show();

        }

    }

    //Method to call DatePicker
    EditText[] editText;
    TextView[] textView;
    public void showDatePicker(EditText[] editText, TextView[] textView){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        this.editText = editText;
        this.textView = textView;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ServiceOrderDetails.this, this, year, month, day);
        datePickerDialog.show();
    }

    int yr, mt, dy, hr, min;
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yr = year;
        mt = month + 1;
        dy = dayOfMonth;
        if(editText != null){
            for (int i = 0; i < editText.length; i++){
                editText[i].setText(yr + "-" + mt + "-" + dy);
            }
        }
        if(textView != null){
            for (int i = 0; i < textView.length; i++){
                textView[i].setText(yr + "-" + mt + "-" + dy);
            }
        }
    }


}

