package com.example.saklasamani.model;

import java.io.Serializable;

public class Harcama implements Serializable {
    private double amount;
    private String note;
    private String category;

    public Harcama(double amount, String category, String note) {
        this.amount = amount;
        this.note = note;
        this.category = category;
    }



    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
