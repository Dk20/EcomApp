package com.k.ecomapp;

public class Catalogue_Master {

    int c_id;
    String c_type;
    String ProductName;
    double price;
    int stock;

    public Catalogue_Master(int c_id, String c_type, String productName, double price, int stock) {
        this.c_id = c_id;
        this.c_type = c_type;
        ProductName = productName;
        this.price = price;
        this.stock = stock;
    }

    public int getC_id() {
        return c_id;
    }

    public String getC_type() {
        return c_type;
    }

    public String getProductName() {
        return ProductName;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
