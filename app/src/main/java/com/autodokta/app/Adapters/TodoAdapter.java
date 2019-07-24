package com.autodokta.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autodokta.app.Garage;
import com.autodokta.app.R;
import com.autodokta.app.TodoDetailsActivity;
import com.autodokta.app.Todo_Services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    ArrayList<TodoItems> itemList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public TodoAdapter(ArrayList<TodoItems> itemList, Context context){
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_item_attachment,viewGroup,false);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder viewHolder, final int i) {
        TextView todo_vehicle_name = viewHolder.view.findViewById(R.id.vehicle_name);
        ImageView del_image = viewHolder.view.findViewById(R.id.task_delete);
        ImageView details = viewHolder.view.findViewById(R.id.todo_details);
        LinearLayout todo_layout = viewHolder.view.findViewById(R.id.todo_layout);

        todo_vehicle_name.setText(itemList.get(i).getVehicle_nameString());

        todo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_todo_service = new Intent(v.getContext(),Todo_Services.class);
                goto_todo_service.putExtra("the_todoID",itemList.get(i).getTodoId());
                v.getContext().startActivity(goto_todo_service);
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TodoDetailsActivity.class);
//                intent.putExtra("title",itemList.get(i).getTodo_Item_Title());
                intent.putExtra("note",itemList.get(i).getTodo_Item_Message());
                intent.putExtra("the_date",itemList.get(i).getTodo_Item_Date());
                intent.putExtra("the_vehicle_name",itemList.get(i).getVehicle_nameString());
                intent.putExtra("the_vehicle_model",itemList.get(i).getVehicle_model());
                intent.putExtra("the_vehicle_year",itemList.get(i).getCar_year());
                intent.putExtra("the_vehicle_fuel_capacity",itemList.get(i).getCar_fuel_capacity());
                intent.putExtra("the_vehicle_maker",itemList.get(i).getVehicle_manufacturer());
                intent.putExtra("the_vehicle_fuel_type",itemList.get(i).getVehicle_fuel_type());
                intent.putExtra("the_vehicle_type",itemList.get(i).getVehicle_type());
                v.getContext().startActivity(intent);
            }
        });

        del_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("todoList").child(user.getUid());

                DatabaseReference item = reference.child(itemList.get(i).getTodoId());
                item.removeValue();
                v.getContext().startActivity(new Intent(v.getContext(), Garage.class));
                Toast.makeText(v.getContext(),"Item Removed From Todo",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
