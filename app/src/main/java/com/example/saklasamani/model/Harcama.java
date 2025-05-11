package com.example.saklasamani.model;

import java.io.Serializable;

public class Harcama implements Serializable {
    private double amount;
    private String note;

    public Harcama(double amount, String note) {
        this.amount = amount;
        this.note = note;
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
}
