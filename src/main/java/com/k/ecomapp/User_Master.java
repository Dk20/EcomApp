package com.k.ecomapp;


public class User_Master {
    int u_id;
    String u_name;
    String u_pass;
    String reg_date;

    public User_Master(int u_id, String u_name, String u_pass, String reg_date) {
        this.u_id = u_id;
        this.u_name = u_name;
        this.u_pass = u_pass;
        this.reg_date = reg_date;
    }

    public int getU_id() {
        return u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public String getU_pass() {
        return u_pass;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public void setU_pass(String u_pass) {
        this.u_pass = u_pass;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public boolean doesMatch(String user,String pass){

        if(user.equals(u_name) && pass.equals(u_pass))
            return true;
        else
            return false;
    }
}
