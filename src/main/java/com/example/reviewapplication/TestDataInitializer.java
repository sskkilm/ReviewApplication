package com.example.reviewapplication;

import com.example.reviewapplication.application.ProductRepository;
import com.example.reviewapplication.domain.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class TestDataInitializer {

    private final ProductRepository productRepository;

    @PostConstruct
    public void testDatainit() {
        for (int i = 0; i < 10; i++) {
            productRepository.save(
                    Product.builder()
                            .reviewCount(0L)
                            .score(0.0F)
                            .build()
            );
        }
    }
}
