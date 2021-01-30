package com.autodokta.app.Models;

public class EventsModel {
    String eventID;
    String image;
    String title;
    String location;
    String description;
    String rate;
    String prize;

    public EventsModel(String eventID,      String image,
                       String title,        String location,
                       String description,  String rate,
                       String prize) {

        this.eventID        =   eventID;
        this.image          =   image;
        this.title          =   title;
        this.location       =   location;
        this.description    =   description;
        this.rate           =   rate;
        this.prize          =   prize;
    }

    public String getEventID() {
        return eventID;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getRate() {
        return rate;
    }

    public String getPrize() {
        return prize;
    }
}
