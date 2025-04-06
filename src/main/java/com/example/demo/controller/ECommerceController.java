package com.example.demo.controller;

import com.example.demo.domain.Product;
import com.example.demo.dto.*;
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

    //Meus chegados, essa é a opção utilizando o Callable que Geovanni ensinou
    @PostMapping("/products")
    public ResponseEntity<ProdutoResponseDTO> addProduct(@RequestBody ProductSaveDTO productSaveDTO) throws ExecutionException, InterruptedException {
        System.out.println("INSERINDO PRODUTOO!!!!!!!");
        ProdutoResponseDTO newProduct = productService.addProducts(productSaveDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }
    // Essa é a opção que o GPT sugeriu
    @PutMapping("/products/{id}/stock")
    public ResponseEntity<UpdateStockResponseDTO> updateStock(@PathVariable Long id, @RequestBody UpdateStockDTO updateStockDTO) {
        System.out.println("ATUALIZANDO STOOOOQUE!!!!");
        UpdateStockResponseDTO response = productService.updateStock(updateStockDTO, id).join();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/products/purchase")
    public ResponseEntity<PurchaseDTO> purchase(@RequestBody ProductPurchaseDTO productPurchaseDTO) {
        PurchaseDTO response = productService.purchaseProduct(productPurchaseDTO).join();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
