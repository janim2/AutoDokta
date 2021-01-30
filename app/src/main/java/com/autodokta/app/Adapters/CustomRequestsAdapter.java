package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.Accessories;
import com.autodokta.app.Chat;
import com.autodokta.app.Models.CustomRequestsModel;
import com.autodokta.app.Models.EventsModel;
import com.autodokta.app.R;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomRequestsAdapter extends RecyclerView.Adapter<CustomRequestsAdapter.ViewHolder> {
    ArrayList<CustomRequestsModel>      itemList;
    Context                             context;
    Accessories                         adapter_accessor;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public CustomRequestsAdapter(ArrayList<CustomRequestsModel> itemList, Context context){
        this.itemList       =   itemList;
        this.context        =   context;
        adapter_accessor    =   new Accessories(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_request_attachment,parent,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TextView  chat_with_requester     =   holder.view.findViewById(R.id.chat_with_requester);
        final TextView  name                    =   holder.view.findViewById(R.id.name);
        final TextView  description             =   holder.view.findViewById(R.id.description);
        final TextView  budget                  =   holder.view.findViewById(R.id.budget);
        final TextView  requester_name          =   holder.view.findViewById(R.id.requester_name);

        name.setText(itemList.get(position).getItem_name());
        description.setText(itemList.get(position).getDescription());
        budget.setText("GHS " + itemList.get(position).getBudget());
        requester_name.setText(itemList.get(position).getRequester_name());

        chat_with_requester.setOnClickListener(view -> {
            Intent chat_intent = new Intent(context, Chat.class);
                chat_intent.putExtra("requester_id", itemList.get(position).getRequestID());
            context.startActivity(chat_intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
