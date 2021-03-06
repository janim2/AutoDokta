package com.autodokta.app.Adapters;


import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.autodokta.app.ItemDetailsActivity;
import com.autodokta.app.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.ViewHolder>{
    ArrayList<CarParts> itemList;
    Context context;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public PartsAdapter(ArrayList<CarParts> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public PartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(PartsAdapter.ViewHolder holder, final int position) {
        final TextView name = holder.view.findViewById(R.id.name);
        ImageView image = holder.view.findViewById(R.id.image);
        TextView price = holder.view.findViewById(R.id.price);

        //prep work before image is loaded is to load it into the cache
        DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                defaultDisplayImageOptions(theImageOptions).build();
        ImageLoader.getInstance().init(config);
//
        String imagelink = itemList.get(position).getImage_url();
        imageLoader.displayImage(imagelink,image);

        name.setText(itemList.get(position).getTitle());
        price.setText("GHC " + itemList.get(position).getPrice());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
                intent.putExtra("partid",itemList.get(position).getPartId());
                intent.putExtra("theimage",itemList.get(position).getImage_url());
                intent.putExtra("theviews",itemList.get(position).getViews());
                intent.putExtra("thename",itemList.get(position).getTitle());
                intent.putExtra("theprice",itemList.get(position).getPrice());
                intent.putExtra("thedescription",itemList.get(position).getDescription());
                intent.putExtra("thesellersNumber",itemList.get(position).getSeller_number());
                intent.putExtra("therating",itemList.get(position).getProduct_rating());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}