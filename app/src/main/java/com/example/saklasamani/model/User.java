package com.example.saklasamani.model;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String password;
    private double income;
    private double budget;

    public User(int id, String userName, String password, double income, double budget) {

        this.userName = userName;
        this.password = password;
        this.income = income;
        this.budget = budget;
    }

    public User(String userName, String password, double income, double budget) {
        this.userName = userName;
        this.password = password;
        this.income = income;
        this.budget = budget;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getter - Setter'lar




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
