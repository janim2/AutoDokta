package com.autodokta.app.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.Accessories;
import com.autodokta.app.Models.EventsModel;
import com.autodokta.app.Models.JobsModel;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    ArrayList<JobsModel>        itemList;
    Context                     context;
    Accessories                 adapter_accessor;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public JobsAdapter(ArrayList<JobsModel> itemList, Context context){
        this.itemList       =   itemList;
        this.context        =   context;
        adapter_accessor    =   new Accessories(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardView  job_card            =   holder.view.findViewById(R.id.job_card);
        final TextView  job_title           =   holder.view.findViewById(R.id.title);
        final TextView  job_region          =   holder.view.findViewById(R.id.region);

        job_title.setText(itemList.get(position).getTitle());
        job_region.setText(itemList.get(position).getRegions());
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

}
