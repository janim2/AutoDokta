package com.autodokta.app.Adapters;

public class TodoItems { ;
    public String title;
    public String message;
    public String todoId;
    public String thedate;


    public TodoItems(String todoId,String thetitle,String message, String thedate){
        this.todoId = todoId;
        this.title = thetitle;
        this.message = message;
        this.thedate = thedate;
    }

    public String getTodoId(){return todoId; }

    public String getTodo_Item_Title(){return title; }

    public String getTodo_Item_Message(){return message; }

    public String getTodo_Item_Date(){return thedate; }






}
