package com.example.demo.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("message")
    private String message;
    @JsonProperty("produto")
    private ProductPurchaseResponseDTO produto;
}
