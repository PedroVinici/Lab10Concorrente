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
        ExecutorService executorService = Executors.newFixedThreadPool(50);  // Ajuste o número de threads conforme necessário

        // Criando múltiplas threads para simular concorrência
        for (int i = 0; i < 10; i++) {
            executorService.submit(new RequestSimulator(i));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);  // Aguarda o término das threads
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
                    int action = (int) (Math.random() * 6);  // Ação aleatória entre 0 e 3
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
                            cadastroProduto();
                            break;
                        case 4:
                            consultaProdutoPorId();
                            break;
                        case 5:
                            geraRelatorio();
                            break;
                    }
                    // Espera um tempo aleatório entre as requisições
                    Thread.sleep((int) (Math.random() * 2000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Simula o cadastro de um novo produto (POST /products)
        private void cadastroProduto() {
            Random random = new Random();
            Long idNew = random.nextLong(1,21);
            String url = baseUrl + "/products";  // Rota de cadastro de produto
            String requestJson = "{ \"id\": \""+idNew+"\", \"name\": \"Teclado Mecânico\", \"price\": 499.99, \"quantity\": 30 }";
            ProductSaveDTO productSaveDTO = new ProductSaveDTO().builder().id(idNew).name("Teclado").price(499.11).quantity(30).build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Criar a entidade com o produto DTO e os cabeçalhos
            HttpEntity<ProductSaveDTO> entity = new HttpEntity<>(productSaveDTO, headers);

            try {
                // Envia a requisição POST e recebe o retorno como um objeto ProductSaveDTO
                ResponseEntity<ProductSaveDTO> response = restTemplate.postForEntity(url, entity, ProductSaveDTO.class);


                // Verifica a resposta e exibe a resposta completa
                if (response.getStatusCodeValue() == 201) {
                    System.out.println("Usuário " + userId + " - Cadastro de Produto: Sucesso");
                    System.out.println("Resposta: " + response.getBody());  // Exibe o corpo da resposta (o DTO)
                } else {
                    System.out.println("Usuário " + userId + " - Erro no Cadastro de Produto: " + response.getStatusCode());
                    System.out.println("Resposta: " + response.getBody());
                }
            } catch (HttpClientErrorException.Conflict e) {
                // Tratamento para quando o produto já existe (409 Conflict)
                System.out.println("Usuário " + userId + " - Produto já existente: " + e.getMessage());
            } catch (HttpClientErrorException e) {
                // Tratamento para outros erros
                System.out.println("Usuário " + userId + " - Erro no Cadastro de Produto: " + e.getMessage());
            }
        }


        private void consultaProduto() {
            String url = baseUrl + "/products";
            try {
                // Altere para ProductReturnDTO[] para lidar com um array de produtos
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
                System.out.println("Usuário " + userId + " - Erro na consulta de produto.");
            }
        }

        private void consultaProdutoPorId() {
            Random random = new Random();
            Long idNew = random.nextLong(1,21);
            String url = baseUrl + "/products/" + idNew;
            try {
                // Altere para ProductReturnDTO[] para lidar com um array de produtos
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
                System.out.println("Usuário " + userId + " - Erro na consulta de produto por id.");
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
                System.out.println("Usuário " + userId + " - Erro na compra de produto.");
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
                System.out.println("Usuário " + userId + " - Erro na atualização de estoque.");
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
                System.out.println("Usuário " + userId + " - Erro na geração de relatório.");
            }
        }
    }
}
