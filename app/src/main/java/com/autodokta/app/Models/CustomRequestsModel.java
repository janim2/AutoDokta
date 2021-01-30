package com.autodokta.app.Models;

public class CustomRequestsModel {
    String requestID;
    String budget;
    String description;
    String item_name;
    String requester_name;
    String requester_id;

    public CustomRequestsModel(String requestID, String budget, String description,
                                String item_name, String requester_name, String requester_id) {
        this.requestID      = requestID;
        this.budget         = budget;
        this.description    = description;
        this.item_name      = item_name;
        this.requester_name = requester_name;
        this.requester_id   = requester_id;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getBudget() {
        return budget;
    }

    public String getDescription() {
        return description;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public String getRequester_id() {
        return requester_id;
    }
}
