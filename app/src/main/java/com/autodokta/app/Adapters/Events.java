package com.autodokta.app.Adapters;

public class Events {
    public String event_name,location,rate,description,image;

//    empty constructor
    public Events(){

    }

//    real constructor
    public Events(String event_name, String location, String rate, String description, String image) {
        this.event_name = event_name;
        this.location = location;
        this.rate = rate;
        this.description = description;
        this.image = image;
    }

//    getters and setters

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
