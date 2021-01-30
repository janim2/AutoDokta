package com.autodokta.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.autodokta.app.helpers.Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class CustomRequestActivity extends AppCompatActivity {

    private EditText                    name,   phone_number, item_name, item_description, budget;

    private String                      sname, sphone_number, sitem_name, sitem_description, sbudget;

    private ProgressBar                 loading;

    private Button                      custom_request_button;

    private FirebaseAuth                mauth;

    private Functions                   functions;

    private Accessories                 custom_request_accessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_request);

        getSupportActionBar().setTitle("Custom Request");

        custom_request_accessor = new Accessories(CustomRequestActivity.this);

        mauth                   = FirebaseAuth.getInstance();

        functions               = new Functions(CustomRequestActivity.this);

        name                    = findViewById(R.id.person_name);
        phone_number            = findViewById(R.id.phone_number);
        item_name               = findViewById(R.id.item_name);
        item_description        = findViewById(R.id.item_description);
        budget                  = findViewById(R.id.budget);

        loading                 = findViewById(R.id.progressBar);
        custom_request_button   = findViewById(R.id.custom_request_button);

        FetchItemsFromDraft();

        custom_request_button.setOnClickListener(view -> {
            sname               =   name.getText().toString().trim();
            sphone_number       =   phone_number.getText().toString().trim();
            sitem_name          =   item_name.getText().toString().trim();
            sitem_description   =   item_description.getText().toString().trim();
            sbudget             =   budget.getText().toString().trim();

            if(sname.equals("")){
                functions.ShowToast("Name required");
            }
            else if(sphone_number.equals("")){
                functions.ShowToast("Phone number required");
            }
            else if(sitem_name.equals("")){
                functions.ShowToast("Item name required");
            }
            else if(sbudget.equals("")){
                functions.ShowToast("Budget required");
            }
            else{
                if(mauth.getCurrentUser()!=null) {
                    if (functions.isNetworkAvailable()){
                        SaveCustomRequest(sname, sphone_number, sitem_name, sitem_description, sbudget);
                    }
                    else{
                        functions.ShowToast("No internet connection");
                    }
                }
                else{
                    NotLoggedInAlertDialogue(CustomRequestActivity.this);
                    SaveAsdraft(sname, sphone_number, sitem_name, sitem_description, sbudget);
                    functions.ShowToast("Saved as draft");
                }
            }
        });
    }

    private void SaveCustomRequest(String name, String phone_number, String item_name, String description, String budget) {
        loading.setVisibility(View.VISIBLE);
        DatabaseReference comp_reference = FirebaseDatabase.getInstance().getReference("requests");
        String request_id = FirebaseDatabase.getInstance().getReference("requests").push().getKey();
        String user_id = "";
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            user_id = mauth.getUid();
        }
        comp_reference.child(request_id).child("name").setValue(name);
        comp_reference.child(request_id).child("phone_number").setValue(phone_number);
        comp_reference.child(request_id).child("item_name").setValue(item_name);
        comp_reference.child(request_id).child("description").setValue(description);
        comp_reference.child(request_id).child("budget").setValue(budget);
        comp_reference.child(request_id).child("user_id").setValue(user_id).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                functions.SendNotifcationsToAllUsers("Custom Request", "A buyer has requested for " + item_name + ". If you have access to the said product, feel free to contact buyer on " + phone_number, new Date().getTime(), "CR", request_id);
                RequestSubmitted(CustomRequestActivity.this);
                ClearDraftRecords();
            }
        });
    }

    private void SaveAsdraft(String name, String phone_number, String item_name, String description, String budget) {
        custom_request_accessor.put("requester_name",             name);
        custom_request_accessor.put("requester_phone_number",     phone_number);
        custom_request_accessor.put("requester_item_name",        item_name);
        custom_request_accessor.put("requester_description",      description);
        custom_request_accessor.put("requester_budget",           budget);
    }

    private void FetchItemsFromDraft() {
        if(mauth.getCurrentUser()!=null) {
            sname = custom_request_accessor.getString("requester_name");
            if(!sname.equals("")){
                functions.ShowToast("Restoring from draft");
                name.setText(sname);
                phone_number.setText(custom_request_accessor.getString("requester_phone_number"));
                item_name.setText(custom_request_accessor.getString("requester_item_name"));
                item_description.setText(custom_request_accessor.getString("requester_description"));
                budget.setText(custom_request_accessor.getString("requester_budget"));
            }
        }

    }

    private void ClearDraftRecords() {
            custom_request_accessor.put("requester_name", "");
            custom_request_accessor.put("requester_phone_number", "");;
            custom_request_accessor.put("requester_item_name", "");
            custom_request_accessor.put("requester_description", "");;
            custom_request_accessor.put("requester_budget", "");
    }

    private void NotLoggedInAlertDialogue(Context context){
        AlertDialog.Builder login = new AlertDialog.Builder(context);
        login.setTitle("WARNING");
        login.setMessage("Login to submit your request");
        login.setNegativeButton("Login", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        login.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
        login.show();
    }

    private void RequestSubmitted(Context context){
        AlertDialog.Builder submitted = new AlertDialog.Builder(context);
        submitted.setTitle("REQUEST SUBMITTED");
        submitted.setMessage(sitem_name + " has been successfully submitted. You will be contacted by a seller of the product. Stay tuned");
        submitted.setNegativeButton("OK", (dialog, which) -> {
           finish();
        });
        submitted.show();
    }
}