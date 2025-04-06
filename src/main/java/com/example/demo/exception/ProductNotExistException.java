package com.example.demo.exception;

public class ProductNotExistException extends RuntimeException {

    public ProductNotExistException() {
        super("Produto não encontrado.");
    }
}
