package com.autodokta.app.Adapters;

public class CarParts {


    public  String partId;
    public String image;
    public String name;
    public String description;
    public String price;
    public String sellersNumber;
    public String product_rating;
    public String quantity;

    private boolean isNew = false;

    public CarParts(){

    }

    public CarParts(String partId, String image, String name, String description, String price,
                    boolean isNew, String sellersNumber,String quantity,String product_rating) {

        this.partId = partId;
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isNew = isNew;
        this.sellersNumber = sellersNumber;
        this.quantity = quantity;
        this.product_rating = product_rating;
    }


    public boolean isNew() {
        return isNew;
    }

    public String getPartId(){return partId; }

    public String getImage(){return image; }

    public String getname(){return name; }

    public String getDescription(){return description; }

    public String getPrice(){return price; }

    public String getsellersNumber(){return sellersNumber; }

    public String getQuantity(){
        return quantity;
    }

    public String getProduct_rating(){
        return product_rating;
    }


}
