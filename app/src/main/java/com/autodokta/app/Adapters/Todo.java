package com.autodokta.app.Adapters;

import java.io.Serializable;
import java.util.HashMap;

public class Todo implements Serializable {

    private String name;
    private String message;
    private String date;
    private String vehicle_nameString;
    private String vehicle_model;
    private String vehicle_year;
    private String vehicle_fuel_capacity;
    private String vehicle_manufacturer;
    private String vehicle_fuel_type;
    private String vehicle_type;

//    this is for the todo
    String serviceName;
    int image;



    public Todo() {

    }

    public Todo(String serviceName,int image){
        this.serviceName = serviceName;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVehicle_nameString(String vehicle_nameString) { this.vehicle_nameString = vehicle_nameString; }

    public void setVehicle_model(String vehicle_model) { this.vehicle_model = vehicle_model; }

    public void setCar_year(String year) { this.vehicle_year = year;  }

    public void setCar_fuel_capacity(String fuel_capacity) { this.vehicle_fuel_capacity = fuel_capacity; }

    public void setVehicle_manufacturer(String vehicle_manufacturer) { this.vehicle_manufacturer = vehicle_manufacturer; }

    public void setVehicle_fuel_type(String vehicleFuelType) { this.vehicle_fuel_type = vehicleFuelType; }

    public void setVehicle_type(String vehicle_type) { this.vehicle_type = vehicle_type; }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getImage() {
        return image;
    }


    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
//        todo.put("name", name);
        todo.put("message", message);
        todo.put("date", date);
        todo.put("vehicle_name",vehicle_nameString);
        todo.put("vehicle_model",vehicle_model);
        todo.put("vehicle_year",vehicle_year);
        todo.put("vehicle_fuel_capa",vehicle_fuel_capacity);
        todo.put("vehicle_type",vehicle_year);
        todo.put("fuel_type",vehicle_fuel_type);
        todo.put("manufacturer",vehicle_manufacturer);

        return todo;
    }


}
