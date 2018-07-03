package com.k.ecomapp;

public class Transaction_Master {
    int t_id;
    int u_id;
    int c_id;
    double amount;
    String TimeStamp;

    public Transaction_Master(int t_id, int u_id, int c_id, double amount, String timeStamp) {

        this.t_id = t_id;
        this.u_id = u_id;
        this.c_id = c_id;
        this.amount = amount;
        TimeStamp = timeStamp;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }


    public int getT_id() {
        return t_id;
    }

    public int getU_id() {
        return u_id;
    }

    public int getC_id() {
        return c_id;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }
}
