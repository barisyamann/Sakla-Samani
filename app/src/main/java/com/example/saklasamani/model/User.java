package com.example.saklasamani.model;

public class User {

    private String userName;
    private String password;
    private double income;

    //constructor
    public User(String userName, String password, double income) {
        this.userName = userName;
        this.password = password;
        this.income = income;
    }

    //get-set
    public void setUserName(String userName) { this.userName = userName;}
    public String getUserName(){return  userName;}
    public void setPassword(String password) { this.password = password;}
    public String getPassword(){return  password;}
    public void setIncome(double income) { this.income = income;}
    public double getIncome(){return  income;}






}