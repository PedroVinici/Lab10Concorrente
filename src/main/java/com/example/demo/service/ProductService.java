package com.example.demo.service;

import com.example.demo.domain.Product;

import com.example.demo.dto.*;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ProdutoResponseDTO addProducts(ProductSaveDTO productSaveDTO) throws ExecutionException, InterruptedException {
        Product newProduct = productRepository.addProduct(productSaveDTO);

        synchronized (newProduct) {
            ProdutoResponseDTO produtoResponseDTO = ProdutoResponseDTO.builder()
                    .id(newProduct.getId())
                    .name(newProduct.getName())
                    .message("Produto cadastrado com sucesso.")
                    .build();

            return produtoResponseDTO;
        }
    }

    public synchronized UpdateStockResponseDTO updateStock(UpdateStockDTO updateStockDTO, Long productId) throws ExecutionException, InterruptedException {
        return productRepository.updateStock(updateStockDTO, productId);
    }

    public ProductReturnDTO getProductById(Long id) throws ExecutionException, InterruptedException {
        Product product = productRepository.getProductById(id);
        ProductReturnDTO productReturnDTO = ProductReturnDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();

        return productReturnDTO;
    }

    public ConcurrentHashMap<Long, Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
}