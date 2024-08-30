package com.tutorial.apidemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tutorial.apidemo.model.Product;

public interface  ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductName(String productName);
}
