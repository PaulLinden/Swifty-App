package com.example.swifty.models;


import java.io.Serializable;
import java.util.List;

public class CompanyModel implements Serializable {

    public CompanyModel(){};
    private String name;
    private String industry;
    private String url;
    private List<String> productList;

    public String getIndustry() {return industry;}

    public void setIndustry(String industry) {this.industry = industry;}

    public String getName() {return name;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public List<String> getProductList() {return productList;}

    public void setProductList(List<String> productList) {this.productList = productList;}

    public void setName(String name) {this.name = name;}
}
