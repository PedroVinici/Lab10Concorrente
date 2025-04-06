package com.example.demo.repository;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductSaveDTO;
import com.example.demo.dto.UpdateStockDTO;
import com.example.demo.dto.UpdateStockResponseDTO;
import com.example.demo.exception.ProductNotExistException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();

    public Product addProduct(ProductSaveDTO productSaveDTO) {

        Product newProduct = new Product(
                productSaveDTO.getId(),
                productSaveDTO.getName(),
                productSaveDTO.getPrice(),
                productSaveDTO.getQuanity()
        );

        Product existing = products.putIfAbsent(newProduct.getId(), newProduct);

        if (existing != null) {
            throw new ResourceAlreadyExistsException("Produto com o ID " + productSaveDTO.getId() + " já existe!");
        }

        return newProduct;
    }

    public UpdateStockResponseDTO updateStock(UpdateStockDTO updateStockDTO, Long productId) {
        int quantityToAdd = updateStockDTO.getQuantity();

        Product updatedProduct = products.computeIfPresent(productId, (id, product) -> {
            product.setQuantity(quantityToAdd);
            return product;
        });

        if (updatedProduct == null) {
            throw new ProductNotExistException();
        }

        return new UpdateStockResponseDTO(
                ("Estoque atualizado."),
                updatedProduct.getQuantity()
        );
    }
}
