package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.CategorisedDetails;
import com.autodokta.app.Models.CategorisedProductModel;
import com.autodokta.app.Models.CategorisedServicesModel;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CategorisedProductAdapter extends RecyclerView.Adapter<CategorisedProductAdapter.ViewHolder> {

    //    context to inflate layout
    Context context;

    //    storing all  product in  a list
    private List<CategorisedProductModel> modelList;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public CategorisedProductAdapter(Context context, List<CategorisedProductModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public CategorisedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //inflating and returning our view holder
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_type_layout,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CategorisedProductAdapter.ViewHolder viewHolder, int i) {

        CategorisedProductModel productModel = modelList.get(i);

        viewHolder.title.setText(productModel.getTitle());
        viewHolder.price.setText("GHC " + productModel.getPrice());
        viewHolder.location.setText(productModel.getLocation());

//        Load images into the recyclerview using Glide
        if (productModel.getImage_url() != null && !productModel.getImage_url().isEmpty()){
            Glide.with(context).load(productModel.getImage_url()).into(viewHolder.serviceImage);
        }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    class ViewHolder extends  RecyclerView.ViewHolder{

        TextView title, price,location;
        ImageView serviceImage;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);


            title = (itemView).findViewById(R.id.prod_Title);
            price = (itemView).findViewById(R.id.prod_Price);
            location = (itemView).findViewById(R.id.prod_Location);
            serviceImage = (itemView).findViewById(R.id.prod_pic);

            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(context, CategorisedDetails.class);
                    intent.putExtra("id",modelList.get(itemPosition).getId());
                    intent.putExtra("title",modelList.get(itemPosition).getTitle());
                    intent.putExtra("price",modelList.get(itemPosition).getPrice());
                    intent.putExtra("img", modelList.get(itemPosition).getImage_url());
                    context.startActivity(intent);
                }
            });
        }


    }
}
