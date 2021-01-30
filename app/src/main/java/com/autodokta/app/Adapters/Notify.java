package com.autodokta.app.Adapters;

public class Notify {


    public String title;
    public String message;
    public long time;
    public String imagetype;
    public String request_id;

    public Notify(){

    }

    public Notify(String title, String message, long time, String imagetype, String request_id) {

        this.title = title;
        this.message = message;
        this.time = time;
        this.imagetype = imagetype;
        this.request_id = request_id;
    }


    public String getTitle(){return title; }

    public String getMessage(){return message; }

    public long getTime(){return time; }

    public String getImageType(){return imagetype; }

    public String getRequest_id(){return request_id; }
}
