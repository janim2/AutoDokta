package com.autodokta.app.fragments;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Items {

    public String uid;
    public String name;
    public String description;
    public String price;
    public String image;

    public Items() {
    }

    public Items(String uid, String name, String description, String price, String image) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("description", description);
        result.put("price", price);
        result.put("image", image);
        return result;
    }
}
