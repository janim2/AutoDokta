package com.autodokta.app.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodokta.app.EventDetails;
import com.autodokta.app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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

        final TextView eventName = viewHolder.itemView.findViewById(R.id.eventImage);
        final TextView Description = viewHolder.itemView.findViewById(R.id.eventDesc);
        final ImageView eventImage = viewHolder.itemView.findViewById(R.id.eventImage);

        eventName.setText(eventsArrayList.get(i).getEvent_name());
        Description.setText(eventsArrayList.get(i).getDescription());

        StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("test");
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(eventImage);

            }
        });







        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                get data from views
                String myTitle = eventName.getText().toString();
                String myDescription = Description.getText().toString();
                Drawable mDrawable = eventImage.getDrawable();
                Bitmap bitmap  = ((BitmapDrawable) mDrawable).getBitmap();

//                Pass the data to the next activity
                Intent intent = new Intent(v.getContext(), EventDetails.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("title",myTitle);
                intent.putExtra("description",myDescription);
                intent.putExtra("image",bytes);
                v.getContext().startActivity(intent);

            }
        });




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
