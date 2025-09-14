package com.example.pactconsumer.client;

import com.example.pactconsumer.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductService(RestTemplate restTemplate, @Value("${provider.baseUrl}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Product getProduct(Long id) {
        return restTemplate.getForObject(baseUrl + "/products/" + id, Product.class);
    }
}
