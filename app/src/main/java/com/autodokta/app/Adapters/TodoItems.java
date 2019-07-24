package com.autodokta.app.Adapters;

public class TodoItems { ;
    public String title;
    public String note;
    public String todoId;
    public String thedate;
    public String vehicle_nameString;
    public String vehicle_model;
    public String vehicle_year;
    public String vehicle_fuel_capacity;
    public String vehicle_manufacturer;
    public String vehicle_fuel_type;
    public String vehicle_type;


    public TodoItems(String todoId,String message, String thedate,String vehicle_nameString,
            String vehicle_model,String vehicle_year,String vehicle_fuel_capacity,
                     String vehicle_manufacturer,String vehicle_fuel_type,String vehicle_type){

        this.todoId = todoId;
//        this.title = thetitle;
        this.note = message;
        this.thedate = thedate;
        this.vehicle_nameString = vehicle_nameString;
        this.vehicle_model = vehicle_model;
        this.vehicle_year = vehicle_year;
        this.vehicle_fuel_capacity = vehicle_fuel_capacity;
        this.vehicle_manufacturer = vehicle_manufacturer;
        this.vehicle_fuel_type = vehicle_fuel_type;
        this.vehicle_type = vehicle_type;
    }

    public String getTodoId(){return todoId; }

    public String getTodo_Item_Title(){return title; }

    public String getTodo_Item_Message(){return note; }

    public String getTodo_Item_Date(){return thedate; }

    public String getVehicle_nameString() { return vehicle_nameString; }

    public String getVehicle_model() { return vehicle_model; }

    public String getCar_year() { return vehicle_year;  }

    public String getCar_fuel_capacity() {  return vehicle_fuel_capacity;}

    public String getVehicle_manufacturer() { return vehicle_manufacturer; }

    public String getVehicle_fuel_type() { return vehicle_fuel_type; }

    public String getVehicle_type() {  return vehicle_type; }







}
