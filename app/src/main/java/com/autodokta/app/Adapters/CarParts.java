package com.autodokta.app.Adapters;

public class CarParts {


    public  String partId;
    public String image;
    public String name;
    public String description;
    public String price;

    public CarParts(String partId, String image, String name, String description, String price) {

        this.partId = partId;
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getPartId(){return partId; }

    public String getImage(){return image; }

    public String getname(){return name; }

    public String getDescription(){return description; }

    public String getPrice(){return price; }



}
