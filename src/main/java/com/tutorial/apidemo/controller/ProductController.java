package com.tutorial.apidemo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.apidemo.model.Product;
import com.tutorial.apidemo.model.ResponseObject;
import com.tutorial.apidemo.repositories.ProductRepository;


@RestController
@RequestMapping(path = "/api/v1/Product")

public class ProductController {

    @Autowired
    private ProductRepository repository;
    
    @GetMapping("")
    //this request is: http://localhost:8080/api/v1/Product
    List<Product> getAllProducts(){
        return repository.findAll();  
    }

    @GetMapping("/{id}")
    //Let's return an object with: data, message, status
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = repository.findById(id);
        if(foundProduct.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query  product successfully", foundProduct)
            );
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find product with id = " + id, "" )
            );
        }
    }

    
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        //check same name
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(!foundProducts.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failled", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Insert product successfully", repository.save(newProduct))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id){
        Product updateProduct = repository.findById(id).map(product -> {
            product.setProductName(newProduct.getProductName());
            product.setYear(newProduct.getYear());
            product.setPrice(newProduct.getPrice());
            product.setUrl(newProduct.getUrl());
            return repository.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return repository.save(newProduct);
        });

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Update product successfully", updateProduct)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = repository.existsById(id);
        if(exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failled", "Cannot find product to delete  ", "") 
        );
    }
}

/*
docker run -d --rm --name mysql-spring-boot-tutorial \-e MYSQL_ROOT_PASSWORD=123456 \-e MYSQL_USER=vuongqn \-e MYSQL_PASSWORD=123456 \-e MYSQL_DATABASE=test_db \-p 3309:3306 \--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \mysql:latest

*/
