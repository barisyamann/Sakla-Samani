package com.example.saklasamani.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String userName;
    private String password;
    private double income;
    private double budget;
    private List<ExtraIncome> extraIncomes;
    private List <Harcama> harcamas;


    public User(String userName, String password, double income, double budget) {
        this.userName = userName;
        this.password = password;
        this.income = income;
        this.budget=budget;
        this.extraIncomes = new ArrayList<>();
        this.harcamas=new ArrayList<>();
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public List<ExtraIncome> getExtraIncomes() { return extraIncomes; }

    public void setExtraIncomes(List<ExtraIncome> extraIncomes) {
        this.extraIncomes = extraIncomes;
    }

    public void addExtraIncome(double amount, String note) {
        extraIncomes.add(new ExtraIncome(amount, note));
    }

    public boolean removeExtraIncomeByNote(String note) {
        return extraIncomes.removeIf(income -> income.getNote().equals(note));
    }

    public double getTotalExtraIncome() {
        double total = 0;
        for (ExtraIncome ei : extraIncomes) {
            total += ei.getAmount();
        }
        return total;
    }

    public List<Harcama> getHarcama() { return harcamas; }

    public void setHarcama(List<Harcama> harcamas) {
        this.harcamas = harcamas;
    }

    public void addHarcama(double amount, String note) {
        harcamas.add(new Harcama(amount, note));
    }

    public boolean removeHarcamaByNote(String note) {
        return harcamas.removeIf(harcamas -> harcamas.getNote().equals(note));
    }

    public double getTotalHarcama() {
        double total = 0;
        for (Harcama ei : harcamas) {
            total += ei.getAmount();
        }
        return total;
    }
}
