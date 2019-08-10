package com.autodokta.app.Adapters;

public class Reviews {

    public String review_rate;
    public String message;
    public String title;
    public String name;

    public Reviews(){

    }

    public Reviews(String review_rate, String message, String title,String name) {

        this.review_rate = review_rate;
        this.message = message;
        this.title = title;
        this.name = name;
    }

    public String getReview_rate(){return review_rate; }

    public String getMessage(){
        return message;
    }

    public String getTitle(){
        return title;
    }

    public String getName(){
        return name;
    }

}
