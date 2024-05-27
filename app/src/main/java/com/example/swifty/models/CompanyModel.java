package com.example.swifty.models;


import java.io.Serializable;
import java.util.List;

/*
* This class represents a company model. It contains the company name, url, and a list of products.
* This class implements the Serializable interface to allow it to be serialized and deserialized.
* */

public class CompanyModel implements Serializable {

    public CompanyModel(String companyName, String url, List<String> productList){
        this.companyName = companyName;
        this.url = url;
        this.productList = productList;
    }

    private final String companyName;
    private final String url;
    private final List<String> productList;

    public String getCompanyName() {return companyName;}

    public String getUrl() {return url;}

    public List<String> getProductList() {return productList;}

}
