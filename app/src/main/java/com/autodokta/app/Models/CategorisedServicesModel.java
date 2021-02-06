package com.autodokta.app.Models;

public class CategorisedServicesModel {

    private int id;
    private String title;
    private String shortdesc;
//    private double rating;
    private String price;
    private int image;

//    empty constructor
    public CategorisedServicesModel(){

    }

//    creating setters  and getters for it


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

//    public double getRating() {
//        return rating;
//    }

//    public void setRating(double rating) {
//        this.rating = rating;
//    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
