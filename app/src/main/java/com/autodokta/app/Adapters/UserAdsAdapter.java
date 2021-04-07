package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.CategorisedDetails;
import com.autodokta.app.EditAd;
import com.autodokta.app.Models.UserAds;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdsAdapter extends RecyclerView.Adapter<UserAdsAdapter.ViewHolder> {
    private static final String TAG = UserAdsAdapter.class.getSimpleName();
    DatabaseReference databaseReference;
     Context context;
    private List<UserAds> userAds;

    private OnCallBack onCallBack;

    public UserAdsAdapter(Context context, List<UserAds> userAds) {
        this.context = context;
        this.userAds = userAds;
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public UserAdsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_ads_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdsAdapter.ViewHolder viewHolder, int i) {

        UserAds ads = userAds.get(i);
        viewHolder.title.setText(ads.getTitle());
        viewHolder.price.setText(ads.getPrice());
        viewHolder.location.setText(ads.getLocation());



        //load image using glide
        if (ads.getImage_url() != null && !ads.getImage_url().isEmpty()){
            Glide.with(context).load(ads.getImage_url()).into(viewHolder.img);
        }

//        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCallBack.onButtonEditClick(userAds.get(i));
////
////                Intent intent = new Intent(v.getContext(),EditAd.class);
////                Bundle bundle = new Bundle();
////                bundle.putString("key",userAds.get(i).getId());
////                bundle.putString("title",userAds.get(i).getTitle());
////                bundle.putString("price",userAds.get(i).getPrice());
////                bundle.putString("location",userAds.get(i).getLocation());
////                intent.putExtras(bundle);
////                v.getContext().startActivity(intent);
//
//
//            }
//        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonDeleteClick(userAds.get(i));

            }
        });

    }

    @Override
    public int getItemCount() {
        return userAds.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,price,location;
        private ImageView img;
        private Button edit,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.user_Title);
            price = (TextView)itemView.findViewById(R.id.user_price);
            location = (TextView)itemView.findViewById(R.id.user_location);
            img = (ImageView)itemView.findViewById(R.id.user_img);
            delete = (Button)itemView.findViewById(R.id.ad_delete);
//            edit = (Button)itemView.findViewById(R.id.ad_edit);


            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(context, CategorisedDetails.class);
                    intent.putExtra("id",userAds.get(itemPosition).getId());
                    intent.putExtra("title",userAds.get(itemPosition).getTitle());
                    intent.putExtra("price",userAds.get(itemPosition).getPrice());
                    intent.putExtra("location", userAds.get(itemPosition).getLocation());
                    intent.putExtra("img", userAds.get(itemPosition).getImage_url());
                    context.startActivity(intent);
                }

            });


        }



    }

    public interface OnCallBack{
        void onButtonDeleteClick(UserAds userAds);
        void  onButtonEditClick(UserAds userAds);
    }


}























































