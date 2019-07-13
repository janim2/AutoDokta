package com.autodokta.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewCartDialogue {
//    <!--testing-->

    public void showDialog(final Activity activity, String msg, final String user, final String cartItemId){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_cart_layout);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button continnue = (Button) dialog.findViewById(R.id.continnue);
        continnue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button cashout = (Button) dialog.findViewById(R.id.cashout);
        cashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(v.getContext(),"Coming Soon",Toast.LENGTH_LONG).show();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("cart");

                mDatabase.child(user).child(cartItemId);

                DatabaseReference quantityvalue = mDatabase.child(user).child(cartItemId);
                quantityvalue.child("quantity").setValue("1");
                dialog.dismiss();
                activity.finish();
                Toast.makeText(v.getContext(),"Added 1 item to Cart",Toast.LENGTH_LONG).show();
                v.getContext().startActivity(new Intent(v.getContext(),Cart.class));

//                mDatabase.child("users").child(user).child("lastname").setValue(lname);
//                mDatabase.child("users").child(user).child("username").setValue(username);
//                mDatabase.child("users").child(user).child("address").setValue(address);
//                mDatabase.child("users").child(user).child("number").setValue(phoneNumber);


            }
        });

        dialog.show();

    }
}