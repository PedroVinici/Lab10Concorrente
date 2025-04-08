package com.example.demo.dto;

import com.example.demo.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SaleDTO {
    @JsonProperty("totalSales")
    private Integer totalSales;
    @JsonProperty("products")
    private List<ProductCompradoDTO> products;

    @Override
    public String toString() {
        return "SaleDTO{" +
                "totalSales=" + totalSales +
                ", products=" + products +
                '}';
    }
}
