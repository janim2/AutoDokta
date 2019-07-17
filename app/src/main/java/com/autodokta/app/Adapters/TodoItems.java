package com.autodokta.app.Adapters;

public class TodoItems { ;
    public String title;
    public String message;
    public String todoId;

    public TodoItems(String todoId,String thetitle,String message){
        this.todoId = todoId;
        this.title = thetitle;
        this.message = message;
    }

    public String getTodoId(){return todoId; }

    public String getTodo_Item_Title(){return title; }

    public String getTodo_Item_Message(){return message; }






}
