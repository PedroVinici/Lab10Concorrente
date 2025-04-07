package com.example.demo.repository;

import com.example.demo.domain.Compra;
import com.example.demo.domain.Product;
import com.example.demo.dto.*;
import com.example.demo.exception.ProductNotExistException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CompraRepository {

    @Getter
    private ConcurrentHashMap<Long, Compra> compras = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong(1);

    public Compra addCompra(Product product, int quantidade) {
        long newId = idCounter.getAndIncrement();


        Compra newCompra = new Compra(
                newId,
                quantidade,
                product
        );

        compras.putIfAbsent(newId, newCompra);

        return newCompra;
    }

}
