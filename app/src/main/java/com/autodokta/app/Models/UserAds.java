package com.autodokta.app.Models;

import java.io.Serializable;

public class UserAds  {

    private String id;
    private String title;
    private String short_description;
    private  String fullDesc;
    //    private double rating;
    private String price;
    private String image_url;
    private String location;
    private  String seller_number;
    private String service_type;

    public UserAds(){

    }

    public UserAds(String id, String title, String short_description, String fullDesc, String price, String image_url, String location, String seller_number, String service_type) {
        this.id = id;
        this.title = title;
        this.short_description = short_description;
        this.fullDesc = fullDesc;
        this.price = price;
        this.image_url = image_url;
        this.location = location;
        this.seller_number = seller_number;
        this.service_type = service_type;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getTitle() {
        return title;
    }


    public String getShort_description() {
        return short_description;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public String getSeller_number() {
        return seller_number;
    }

    public String getPrice() {

        return price;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getService_type() {
        return service_type;
    }
}
