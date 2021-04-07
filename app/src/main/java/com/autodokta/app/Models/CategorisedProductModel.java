package com.autodokta.app.Models;

public class CategorisedProductModel {

    private String id;
    private String title;
    private String description;
    private String price;
    private String image_url;
    private String location;
    private String region;
    private  String seller_number;

//    empty constructor
    public CategorisedProductModel(){

    }

    public CategorisedProductModel(String id, String title, String description, String price, String image_url, String location, String region, String seller_number) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image_url = image_url;
        this.location = location;
        this.region = region;
        this.seller_number = seller_number;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public String getRegion() {
        return region;
    }

    public String getSeller_number() {
        return seller_number;
    }
}
