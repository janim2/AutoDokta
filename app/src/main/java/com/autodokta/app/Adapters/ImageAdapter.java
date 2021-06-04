package com.autodokta.app.Adapters;

import android.content.Context;

import android.support.annotation.NonNull;
import androidx.core.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autodokta.app.R;

import java.util.ArrayList;

//The whole code is all about populating the ViewPager with custom content
public class ImageAdapter extends PagerAdapter {
        private ArrayList<Integer> images;
        private LayoutInflater inflater;
        private Context context;

    public ImageAdapter(ArrayList<Integer> images, Context context) {
        this.images = images;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //    Check whether the view is associated with key and returned by instantiateItem()
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


//    This method create the page position passed as an argument
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      View imageLayout = inflater.inflate(R.layout.slide,container,false);
      ImageView imageView = (ImageView)imageLayout.findViewById(R.id.image);
      imageView.setImageResource(images.get(position));
      container.addView(imageLayout,0);
      return imageLayout;
    }

//    This method removes the page from its current position from container
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

//    Returns the the number of views available in the viewPager
    @Override
    public int getCount() {
        return images.size();
    }
}
