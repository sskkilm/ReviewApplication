package com.example.reviewapplication.infrastructure;

import com.example.reviewapplication.application.ReviewRepository;
import com.example.reviewapplication.domain.Product;
import com.example.reviewapplication.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {


    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewQueryRepository reviewQueryRepository;

    @Override
    public boolean existByProductAndUserId(Product product, Long userId) {
        return reviewJpaRepository.existsByProductAndUserId(product, userId);
    }

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public List<Review> findReviewsByCursor(Long productId, Long cursor, long size) {
        return reviewQueryRepository.findReviewsByCursor(productId, cursor, size);
    }
}
