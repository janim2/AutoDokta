package com.autodokta.app;


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

public class Chat extends AppCompatActivity {

    FloatingActionButton fab;
    FirebaseListAdapter<ChatMessage> adapter;
    String message_text, message_time, message_user;

    private ArrayList messagesArray = new ArrayList<ChatMessage>();
    private RecyclerView messages_RecyclerView;
    private RecyclerView.Adapter messages_mAdapter;
    private TextView no_chats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setTitle("Auto Dokta Chat");
        fab = (FloatingActionButton)findViewById(R.id.fab);
        messages_RecyclerView = findViewById(R.id.messages_Recyclerview);
        no_chats = findViewById(R.id.no_chats);

        //getiing the messages variable initialization starts here
        getTheMessagesKeys();
        messages_RecyclerView.setHasFixedSize(true);

        messages_mAdapter = new MessageAdapter(getMessagesFromDatabase(),Chat.this);
        messages_RecyclerView.setAdapter(messages_mAdapter);
        //ends here

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference("chat")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),new Date().getTime(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid()));

                // Clear the input
                input.setText("");
                getTheMessagesKeys();
            }
        });
    }

    public void getTheMessages(String key) {
        DatabaseReference getMessages = FirebaseDatabase.getInstance().getReference("chat")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key);
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

                    ChatMessage obj = new ChatMessage(message_text,Long.valueOf(message_time),message_user);
                    messagesArray.add(obj);
                    messages_RecyclerView.setAdapter(messages_mAdapter);
                    messages_mAdapter.notifyDataSetChanged();
                    no_chats.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chat.this, "Cancelled", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getTheMessagesKeys() {
        DatabaseReference messages = FirebaseDatabase.getInstance().getReference("chat")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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

    public ArrayList<ChatMessage> getMessagesFromDatabase(){
        return  messagesArray;
    }

}
