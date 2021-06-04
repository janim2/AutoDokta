package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autodokta.app.CategorisedDetails;
import com.autodokta.app.Models.CategorisedServicesModel;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CategorisedServicesAdapter extends RecyclerView.Adapter<CategorisedServicesAdapter.ViewHolder> {

//    context to inflate layout
    Context context;

//    storing all  product in  a list
    private List<CategorisedServicesModel> modelList;
    ImageLoader imageLoader = ImageLoader.getInstance();

//    contructor


    public CategorisedServicesAdapter(Context context, List<CategorisedServicesModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflating and returning our view holder
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorisedservice_layout,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            //getting the product of the specified position
        CategorisedServicesModel servicesModel = modelList.get(i);

        //binding the data with the viewholder views
        viewHolder.title.setText(servicesModel.getTitle());
        viewHolder.shortDesc.setText(servicesModel.getShort_description());
        viewHolder.price.setText("GHC " + servicesModel.getPrice());
        viewHolder.location.setText(servicesModel.getLocation());

//        Load images into the recyclerview using Glide
        if (servicesModel.getImage_url() != null && !servicesModel.getImage_url().isEmpty()){
            Glide.with(context).load(servicesModel.getImage_url()).into(viewHolder.serviceImage);
        }



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,shortDesc,price,location;
        ImageView serviceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (itemView).findViewById(R.id.catTitle);
            shortDesc = (itemView).findViewById(R.id.textViewShortDesc);
            price = (itemView).findViewById(R.id.catPrice);
            location = (itemView).findViewById(R.id.catLocation);
            serviceImage = (itemView).findViewById(R.id.thumbnail);

            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(context, CategorisedDetails.class);
                    intent.putExtra("id",modelList.get(itemPosition).getId());
                    intent.putExtra("title",modelList.get(itemPosition).getTitle());
                    intent.putExtra("price",modelList.get(itemPosition).getPrice());
                    intent.putExtra("short_description", modelList.get(itemPosition).getShort_description());
                    intent.putExtra("img", modelList.get(itemPosition).getImage_url());
                    intent.putExtra("seller_number", modelList.get(itemPosition).getSeller_number());
                    context.startActivity(intent);
                }
            });
        }
    }
}
