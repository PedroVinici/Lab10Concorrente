package com.example.demo.controller;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductSaveDTO;
import com.example.demo.dto.ProdutoResponseDTO;
import com.example.demo.dto.UpdateStockDTO;
import com.example.demo.dto.UpdateStockResponseDTO;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ECommerceController {
    @Autowired
    ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProdutoResponseDTO> addProduct(@RequestBody ProductSaveDTO productSaveDTO) throws ExecutionException, InterruptedException {
        ProdutoResponseDTO newProduct = productService.addProducts(productSaveDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }
    @PutMapping("/products/{id}/stock")
    public ResponseEntity<UpdateStockResponseDTO> updateStock(@PathVariable Long id, @RequestBody UpdateStockDTO updateStockDTO) throws ExecutionException, InterruptedException {
        UpdateStockResponseDTO response = productService.updateStock(updateStockDTO, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
