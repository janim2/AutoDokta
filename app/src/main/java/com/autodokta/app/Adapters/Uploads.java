package com.autodokta.app.Adapters;

public class Uploads {

    public String upload_id;
    public String product_name;
    public String product_price;
    public String buyer_number;
    public String description;
    public String product_image;

    public Uploads(){
//       Default constructor required for calls to dataSnapshot
    }

//   non-empty constructor for all fields
    public Uploads(String upload_id, String product_name, String product_price, String buyer_number, String description, String product_image) {
        this.upload_id = upload_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.buyer_number = buyer_number;
        this.description = description;
        this.product_image = product_image;
    }

//    Creating getters for all fields
    public String getUpload_id() {
        return upload_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getBuyer_number() {
        return buyer_number;
    }

    public String getDescription() {
        return description;
    }

    public String getProduct_image() {
        return product_image;
    }
}
