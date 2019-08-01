package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Cart;
import com.autodokta.app.ItemDetailsActivity;
import com.autodokta.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

    public class CartPartsAdapter extends RecyclerView.Adapter<CartPartsAdapter.ViewHolder>{
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

        public CartPartsAdapter(ArrayList<CarParts> itemList,Context context){
            this.itemList  = itemList;
            this.context  = context;
        }

        @Override
        public CartPartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
            ViewHolder vh = new ViewHolder(layoutView);
            return vh;
        }


        @Override
        public void onBindViewHolder(CartPartsAdapter.ViewHolder holder, final int position) {
            final TextView name = holder.view.findViewById(R.id.itemname);
            ImageView image = holder.view.findViewById(R.id.itemimage);
            TextView price = holder.view.findViewById(R.id.itemprize);
            final TextView quantity = holder.view.findViewById(R.id.itemquantity);
            ImageView favourite = holder.view.findViewById(R.id.addtofavourite);
            ImageView delete = holder.view.findViewById(R.id.removeitem);
            ImageView subtract = holder.view.findViewById(R.id.thesubtract);
            ImageView add = holder.view.findViewById(R.id.additem);

            //prep work before image is loaded is to load it into the cache
            final DisplayImageOptions theImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).
                    cacheOnDisk(true).build();
            final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                    defaultDisplayImageOptions(theImageOptions).build();
            ImageLoader.getInstance().init(config);
//
            String imagelink = itemList.get(position).getImage();
            imageLoader.displayImage(imagelink,image);

            name.setText(itemList.get(position).getname());
            price.setText(itemList.get(position).getPrice());
            quantity.setText(itemList.get(position).getQuantity());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid());

                    DatabaseReference item = reference.child(itemList.get(position).getPartId());
                    item.removeValue();
                    Toast.makeText(v.getContext(),"Item Removed From Cart",Toast.LENGTH_LONG).show();
                    v.getContext().startActivity(new Intent(v.getContext(),Cart.class));
                }
            });

//            favourite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(),"Coming Soon",Toast.LENGTH_LONG).show();
//                }
//            });

            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.valueOf(itemList.get(position).getQuantity()) < 2){

                    }else{
                        itemList.get(position).quantity = (Integer.valueOf(itemList.get(position).quantity) - 1) + "";
                        quantity.setText(itemList.get(position).quantity);

                        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid());

                        DatabaseReference item = reference.child(itemList.get(position).getPartId());

                        item.child("quantity").setValue(itemList.get(position).getQuantity());

                        v.getContext().startActivity(new Intent(v.getContext(), Cart.class));
                    }
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.get(position).quantity = (Integer.valueOf(itemList.get(position).quantity) + 1) + "";
                    quantity.setText(itemList.get(position).quantity);

                    FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid());
                    DatabaseReference item = reference.child(itemList.get(position).getPartId());
                    item.child("quantity").setValue(itemList.get(position).getQuantity());

                    v.getContext().startActivity(new Intent(v.getContext(), Cart.class));

//                    LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                    View view  =inflater.inflate(R.layout.activity_cart,null);
//                    TextView tv = (TextView)view.findViewById(R.id.totalprize);
//                    tv.setText(String.valueOf(Integer.valueOf(itemList.get(position).getQuantity()) * Float.valueOf(itemList.get(position).getPrice())));
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }


}
