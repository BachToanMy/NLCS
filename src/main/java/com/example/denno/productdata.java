package com.example.denno;

import java.util.Date;

public class productdata {

    private Integer id;
    private String productID;
    private String productName;
    private Integer stock;
    private Double price;
    private String status;
    private String image;
    private Date date;
    private String type;
    private Integer quantity;


    public productdata(Integer id, String productID, String productName
                        ,String type,Integer stock,Double price,
                       String status,String image,Date date){
        this.id = id;
        this.productID = productID;
        this.productName = productName;
        this.status =status;
        this.stock = stock;
        this.price = price;
        this.image = image;
        this.date = date;
        this.type = type;

    }
    public productdata(Integer id,String productID, String productName,String type
            ,Double price,String image,Date date){
        this.id = id;
        this.productID = productID;
        this.productName=productName;
        this.type=type;
        this.price=price;
        this.image=image;
        this.date=date;
    }

    public productdata(Integer id,String productID, String productName,String type
            ,Integer quantity,Double price,String image,Date date){
        this.id = id;
        this.productID = productID;
        this.productName=productName;
        this.type=type;
        this.price=price;
        this.image=image;
        this.date=date;
        this.quantity=quantity;
    }
    public productdata(Integer id,String productID, String productName
            ,Double price,String image){
        this.id = id;
        this.productID = productID;
        this.productName=productName;
        this.price=price;
        this.image=image;
        this.date=date;
    }
    public Integer getId(){return id;}
    public String getProductID(){return productID;}
    public String getProductName(){return productName;}
    public Integer getStock(){return stock;}
    public Double getPrice(){return price;}
    public String getStatus(){return status;}
    public String getImage(){return image;}
    public Date getDate(){return date;}
    public String getType(){return type;}
    public Integer getQuantity(){return quantity;}
}
