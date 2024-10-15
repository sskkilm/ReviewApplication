package com.example.reviewapplication.application;

import com.example.reviewapplication.domain.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);

    Product save(Product product);

    Optional<Product> findByIdForUpdate(Long id);
}
