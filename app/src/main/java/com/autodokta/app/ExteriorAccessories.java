package com.autodokta.app;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExteriorAccessories extends Fragment {

    ArrayList resultExteriorParts = new ArrayList<CarParts>();
    RecyclerView PostRecyclerView;
    RecyclerView.Adapter mPostAdapter;
    RecyclerView.LayoutManager mPostLayoutManager;
    String imageurl, name, description, price;
    ProgressBar loading;


    public ExteriorAccessories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View exteriorParts =  inflater.inflate(R.layout.fragment_exterior_accessories, container, false);

        loading  = (ProgressBar)exteriorParts.findViewById(R.id.loading);

        PostRecyclerView = (RecyclerView) exteriorParts.findViewById(R.id.myRecyclerView);
        PostRecyclerView.setHasFixedSize(true);

        mPostLayoutManager = new LinearLayoutManager(getActivity());
        PostRecyclerView.setLayoutManager(mPostLayoutManager);

        getExteriorPartIds();

        mPostAdapter = new PartsAdapter(getExteriorParts(),getActivity());
        PostRecyclerView.setAdapter(mPostAdapter);
        return  exteriorParts;
    }

    private void getExteriorPartIds() {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("carparts").child("ExteriorParts");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        FetchExteriorParts(child.getKey());
                    }
                }else{
                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void FetchExteriorParts(final String key) {
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("carparts").child("ExteriorParts").child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            price = child.getValue().toString();
                        }


                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }

                    String exteriorpartid = key;

                    CarParts obj = new CarParts(exteriorpartid,imageurl,name,description,price);
                    resultExteriorParts.add(obj);
                    PostRecyclerView.setAdapter(mPostAdapter);
                    mPostAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    public ArrayList<CarParts> getExteriorParts(){
        return  resultExteriorParts;
    }

}
