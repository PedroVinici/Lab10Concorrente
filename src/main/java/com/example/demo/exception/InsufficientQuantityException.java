package com.example.demo.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(Integer quantity) {
        super("Estoque insuficiente. Quantidade disponível: " + quantity);
    }
}
