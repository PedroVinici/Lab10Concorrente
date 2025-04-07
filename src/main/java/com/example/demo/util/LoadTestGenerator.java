package com.example.demo.util;

import com.example.demo.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.web.client.RestTemplate;

@Component
public class LoadTestGenerator {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public LoadTestGenerator(RestTemplate restTemplate, @Value("${server.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void startLoadTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        // Criando múltiplas threads para simular concorrência
        for (int i = 0; i < 10; i++) {
            executorService.submit(new RequestSimulator(i));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    // Classe interna para simular uma sequência de requisições
    private class RequestSimulator implements Runnable {

        private final int userId;

        public RequestSimulator(int userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    int action = (int) (Math.random() * 4);

                    switch (action) {
                        case 0:
                            consultaProduto();
                            break;
                        case 1:
                            compraProduto();
                            break;
                        case 2:
                            atualizaEstoque();
                            break;
                        case 3:
                            geraRelatorio();
                            break;
                    }
                    Thread.sleep((int) (Math.random() * 2000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void consultaProduto() {
            Random random = new Random();
            int idRandom = random.nextInt(1000,100000);
            String productId = Integer.toString(idRandom);
            String url = baseUrl + "/products/" + productId;
            try {
                ResponseEntity<ProductReturnDTO> response = restTemplate.getForEntity(url, ProductReturnDTO.class);
                System.out.println("Usuário " + userId + " - Consulta Produto: " + response.getStatusCode());
            } catch (Exception e) {
                System.out.println("Usuário " + userId + " - Erro na consulta de produto.");
            }
        }

        private void compraProduto() {
            String url = baseUrl + "/purchase";
            ProductPurchaseDTO request = new ProductPurchaseDTO(1234L, 2);  // Produto e quantidade
            try {
                ResponseEntity<PurchaseDTO> response = restTemplate.postForEntity(url, request, PurchaseDTO.class);
                System.out.println("Usuário " + userId + " - Compra Produto: " + response.getStatusCode());
            } catch (Exception e) {
                System.out.println("Usuário " + userId + " - Erro na compra de produto.");
            }
        }

        private void atualizaEstoque() {
            String productId = "1234";  // Ou use um valor aleatório
            String url = baseUrl + "/products/" + productId + "/stock";
            UpdateStockDTO request = new UpdateStockDTO(50);  // Quantidade de estoque a ser atualizada
            try {
                restTemplate.put(url, request);
                System.out.println("Usuário " + userId + " - Estoque Atualizado.");
            } catch (Exception e) {
                System.out.println("Usuário " + userId + " - Erro na atualização de estoque.");
            }
        }

        private void geraRelatorio() {
            String url = baseUrl + "/sales/report";
            try {
                ResponseEntity<SaleDTO> response = restTemplate.getForEntity(url, SaleDTO.class);
                System.out.println("Usuário " + userId + " - Relatório Gerado: " + response.getStatusCode());
            } catch (Exception e) {
                System.out.println("Usuário " + userId + " - Erro na geração de relatório.");
            }
        }
    }
}