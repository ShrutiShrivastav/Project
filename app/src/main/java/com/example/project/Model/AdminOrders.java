package com.example.project.Model;

public class AdminOrders {
    private String name,phone,address,state,city,date,time,amount ;

    public AdminOrders() {
    }

    public AdminOrders(String name, String phone, String address, String state, String city, String date, String time, String amount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.state = state;
        this.city = city;
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
