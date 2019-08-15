package com.autodokta.app.Adapters;

public class Notify {


    public String title;
    public String message;
    public long time;
    public String imagetype;

    public Notify(){

    }

    public Notify(String title, String message, long time, String imagetype) {

        this.title = title;
        this.message = message;
        this.time = time;
        this.imagetype = imagetype;
    }


    public String getTitle(){return title; }

    public String getMessage(){return message; }

    public long getTime(){return time; }

    public String getImageType(){return imagetype; }


}
