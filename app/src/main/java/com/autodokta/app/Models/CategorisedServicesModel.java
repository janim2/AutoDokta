package com.autodokta.app.Models;

public class CategorisedServicesModel {

    private String id;
    private String title;
    private String short_description;
    private  String fullDesc;
//    private double rating;
    private String price;
    private String image_url;
    private String location;
    private  String seller_number;

//    empty constructor
    public CategorisedServicesModel(){

    }

    //field constructors

    public CategorisedServicesModel(String id, String title, String short_description, String fullDesc, String price, String image_url, String location, String seller_number) {
        this.id = id;
        this.title = title;
        this.short_description = short_description;
        this.fullDesc = fullDesc;
        this.price = price;
        this.image_url = image_url;
        this.location = location;
        this.seller_number = seller_number;
    }


    // creating getters


    public String getId() {
        return id;
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

    public String getPrice() {
        return price;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getLocation() {
        return location;
    }

    public String getSeller_number() {
        return seller_number;
    }
}
