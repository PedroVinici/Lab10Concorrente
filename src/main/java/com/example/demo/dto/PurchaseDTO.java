package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseDTO {
    private String message;
    private ProductPurchaseResponseDTO produto;
}
