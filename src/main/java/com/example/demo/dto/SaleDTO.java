package com.example.demo.dto;

import com.example.demo.domain.Product;
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
    private Integer totalSales;
    private List<ProductCompradoDTO> products;
}
