package com.autodokta.app.Adapters;

public class CarParts {


    public  String partId;
    public String image_url;
    public String views;
    public String title;
    public String description;
    public String price;
    public String seller_number;
    public String product_rating;
    public String quantity;



    public String product_type;

    private boolean isNew = false;

    public CarParts(){

    }


    public CarParts(String partId, String image_url, String views, String title, String description, String price, String seller_number, String product_rating, String quantity, String product_type, boolean isNew) {
        this.partId = partId;
        this.image_url = image_url;
        this.views = views;
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller_number = seller_number;
        this.product_rating = product_rating;
        this.quantity = quantity;
        this.product_type = product_type;
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getPartId(){return partId; }



    public String getViews(){return views; }



    public String getDescription(){return description; }

    public String getPrice(){return price; }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getSeller_number() {
        return seller_number;
    }

    public String getQuantity(){
        return quantity;
    }

    public String getProduct_rating(){
        return product_rating;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String setProduct_type(String product_type) {
//        this.product_type = product_type;
        return product_type;
    }
}
