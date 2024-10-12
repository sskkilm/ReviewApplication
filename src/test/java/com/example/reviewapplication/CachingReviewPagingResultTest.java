package com.example.reviewapplication;

import com.example.reviewapplication.application.ProductRepository;
import com.example.reviewapplication.application.ReviewRepository;
import com.example.reviewapplication.application.ReviewService;
import com.example.reviewapplication.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CachingReviewPagingResultTest {

    @Autowired
    ReviewService reviewService;

    @SpyBean
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void 상품_데이터_초기화() {
        Product product = Product.builder()
                .reviewCount(0L)
                .score(0.0F)
                .build();
        productRepository.save(product);
    }

    @Test
    void 리뷰를_5번_조회해도_메소드는_1번_실행된다() {
        //given

        //when
        for (int i = 0; i < 5; i++) {
            reviewService.getReviewList(1L, null, 3);
        }

        //then
        verify(reviewRepository, times(1))
                .findReviewsByCursor(1L, null, 3);
    }
}
