package com.example.pactconsumer.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.pactconsumer.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "product-provider")
public class ProductServicePactTest {

    @Pact(consumer = "product-consumer")
    public V4Pact createPact(PactBuilder builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return builder
                .usingLegacyDsl()
                .given("a product with ID 1 exists")
                .uponReceiving("a request for product 1")
                .path("/products/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body("{\"id\": 1, \"name\": \"Product 1\", \"price\": 10.99}")
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void testGetProduct(MockServer mockServer) {
        // Arrange
        RestTemplate restTemplate = new RestTemplate();
        ProductService productService = new ProductService(restTemplate, mockServer.getUrl());

        // Act
        Product product = productService.getProduct(1L);

        // Assert
        assertEquals(1L, product.getId());
        assertEquals("Product 1", product.getName());
        assertEquals(10.99, product.getPrice());
    }
}