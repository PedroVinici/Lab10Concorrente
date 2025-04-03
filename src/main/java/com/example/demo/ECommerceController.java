package com.example.demo;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class ECommerceController {
    @Autowired
    ProductService productService; 
    
    @GetMapping("/product")
    public ResponseEntity<List<Product>> GetProduct() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }
}
