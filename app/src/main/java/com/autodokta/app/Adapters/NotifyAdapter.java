package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Cart;
import com.autodokta.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder>{
    ArrayList<Notify> itemList;
    Context context;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public NotifyAdapter(ArrayList<Notify> itemList, Context context){
        this.itemList  = itemList;
        this.context  = context;
    }

    @Override
    public NotifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(NotifyAdapter.ViewHolder holder, final int position) {
        final TextView title = holder.view.findViewById(R.id.notify_title);
        ImageView image = holder.view.findViewById(R.id.notify_image);
        TextView time = holder.view.findViewById(R.id.notify_time);
        TextView message = holder.view.findViewById(R.id.notify_message);

        if(itemList.get(position).getImageType() != null){
            String which_image = itemList.get(position).getImageType();

            if(which_image.equals("WN")){//stands for welcome Notification
                image.setImageDrawable(holder.view.getResources().getDrawable(R.mipmap.ic_launcher_round));
            }
            else if(which_image.equals("ACN")){//stands for added contact us notification
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.ic_book_black_24dp));
            }
            else if(which_image.equals("SSN")){//stands for suggested service notification

            }
            else if(which_image.equals("AIN")){//stands for Added insurance notification

            }
            else if(which_image.equals("AGIN")){//stands for Added Garage Item notification

            }
            else if(which_image.equals("DGIN")){//stands for Delete Garage Item notification

            }
            else if(which_image.equals("PUNN")){//stands for Profile Update notification

            }
            else if(which_image.equals("ACaN")){//stands for Add to Cart notification

            }
            else if(which_image.equals("DCaN")){//stands for Delete from Cart notification

            }
            else if(which_image.equals("AWN")){//stands for Add WishList notification
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.correct));
            }
            else if(which_image.equals("RWN")){//stands for Remove WishList notification
                image.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.delete));
            }
            else if(which_image.equals("RAN")){//stands for Review Added notification

            }
            else{
                image.setImageDrawable(holder.view.getResources().getDrawable(R.mipmap.ic_launcher_round));
            }

            title.setText(itemList.get(position).getTitle());
            message.setText(itemList.get(position).getMessage());
            time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",itemList.get(position).getTime()));
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
