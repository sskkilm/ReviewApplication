package com.example.reviewapplication.domain;

import org.springframework.stereotype.Component;

@Component
public class ProductManager {
    public void updateProduct(Product product, Review review) {
        Float totalScore = product.calculateTotalScore() + review.getScore();
        product.increaseReviewCount();

        product.updateScore(totalScore);
    }
}
