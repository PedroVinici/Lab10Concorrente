package com.example.demo.service;

import com.example.demo.domain.Compra;
import com.example.demo.domain.Product;
import com.example.demo.dto.*;
import com.example.demo.exception.InsufficientQuantityException;
import com.example.demo.repository.CompraRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CompraService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CompraRepository compraRepository;

    private ExecutorService executor = Executors.newCachedThreadPool();


    public PurchaseDTO purchaseProduct(ProductPurchaseDTO productPurchaseDTO) throws ExecutionException, InterruptedException {
        Product product = productRepository.getProductById(productPurchaseDTO.getId());
        if (productPurchaseDTO.getQuantity() > product.getQuantity()) {
            throw new InsufficientQuantityException(product.getQuantity());
        }

        synchronized (product) {
            compraRepository.addCompra(product, productPurchaseDTO.getQuantity());
            product.setQuantity(product.getQuantity() - productPurchaseDTO.getQuantity());

            ProductPurchaseResponseDTO productPurchaseResponseDTO = ProductPurchaseResponseDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .remainingStock(product.getQuantity())
                    .build();

            PurchaseDTO purchaseDTO = PurchaseDTO.builder()
                    .message("Compra realizada com sucesso.")
                    .produto(productPurchaseResponseDTO)
                    .build();

            return purchaseDTO;
        }
    }

    public SaleDTO getRelatorio() throws ExecutionException, InterruptedException {
        ConcurrentHashMap<Long, Compra> compras = compraRepository.getCompras();

        ConcurrentHashMap<Long, Product> products = productRepository.getAllProducts();
        ConcurrentHashMap<Long, ProductCompradoDTO> compraProdutos = new ConcurrentHashMap<>();

        synchronized (compraProdutos) {
            compras.forEach((idCompra, compra) -> {
                Product produto = products.get(compra.getProduto().getId());

                if (produto != null) {
                    compraProdutos.merge(
                            produto.getId(),
                            new ProductCompradoDTO(produto.getId(), produto.getName(), compra.getQuantidade()),
                            (existente, novo) -> {
                                existente.setQuantitySold(existente.getQuantitySold() + novo.getQuantitySold());
                                return existente;
                            }
                    );
                }
            });

            SaleDTO saleDTO = SaleDTO.builder()
                    .totalSales(compras.size())
                    .products(new ArrayList<>(compraProdutos.values()))
                    .build();

            return saleDTO;

        }




    }
}
