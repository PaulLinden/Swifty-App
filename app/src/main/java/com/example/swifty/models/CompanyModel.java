package com.example.swifty.models;


import java.io.Serializable;
import java.util.List;

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
