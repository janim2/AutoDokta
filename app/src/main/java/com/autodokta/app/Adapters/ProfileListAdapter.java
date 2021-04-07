package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autodokta.app.Invite;
import com.autodokta.app.Membership;
import com.autodokta.app.Models.ProfileList;
import com.autodokta.app.MyAds;
import com.autodokta.app.R;
import com.autodokta.app.UserSettings;
import com.autodokta.app.User_Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProfileList> profileList;


    public ProfileListAdapter(Context context, ArrayList<ProfileList> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.prfile_list_layout,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ViewHolder viewHolder, int i) {
       ProfileList list = profileList.get(i);
       viewHolder.setMyList(list.getTitle());

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

      class ViewHolder extends RecyclerView.ViewHolder{
        private final Context context;
        private  TextView myList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myList = (TextView) itemView.findViewById(R.id.profileActions);
            itemView.setClickable(true);
            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    int itemPosition = getLayoutPosition();
                    switch (itemPosition){
                        case 0:
                            intent = new Intent(v.getContext(),MyAds.class);
                            break;
                        case 1:
                            intent = new Intent(v.getContext(), UserSettings.class);
                            break;
                        case 2:
                            intent = new Intent(v.getContext(),Membership.class);
                            break;
                        case 3:
                            intent = new Intent(v.getContext(), Invite.class);
                            break;
                        case 4:
                            FirebaseAuth.getInstance().signOut();

                            default:
                                intent = new Intent(v.getContext(), User_Profile.class);
                                break;


                    }
                    context.startActivity(intent);
                }
            });


        }

        public void setMyList(String myList) {
            this.myList.setText(myList);
        }


      }
}
