package com.autodokta.app.Adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.R;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private Uri uri;
    private ArrayList<Events>eventsArrayList;

    public EventsAdapter(ArrayList<Events> eventsArrayList) {
        this.eventsArrayList = eventsArrayList;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.eventName.setText(eventsArrayList.get(i).getEvent_name());
        viewHolder.description.setText(eventsArrayList.get(i).getDescription());
        




//

    }

    @Override
    public int getItemCount() {
       return eventsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView eventName,description;
         ImageView eventImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = (TextView)itemView.findViewById(R.id.eventName);
            description = (TextView)itemView.findViewById(R.id.eventDesc);
            eventImage= (ImageView)itemView.findViewById(R.id.eventImage);
        }
    }
}
