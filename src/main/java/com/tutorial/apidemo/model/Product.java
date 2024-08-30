package com.tutorial.apidemo.model;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tblProduct")
public class Product {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)//auto-
    @SequenceGenerator(
        name = "product_sequence",
        sequenceName = "product_sequence",
        allocationSize = 1 
        //increment by 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "product_sequence"
    )
    private Long id;
    //validate = constraints
    @Column(nullable = false, unique = true,length=300)
    private String productName;
    private int productyear;
    private Double price;
    private String url;
    
    public Product(){}
    //calculated field = transient
    @Transient  
    private int age; // age is calculated from 'year'
    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - productyear;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public Product(String productName, int productyear, Double price, String url) {
        this.productName = productName;
        this.productyear = productyear;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getYear() {
        return productyear;
    }

    public void setYear(int productyear) {
        this.productyear = productyear;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Product{" +
                 "id" + id +
                  ", productName=" + productName + 
                  ", age=" + age +
                  ", year=" + productyear + 
                  ", price=" + price +
                  ", url=" + url + '\''+
                  '}';
    }

}
