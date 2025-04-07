package com.example.demo.service;

import com.example.demo.domain.Product;
<<<<<<< HEAD
import com.example.demo.dto.*;
=======
import com.example.demo.dto.ProductReturnDTO;
import com.example.demo.dto.ProductSaveDTO;
import com.example.demo.dto.ProdutoResponseDTO;
import com.example.demo.dto.UpdateStockDTO;
import com.example.demo.dto.UpdateStockResponseDTO;
>>>>>>> 6e5197525b769eaa34e57914b6bcfbf0aebab8c4
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

    //Meus chegados, essa é a opção utilizando o Callable que Geovanni ensinou
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
    // Essa é a opção que o GPT sugeriu
    public CompletableFuture<UpdateStockResponseDTO> updateStock(UpdateStockDTO updateStockDTO, Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            return productRepository.updateStock(updateStockDTO, productId);
        }, taskExecutor);
    }

    public CompletableFuture<PurchaseDTO> purchaseProduct(ProductPurchaseDTO productPurchaseDTO) throws ExecutionException, InterruptedException {
        Callable<PurchaseDTO> newPurchaseDTO = () -> {
            Product product = productRepository.get
        }
    }

    public ProductReturnDTO getProductById(Long id) {
        return productRepository.getProductById(id);
    }

    public HashMap<Long, Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
}