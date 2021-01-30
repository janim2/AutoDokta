package com.autodokta.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Adapters.ChatMessage;
import com.autodokta.app.Adapters.MessageAdapter;
import com.autodokta.app.Adapters.ReviewAdapter;
import com.autodokta.app.Adapters.Reviews;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Chat extends AppCompatActivity {

    FloatingActionButton                fab;

    FirebaseListAdapter<ChatMessage>    adapter;

    String                              message_text, message_time, message_user,
                                        requester_id="", current_chat_id="";

    private ArrayList                   messagesArray = new ArrayList<ChatMessage>();

    private RecyclerView                messages_RecyclerView;

    private RecyclerView.Adapter        messages_mAdapter;

    private TextView                    no_chats;

    private Intent                      chat_intent;

    private FirebaseAuth                mauth;

    private String                      requester_sfirstname, requester_slastname, usertype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mauth                   = FirebaseAuth.getInstance();

        chat_intent             = getIntent();

        getSupportActionBar().setTitle("Custom Request Chat");

        fab                     = (FloatingActionButton)findViewById(R.id.fab);

        messages_RecyclerView   = findViewById(R.id.messages_Recyclerview);
        no_chats                = findViewById(R.id.no_chats);

        requester_id            = chat_intent.getStringExtra("requester_id");

        //getiing the messages variable initialization starts here
        messages_RecyclerView.setHasFixedSize(true);

        messages_mAdapter = new MessageAdapter(getMessagesFromDatabase(),Chat.this);
        messages_RecyclerView.setAdapter(messages_mAdapter);
        //ends here

        fab.setOnClickListener((View.OnClickListener) view -> {
            EditText input = (EditText)findViewById(R.id.input);

            // Read the input field and push a new instance
            // of ChatMessage to the Firebase database
            FirebaseDatabase.getInstance()
                    .getReference("chat")
                    .child(current_chat_id)
                    .push()
                    .setValue(new ChatMessage(input.getText().toString(),new Date().getTime(),
                            mauth.getUid()));

            // Clear the input
            input.setText("");
            getTheMessagesKeys();
        });
    }

    private void getTheMessagesKeys() {
        DatabaseReference messages = FirebaseDatabase.getInstance().getReference("chat")
               .child(current_chat_id);

        messages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey() != null){
                            getTheMessages(child.getKey());
                        }
                    }
                }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chat.this,"Cancelled",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getTheMessages(String key) {
        DatabaseReference getMessages = FirebaseDatabase.getInstance().getReference("chat")
                .child(current_chat_id).child(key);
        Query query = getMessages.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("messageText")) {
                            message_text = child.getValue().toString();
                        }

                        if (child.getKey().equals("messageTime")) {
                            message_time = child.getValue().toString();
                        }

                        if (child.getKey().equals("messageUser")) {
                            message_user = child.getValue().toString();
                        }
                        else {
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();
                        }
                    }

                    try {
                        ChatMessage obj = new ChatMessage(message_text,Long.parseLong(message_time),message_user);
                        messagesArray.add(obj);
                        messages_RecyclerView.setAdapter(messages_mAdapter);
                        messages_mAdapter.notifyDataSetChanged();
                        no_chats.setVisibility(View.GONE);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chat.this, "Cancelled", Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList<ChatMessage> getMessagesFromDatabase(){
        return  messagesArray;
    }

    @Override
    protected void onStart() {
        super.onStart();
        AcquireOrCreateMessageID(requester_id);
//        GetRequesterName(requester_id);
    }

//    private void GetRequesterName(String requester_id) {
//        DatabaseReference requester_name = FirebaseDatabase.getInstance().getReference("users")
//                .child(requester_id);
//
//        requester_name.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for(DataSnapshot child : dataSnapshot.getChildren()){
//
//                        if(child.getKey().equals("firstname")){
//                            requester_sfirstname = child.getValue().toString();
//
//                        }
//                        if(child.getKey().equals("lastname")){
//                            requester_slastname = child.getValue().toString();
//                        }
//                        if(requester_slastname != null && requester_slastname.equals("")){
//                            getSupportActionBar().setTitle("AutoDokta");
//                        }
//                        else{
//                            getSupportActionBar().setTitle(requester_sfirstname + " " + requester_slastname);
//                        }
//
//                    }
//                }else{
////                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(Chat.this,"Cancelled",Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    private void AcquireOrCreateMessageID(String requester_id) {
        DatabaseReference findchatlist_id = FirebaseDatabase.getInstance().getReference("chat_list")
                .child(mauth.getUid());
        Query query = findchatlist_id.orderByChild("chatter").equalTo(requester_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //TODO: get chat id
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        String key = child.getKey();
                        GetChatIDUsingKey(key);
                    }
                }
                else{
                    //TODO: generate a new chat id
                    GeneratenewChatIDForUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetChatIDUsingKey(String key) {
        DatabaseReference chat_id = FirebaseDatabase.getInstance().getReference("chat_list")
                .child(mauth.getUid()).child(key);

        chat_id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("chat_id")){
                            current_chat_id = child.getValue().toString();
                            getTheMessagesKeys();
                        }
                    }
                }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chat.this,"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GeneratenewChatIDForUser() {
        DatabaseReference newchat = FirebaseDatabase.getInstance().getReference("chat_list")
                .child(mauth.getUid());
        current_chat_id = newchat.push().getKey();

        final HashMap<String, Object> save_new_id = new HashMap<>();

        save_new_id.put("chatter",   requester_id);
        save_new_id.put("chat_id",   current_chat_id);
        save_new_id.put("time",      new Date().getTime());

        newchat.push().setValue(save_new_id).addOnSuccessListener(aVoid -> GeneratenewChatIDForRecepient(current_chat_id));
    }

    private void GeneratenewChatIDForRecepient(String the_current_chat_id) {
        DatabaseReference newchat = FirebaseDatabase.getInstance().getReference("chat_list")
                .child(requester_id);
        final HashMap<String, Object> save_new_id = new HashMap<>();

        save_new_id.put("chatter",   mauth.getUid());
        save_new_id.put("chat_id",   the_current_chat_id);
        save_new_id.put("time",      new Date().getTime());

        newchat.push().setValue(save_new_id);
    }

}
