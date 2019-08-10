package com.autodokta.app.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.ItemDetailsActivity;
import com.autodokta.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    ArrayList<Reviews> itemList;
    Context context;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ReviewAdapter(ArrayList<Reviews> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, final int position) {
        final TextView review_titled = holder.view.findViewById(R.id.rate_title);
        final TextView review_message = holder.view.findViewById(R.id.rate_message);
        ImageView star_1 = holder.view.findViewById(R.id.star_one);
        ImageView star_2 = holder.view.findViewById(R.id.star_two);
        ImageView star_3 = holder.view.findViewById(R.id.star_three);
        ImageView star_4 = holder.view.findViewById(R.id.star_four);
        ImageView star_5 = holder.view.findViewById(R.id.star_five);
        TextView bywho = holder.view.findViewById(R.id.rate_byWho);

        String therate = itemList.get(position).getReview_rate();
        if(therate.equals("5")){
            star_5.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_4.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_1.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            review_titled.setText("It's perfect");
        }
        else if(therate.equals("4")){
            star_5.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_4.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_1.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            review_titled.setText("I like it");

        }
        else if(therate.equals("3")){
            star_5.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_4.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_1.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            review_titled.setText("It's OK");

        }
        else if(therate.equals("2")){
            star_5.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_4.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            star_1.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            review_titled.setText("I don't like it");

        }
        else if(therate.equals("1")){
            star_5.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_4.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_3.setImageDrawable(context.getResources().getDrawable(R.drawable.bare_star));
            star_1.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
            review_titled.setText("i hate");

        }
        review_message.setText(itemList.get(position).getMessage());
        bywho.setText("by " + itemList.get(position).getName());

//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
//                intent.putExtra("partid",itemList.get(position).getPartId());
//                intent.putExtra("theimage",itemList.get(position).getImage());
//                intent.putExtra("thename",itemList.get(position).getname());
//                intent.putExtra("theprice",itemList.get(position).getPrice());
//                intent.putExtra("thedescription",itemList.get(position).getDescription());
//                intent.putExtra("thesellersNumber",itemList.get(position).getsellersNumber());
//                intent.putExtra("therating",itemList.get(position).getProduct_rating());
//                v.getContext().startActivity(intent);
//            }
//        });


    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}