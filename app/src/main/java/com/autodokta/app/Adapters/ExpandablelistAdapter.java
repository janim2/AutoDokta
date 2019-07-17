package com.autodokta.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.autodokta.app.R;

import java.util.HashMap;
import java.util.List;

public class ExpandablelistAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> listDataHeader;
    HashMap<String,List<String>> listHashMap;


    public ExpandablelistAdapter(Context context, List<String> listDataHeader, HashMap<String,List<String>> listHashMap){
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;

        }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return listHashMap.get(listDataHeader.get(groupPosition)).size();
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_group,null);
        }

        RadioButton ibListheader = (RadioButton) convertView.findViewById(R.id.paymentype);
        ibListheader.setText(headerTitle);
//        ImageView listimage = (ImageView)convertView.findViewById(R.id.ibimageView);
//
//        if(headerTitle.equals("ACCOUNT")){
//            listimage.setImageResource(R.drawable.account);
//        }
//        if(headerTitle.equals("NOTIFICATION")){
//            listimage.setImageResource(R.drawable.notifications);
//        }
//        if(headerTitle.equals("SUPPORT")){
//            listimage.setImageResource(R.drawable.hhelp);
//        }
//        if(headerTitle.equals("LEAVE")){
//            listimage.setImageResource(R.drawable.logout);
//        }
        return  convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String)getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_item,null);
            }
            TextView txtListChild = (TextView)convertView.findViewById(R.id.paymentdescription);
            txtListChild.setText(childText);

//            ImageView imageView = (ImageView)convertView.findViewById(R.id.iBimageView);
//            if(childText.equals("Password")){
//                imageView.setImageResource(R.drawable.password);
//            }
//
//            if(childText.equals("Me")){
//                imageView.setImageResource(R.drawable.person);
//            }
//
//            if(childText.equals("Search History")){
//                imageView.setImageResource(R.drawable.search);
//            }
//
//            //notifications
//
//            //support
//            if(childText.equals("Faq")){
//                imageView.setImageResource(R.drawable.web);
//            }
//
//            if(childText.equals("Report A Problem")){
//                imageView.setImageResource(R.drawable.report);
//            }
//            if(childText.equals("Terms & Policies")){
//                imageView.setImageResource(R.drawable.web);
//            }
//            if(childText.equals("About")){
//                imageView.setImageResource(R.drawable.about);
//            }
//            if(childText.equals("Log Out")){
//                imageView.setImageResource(R.drawable.run);
//            }
            return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}
