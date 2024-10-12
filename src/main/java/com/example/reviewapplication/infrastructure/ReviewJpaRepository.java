package com.example.reviewapplication.infrastructure;

import com.example.reviewapplication.domain.Product;
import com.example.reviewapplication.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    boolean existsByProductAndUserId(Product product, Long userId);

}
