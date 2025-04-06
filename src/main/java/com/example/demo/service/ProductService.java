package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductSaveDTO;
import com.example.demo.dto.ProdutoResponseDTO;
import com.example.demo.dto.UpdateStockDTO;
import com.example.demo.dto.UpdateStockResponseDTO;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ThreadPoolConfig threadPoolConfig;
    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    public ProdutoResponseDTO addProducts(ProductSaveDTO productSaveDTO){
        Product newProduct = productRepository.addProduct(productSaveDTO);

        ProdutoResponseDTO produtoResponseDTO  = new ProdutoResponseDTO();
        produtoResponseDTO.setId(newProduct.getId());
        produtoResponseDTO.setName(newProduct.getName());
        produtoResponseDTO.setMessage("Produto cadastrado com sucesso.");

        return produtoResponseDTO;
    }

    public CompletableFuture<UpdateStockResponseDTO> updateStock(UpdateStockDTO updateStockDTO, Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            return productRepository.updateStock(updateStockDTO, productId);
        }, taskExecutor);
    }


}