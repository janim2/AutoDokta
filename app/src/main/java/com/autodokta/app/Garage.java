package com.autodokta.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.autodokta.app.Adapters.TodoAdapter;
import com.autodokta.app.Adapters.TodoItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Garage extends AppCompatActivity {

    ArrayList todo_items = new ArrayList<TodoItems>();
    RecyclerView todo_items_recyclerView;
    RecyclerView.Adapter todoAdapter;
    RecyclerView.LayoutManager todoLayoutManager;
    String todo_title;
    ProgressBar loading;
    String todo_name,todo_message;
    FirebaseUser user;

    FloatingActionButton add_a_todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        loading = (ProgressBar) findViewById(R.id.loading);
        add_a_todo = (FloatingActionButton) findViewById(R.id.add_a_todo);

        user = FirebaseAuth.getInstance().getCurrentUser();

        todo_items_recyclerView = (RecyclerView) findViewById(R.id.todolist_items);
        todo_items_recyclerView.setHasFixedSize(true);

        todoLayoutManager = new LinearLayoutManager(Garage.this);
        todo_items_recyclerView.setLayoutManager(todoLayoutManager);

        gettodoListIDs();

        todoAdapter = new TodoAdapter(getTodo(),Garage.this);
        todo_items_recyclerView.setAdapter(todoAdapter);

        add_a_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Garage.this, Add_a_TodoActivity.class));
            }
        });
    }

    private void gettodoListIDs() {
        try {
            DatabaseReference partdatabase = FirebaseDatabase.getInstance().getReference().child("todoList").child(user.getUid());

            partdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            getTodoItemsNow(child.getKey());
                        }
                    }else{
                        Toast.makeText(Garage.this,"Cannot get ID",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Garage.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch(NullPointerException e){

        }

    }

    private void getTodoItemsNow(final String key) {

        try {
            DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("todoList").child(user.getUid()).child(key);
            postData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getKey().equals("name")){
                                todo_name = child.getValue().toString();
                            }

                            if(child.getKey().equals("message")){
                                todo_message = child.getValue().toString();
                            }
                        }

                        TodoItems obj = new TodoItems(key,todo_name,todo_message);
                        todo_items.add(obj);
                        todo_items_recyclerView.setAdapter(todoAdapter);
                        todoAdapter.notifyDataSetChanged();
                        loading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Garage.this,"Cancelled",Toast.LENGTH_LONG).show();

                }
            });
        }catch (NullPointerException e){

        }

    }

    public ArrayList<TodoItems> getTodo(){
        return  todo_items;
    }

}
