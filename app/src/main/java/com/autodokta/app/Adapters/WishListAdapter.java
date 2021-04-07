package com.autodokta.app.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autodokta.app.ItemDetailsActivity;
import com.autodokta.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder>{
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

    public WishListAdapter(ArrayList<CarParts> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(WishListAdapter.ViewHolder holder, final int position) {
        ImageView image = holder.view.findViewById(R.id.item_image);
        TextView price = holder.view.findViewById(R.id.item_prize);
        TextView name = holder.view.findViewById(R.id.item_name);
        LinearLayout wish_layout = holder.view.findViewById(R.id.wish_list_layout);

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
        price.setText(itemList.get(position).getPrice());

        wish_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
                intent.putExtra("partid",itemList.get(position).getPartId());
                intent.putExtra("theimage",itemList.get(position).getImage_url());
                intent.putExtra("thename",itemList.get(position).getTitle());
                intent.putExtra("theprice",itemList.get(position).getPrice());
                intent.putExtra("thedescription",itemList.get(position).getDescription());
                intent.putExtra("thesellersNumber",itemList.get(position).getSeller_number());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}