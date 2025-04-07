package com.example.demo.controller;

import com.example.demo.domain.Product;
import com.example.demo.dto.*;
import com.example.demo.service.CompraService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@RestController
public class ECommerceController {
    @Autowired
    ProductService productService;
    @Autowired
    CompraService compraService;


    @PostMapping("/products")
    public ResponseEntity<ProdutoResponseDTO> addProduct(@RequestBody ProductSaveDTO productSaveDTO) throws ExecutionException, InterruptedException {
        ProdutoResponseDTO newProduct = productService.addProducts(productSaveDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}/stock")
    public ResponseEntity<UpdateStockResponseDTO> updateStock(@PathVariable Long id, @RequestBody UpdateStockDTO updateStockDTO) throws ExecutionException, InterruptedException {
        UpdateStockResponseDTO response = productService.updateStock(updateStockDTO, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseDTO> purchase(@RequestBody ProductPurchaseDTO productPurchaseDTO) throws ExecutionException, InterruptedException {
        PurchaseDTO response = compraService.purchaseProduct(productPurchaseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductReturnDTO> getProductById(@PathVariable Long id) throws ExecutionException, InterruptedException {
        ProductReturnDTO product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductReturnDTO>> getAllProducts() {
        ConcurrentHashMap<Long, Product> products = productService.getAllProducts();

        List<ProductReturnDTO> productList = products.values()
                .parallelStream()
                .map(product -> new ProductReturnDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity()))
                .toList();

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/sales/report")
    public ResponseEntity<SaleDTO> salesReport() throws ExecutionException, InterruptedException {
        SaleDTO response = compraService.getRelatorio();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
