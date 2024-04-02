package com.example.swifty.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionModel implements Serializable {

    private UserModel user;
    private CompanyModel company;
    private LocalDateTime dateTime;
    private List<ProductModel> products;

    public TransactionModel(UserModel user, CompanyModel company, LocalDateTime dateTime, List<ProductModel> products){
        this.user = user;
        this.company = company;
        this.dateTime = dateTime;
        this.products = products;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
