package com.example.reviewapplication;

import com.example.reviewapplication.application.ProductRepository;
import com.example.reviewapplication.application.ReviewService;
import com.example.reviewapplication.domain.Product;
import com.example.reviewapplication.dto.ReviewCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CreateReviewConcurrencyTest {

    @Autowired
    ReviewService reviewService;

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
    void 멀티_스레드_환경에서_100명의_사용자가_동시에_리뷰를_작성한다() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long productId = 1L;
        MultipartFile file = null;
        List<ReviewCreateRequest> requests = LongStream.range(0, threadCount).mapToObj(
                userId -> ReviewCreateRequest.builder()
                        .userId(userId)
                        .score(3)
                        .content("content")
                        .build()
        ).toList();

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    reviewService.createReview(productId, file, requests.get(finalI));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        Product product = productRepository.findById(1L).get();
        assertEquals(100, product.getReviewCount());
    }
}