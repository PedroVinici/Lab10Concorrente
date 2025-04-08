package com.example.demo.util;

import com.example.demo.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.*;

@Component
public class LoadTestGenerator {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public LoadTestGenerator(RestTemplate restTemplate, @Value("${server.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void startLoadTest() throws InterruptedException {
        //ExecutorService executorService = Executors.newFixedThreadPool(50);  // Ajuste o número de threads conforme necessário

        RequestSimulator requestSimulator = new RequestSimulator(1);
        System.out.println("\n-----------------------\nSimulando caso de Altas taxas de CONSULTAS\n-----------------------\n");
        requestSimulator.simulaRequisicoesConsulta();
        System.out.println("\n-----------------------\nSimulando caso de Altas taxas de COMPRA\n-----------------------\n");
        requestSimulator.simulaRequisicoesCompra();
        System.out.println("\n-----------------------\nSimulando caso de Altas taxas de ATUALIZAÇÃO\n-----------------------\n");
        requestSimulator.simulaRequisicoesAtulizacoes();
        System.out.println("\n-----------------------\nSimulando caso de Altas taxas de RELATORIOS\n-----------------------\n");
        requestSimulator.simulaRequisicoesRelatorio();


    }

    // Classe interna para simular uma sequência de requisições
    private class RequestSimulator {

        private final int userId;

        public RequestSimulator(int userId) {
            this.userId = userId;
        }


        public void simulaRequisicoesConsulta() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(20); // Pool de 10 threads para simular concorrência
            Random random = new Random();

            // Submete várias tarefas de consulta de produto
            for (int i = 0; i < 100; i++) {
                int verfic = random.nextInt(2);
                executorService.submit(() -> {
                    if(verfic == 0){
                        consultaProduto();
                    } else {
                        cadastroProduto();
                    }
                });
            }

            // Finaliza o ExecutorService
            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        }

        public void simulaRequisicoesRelatorio() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(20); // Pool de 10 threads para simular concorrência
            Random random = new Random();

            // Submete várias tarefas de consulta de produto
            for (int i = 0; i < 100; i++) {
                int verfic = random.nextInt(4);
                executorService.submit(() -> {
                    if(verfic == 0){
                        atualizaEstoque();
                    } else {
                        geraRelatorio();
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        }

        public void simulaRequisicoesCompra() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            cadastroProduto(1L);

            // Submete várias tarefas de consulta de produto
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                     compraProduto(2L);
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        }

        public void simulaRequisicoesAtulizacoes() throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(50); // Pool de 10 threads para simular concorrência

            // Submete várias tarefas de consulta de produto
            for (int i = 0; i < 50; i++) {
                executorService.submit(() -> {
                    atualizaEstoque();
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        }

        // Simula o cadastro de um novo produto (POST /products)
        private void cadastroProduto() {
            Random random = new Random();
            Long idNew = random.nextLong(1,21);
            String url = baseUrl + "/products";  // Rota de cadastro de produto
            ProductSaveDTO productSaveDTO = new ProductSaveDTO().builder().id(idNew).name("Teclado").price(499.11).quantity(30).build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Criar a entidade com o produto DTO e os cabeçalhos
            HttpEntity<ProductSaveDTO> entity = new HttpEntity<>(productSaveDTO, headers);

            try {
                ResponseEntity<ProductSaveDTO> response = restTemplate.postForEntity(url, entity, ProductSaveDTO.class);


                if (response.getStatusCodeValue() == 201) {
                    System.out.println("Usuário " + userId + " - Cadastro de Produto: Sucesso");
                    System.out.println("Resposta: " + response.getBody());  // Exibe o corpo da resposta (o DTO)
                } else {
                    System.out.println("Usuário " + userId + " - Erro no Cadastro de Produto: " + response.getStatusCode());
                    System.out.println("Resposta: " + response.getBody());
                }
            } catch (HttpClientErrorException.Conflict e) {
                System.out.println("Usuário " + userId + " - Produto já existente: " + e.getMessage());
            } catch (HttpClientErrorException e) {
                System.out.println("Usuário " + userId + " - Erro no Cadastro de Produto: " + e.getMessage());
            }
        }

        private void cadastroProduto(Long id) {
            Random random = new Random();
            String url = baseUrl + "/products";  // Rota de cadastro de produto
            ProductSaveDTO productSaveDTO = new ProductSaveDTO().builder().id(id).name("Teclado").price(499.11).quantity(30).build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ProductSaveDTO> entity = new HttpEntity<>(productSaveDTO, headers);

            try {
                ResponseEntity<ProductSaveDTO> response = restTemplate.postForEntity(url, entity, ProductSaveDTO.class);


                if (response.getStatusCodeValue() == 201) {
                    System.out.println("Usuário " + userId + " - Cadastro de Produto: Sucesso");
                    System.out.println("Resposta: " + response.getBody());  // Exibe o corpo da resposta (o DTO)
                } else {
                    System.out.println("Usuário " + userId + " - Erro no Cadastro de Produto: " + response.getStatusCode());
                    System.out.println("Resposta: " + response.getBody());
                }
            } catch (HttpClientErrorException.Conflict e) {
                System.out.println("Usuário " + userId + " - Produto já existente: " + e.getMessage());
            } catch (HttpClientErrorException e) {
                System.out.println(e.getMessage());
            }
        }


        private void consultaProduto() {
            String url = baseUrl + "/products";
            try {
                ResponseEntity<ProductReturnDTO[]> response = restTemplate.getForEntity(url, ProductReturnDTO[].class);

                System.out.println("Usuário " + userId + " - Consulta Produto: " + response.getStatusCode());

                ProductReturnDTO[] products = response.getBody();
                if (products != null && products.length > 0) {
                    for (ProductReturnDTO product : products) {
                        System.out.println("Produto ID: " + product.getId());
                        System.out.println("Nome: " + product.getName());
                        System.out.println("Preço: " + product.getPrice());
                        System.out.println("Quantidade: " + product.getQuantity());
                        System.out.println("-----------------------------------");
                    }
                    System.out.println("Produto encontrado: " + products[0]);
                } else {
                    System.out.println("Nenhum produto encontrado.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void consultaProdutoPorId() {
            Random random = new Random();
            Long idNew = random.nextLong(1,21);
            String url = baseUrl + "/products/" + idNew;
            try {
                ResponseEntity<ProductReturnDTO> response = restTemplate.getForEntity(url, ProductReturnDTO.class);

                System.out.println("Usuário " + userId + " - Consulta Produto: " + response.getStatusCode());

                ProductReturnDTO product = response.getBody();
                if (product != null) {
                    System.out.println("Produto com id: " + idNew + "encontrado: " + product);
                        System.out.println("Produto ID: " + product.getId());
                        System.out.println("Nome: " + product.getName());
                        System.out.println("Preço: " + product.getPrice());
                        System.out.println("Quantidade: " + product.getQuantity());
                        System.out.println("-----------------------------------");


                } else {
                    System.out.println("Nenhum produto encontrado, no Produto com id:.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        private void compraProduto() {
            String url = baseUrl + "/purchase";
            Random random = new Random();
            Long idNew = random.nextLong(1,21);
            ProductPurchaseDTO request = new ProductPurchaseDTO(idNew, 2);  // Produto e quantidade
            try {
                ResponseEntity<PurchaseDTO> response = restTemplate.postForEntity(url, request, PurchaseDTO.class);
                System.out.println("Usuário " + userId + " - Compra Produto: " + response.getStatusCode());
                System.out.println("Resposta da compra de id: " + idNew);
                PurchaseDTO compra = response.getBody();
                if (compra != null) {
                    System.out.println("Message: " + compra.getMessage());
                    System.out.println("Produto: " + compra.getProduto().toString());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void compraProduto(Long id) {
            String url = baseUrl + "/purchase";
            ProductPurchaseDTO request = new ProductPurchaseDTO(id, 2);  // Produto e quantidade
            try {
                ResponseEntity<PurchaseDTO> response = restTemplate.postForEntity(url, request, PurchaseDTO.class);
                System.out.println("Usuário " + userId + " - Compra Produto: " + response.getStatusCode());
                System.out.println("Resposta da compra de id: " + id);
                PurchaseDTO compra = response.getBody();
                if (compra != null) {
                    System.out.println("Message: " + compra.getMessage());
                    System.out.println("Produto: " + compra.getProduto().toString());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void atualizaEstoque() {
            Random random = new Random();
            Long idNew = random.nextLong(1,21);
            String url = baseUrl + "/products/" + idNew + "/stock";
            UpdateStockDTO request = new UpdateStockDTO(50);  // Quantidade de estoque a ser atualizada
            try {
                restTemplate.put(url, request);
                System.out.println("Usuário " + userId + " - Estoque Atualizado.");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void geraRelatorio() {
            String url = baseUrl + "/sales/report";

            try {
                ResponseEntity<SaleDTO> response = restTemplate.getForEntity(url, SaleDTO.class);
                System.out.println("Usuário " + userId + " - Relatório Gerado: " + response.getStatusCode());
                System.out.println("Resposta: ");
                SaleDTO saleDTO = response.getBody();
                if (saleDTO != null) {
                    System.out.println("Total de vendas: " + saleDTO.getTotalSales());
                    System.out.println("Produtos: " + saleDTO.getProducts());
                    System.out.println("-----------------------------------");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
