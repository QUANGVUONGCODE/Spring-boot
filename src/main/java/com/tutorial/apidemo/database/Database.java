package com.tutorial.apidemo.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tutorial.apidemo.model.Product;
import com.tutorial.apidemo.repositories.ProductRepository;

@Configuration
public class Database {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    
@Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                
                // Product productA = new Product("Apple", 2020, 120.0, "");
                // Product productB = new Product("Banana", 2020, 230.0, "");
                // logger.info("insert data: " + productRepository.save(productA));
                // logger.info("insert data: " + productRepository.save(productB));

            } 
        };
    }
}
