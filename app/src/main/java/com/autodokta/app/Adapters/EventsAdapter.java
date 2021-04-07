package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.Accessories;
import com.autodokta.app.Models.EventsModel;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    ArrayList<EventsModel>      itemList;
    Context                     context;
    Accessories                 adapter_accessor;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public EventsAdapter(ArrayList<EventsModel> itemList, Context context){
        this.itemList       =   itemList;
        this.context        =   context;
        adapter_accessor    =   new Accessories(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardView  event_card            =   holder.view.findViewById(R.id.event_card);
        final ImageView event_poster          =   holder.view.findViewById(R.id.event_poster);
        final TextView  event_name            =   holder.view.findViewById(R.id.name);
        final TextView  event_rate            =   holder.view.findViewById(R.id.rate);


        event_name.setText(itemList.get(position).getTitle());
        event_rate.setText(itemList.get(position).getRate());

        try {
            Glide.with(context).load(itemList.get(position).getImage()).thumbnail(0.3f).into(event_poster);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

}
