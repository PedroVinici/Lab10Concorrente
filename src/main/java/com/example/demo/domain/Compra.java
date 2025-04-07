package com.example.demo.domain;

import com.example.demo.dto.PurchaseDTO;
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
public class Compra {
    private Long id;
    private int quantidade;
    private Product produto;
}
