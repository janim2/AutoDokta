package com.autodokta.app.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.Models.CategorisedServicesModel;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.Double.parseDouble;

public class CategorisedServicesAdapter extends RecyclerView.Adapter<CategorisedServicesAdapter.ViewHolder> {

//    context to inflate layout
    Context context;

//    storing all  product in  a list
    private List<CategorisedServicesModel> modelList;

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
        viewHolder.shortDesc.setText(servicesModel.getShortdesc());
        viewHolder.price.setText((servicesModel.getPrice()));

        final ImageView testImage = viewHolder.itemView.findViewById(R.id.myImage);
        Glide.with(context).load(modelList.get(i).getImage()).thumbnail(0.3f).into(testImage);





    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,shortDesc,price;
//        ImageView serviceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (itemView).findViewById(R.id.textViewTitle);
            shortDesc = (itemView).findViewById(R.id.textViewShortDesc);
            price = (itemView).findViewById(R.id.textViewPrice);
//            serviceImage = (itemView).findViewById(R.id.myImage);
        }
    }
}
