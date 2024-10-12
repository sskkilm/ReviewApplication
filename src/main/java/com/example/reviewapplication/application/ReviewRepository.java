package com.example.reviewapplication.application;

import com.example.reviewapplication.domain.Product;
import com.example.reviewapplication.domain.Review;

import java.util.List;

public interface ReviewRepository {

    boolean existByProductAndUserId(Product product, Long userId);

    Review save(Review review);

    List<Review> findReviewsByCursor(Long productId, Long cursor, long size);
}
