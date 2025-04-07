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
import java.util.concurrent.*;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ThreadPoolConfig threadPoolConfig;
    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    private ExecutorService executor = Executors.newCachedThreadPool();

    public ProdutoResponseDTO addProducts(ProductSaveDTO productSaveDTO) throws ExecutionException, InterruptedException {
        Callable<ProdutoResponseDTO> newProdutoResponseDTO = () -> {
            Product newProduct = productRepository.addProduct(productSaveDTO);

            ProdutoResponseDTO produtoResponseDTO  = new ProdutoResponseDTO();
            produtoResponseDTO.setId(newProduct.getId());
            produtoResponseDTO.setName(newProduct.getName());
            produtoResponseDTO.setMessage("Produto cadastrado com sucesso.");

            return produtoResponseDTO;
        };
        Future<ProdutoResponseDTO> newProduct = executor.submit(newProdutoResponseDTO);
        return newProduct.get();

    }

    public UpdateStockResponseDTO updateStock(UpdateStockDTO updateStockDTO, Long productId) throws ExecutionException, InterruptedException {
        Callable<UpdateStockResponseDTO> newProdutoResponseDTO = () -> {
            return productRepository.updateStock(updateStockDTO, productId);
        };
        Future<UpdateStockResponseDTO> productResponse = executor.submit(newProdutoResponseDTO);
        return productResponse.get();
    }


}