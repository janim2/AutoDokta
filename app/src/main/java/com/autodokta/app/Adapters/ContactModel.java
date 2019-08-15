package com.autodokta.app.Adapters;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ContactModel {

    public String id, name,email,subject,description,orderNumber;


//    Default constructor required for call to Datasnapshot.getValue(User.class)
    public ContactModel(){

    }

    public ContactModel(String id, String name, String email, String subject, String description, String orderNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.description = description;
        this.orderNumber = orderNumber;
    }



}
