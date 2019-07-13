package com.autodokta.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.autodokta.app.ItemDetailsActivity;
import com.autodokta.app.MainActivity;
import com.autodokta.app.R;


public class ImageListFragment extends Fragment {

    public static final String STRING_IMAGE_URI = "ImageUri";
    public static final String STRING_IMAGE_POSITION = "ImagePosition";
    private static final String STRING_NAME = "ItemName";
    private static final String STRING_DESCRIPTION = "ItemDesc";
    private static final String STRING_PRICE = "ItemPrice";
    private static MainActivity mActivity;

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Items, ViewHolder> mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.layout_recylerview_list, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Query recentPostsQuery = mDatabase.child("listings").limitToFirst(100);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Items>()
                .setQuery(recentPostsQuery, Items.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Items, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Items model) {
                holder.bindToPost(model);

                holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
                        intent.putExtra(STRING_IMAGE_URI, model.image);
                        intent.putExtra(STRING_NAME, model.name);
                        intent.putExtra(STRING_DESCRIPTION, model.description);
                        intent.putExtra(STRING_PRICE, model.price);
                        mActivity.startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new ViewHolder(inflater.inflate(R.layout.list_item, viewGroup, false));
            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    //
//    public static class SimpleStringRecyclerViewAdapter
//            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
//
//        private String[] mValues;
//        private RecyclerView mRecyclerView;
//
//
//
//        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, String[] items) {
//            mValues = items;
//            mRecyclerView = recyclerView;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//            return new ViewHolder(view);
//        }
//
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//
//
//            Picasso.with(mActivity.getBaseContext()).load(mValues[position]).into(holder.mImageView);
//
//
//            //Set click action for wishlist
//            holder.mImageViewWishlist.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
//                    imageUrlUtils.addWishlistImageUri(mValues[position]);
//                    holder.mImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_18dp);
//                    notifyDataSetChanged();
//                    Toast.makeText(mActivity, "Item added to wishlist.", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
//                    intent.putExtra(STRING_IMAGE_URI, mValues[position]);
//                    intent.putExtra(STRING_IMAGE_POSITION, position);
//                    mActivity.startActivity(intent);
//
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return mValues.length;
//        }
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final LinearLayout mLayoutItem;
        public final ImageView mImageViewWishlist;

        public TextView name;
        public TextView desc;
        public TextView price;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.image1);
            mLayoutItem = view.findViewById(R.id.layout_item);
            mImageViewWishlist = view.findViewById(R.id.ic_wishlist);

            ////////////////////////////////////////
            name = view.findViewById(R.id.item_name);
            desc = view.findViewById(R.id.item_desc);
            price = view.findViewById(R.id.item_price);
        }

        public void bindToPost(Items model) {
            name.setText(model.name);
            desc.setText(model.description);
            price.setText(model.price);
        }
    }
//        <!--testing-->

}
