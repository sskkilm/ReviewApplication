package com.example.reviewapplication.application;

import com.example.reviewapplication.domain.Product;
import com.example.reviewapplication.domain.ProductManager;
import com.example.reviewapplication.domain.Review;
import com.example.reviewapplication.dto.ReviewCreateRequest;
import com.example.reviewapplication.dto.ReviewPagingResult;
import com.example.reviewapplication.exception.AppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductManager productManager;

    @Mock
    S3Repository s3Repository;

    @InjectMocks
    ReviewService reviewService;

    @Test
    void 리뷰는_존재하는_상품에만_작성할_수_있다() {
        //given
        MultipartFile file = new MockMultipartFile(
                "mockFile", "imageUrl", null, (byte[]) null
        );
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .userId(1L)
                .score(5)
                .content("content")
                .build();
        given(productRepository.findByIdForUpdate(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(AppException.class,
                //when
                () -> reviewService.createReview(1L, file, request)
        );
    }

    @Test
    void 유저는_하나의_상품에_대해_하나의_리뷰만_작성이_가능하다() {
        //given
        MultipartFile file = new MockMultipartFile(
                "mockFile", "imageUrl", null, (byte[]) null
        );
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .userId(1L)
                .score(5)
                .content("content")
                .build();
        Product product = Product.builder()
                .id(1L)
                .reviewCount(0L)
                .score(0.0F)
                .build();
        given(productRepository.findByIdForUpdate(1L))
                .willReturn(Optional.of(product));
        given(reviewRepository.existByProductAndUserId(product, 1L))
                .willReturn(true);

        //then
        assertThrows(AppException.class,
                //when
                () -> reviewService.createReview(1L, file, request)
        );
    }

    @Test
    void 리뷰_생성_성공() {
        //given
        MultipartFile file = new MockMultipartFile(
                "mockFile", "imageUrl", null, (byte[]) null
        );
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .userId(1L)
                .score(5)
                .content("content")
                .build();
        Product product = Product.builder()
                .id(1L)
                .reviewCount(0L)
                .score(0.0F)
                .build();
        given(productRepository.findByIdForUpdate(1L))
                .willReturn(Optional.of(product));
        given(reviewRepository.existByProductAndUserId(product, 1L))
                .willReturn(false);
        given(reviewRepository.save(any(Review.class)))
                .willReturn(
                        Review.builder()
                                .product(product)
                                .userId(request.getUserId())
                                .score(request.getScore())
                                .content(request.getContent())
                                .imageUrl(file.getOriginalFilename())
                                .build()
                );

        //when
        Review review = reviewService.createReview(1L, file, request);

        //then
        assertEquals(1L, review.getProduct().getId());
        assertEquals(1L, review.getUserId());
        assertEquals(5, review.getScore());
        assertEquals("content", review.getContent());
        assertEquals("imageUrl", review.getImageUrl());
    }

    @Test
    void 존재하지_않는_상품의_리뷰를_조회할_수_없다() {
        //given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(AppException.class,
                //when
                () -> reviewService.getReviewList(1L, null, 10));
    }

    @Test
    void 리뷰가_없는_상품은_커서가_0으로_설정된다() {
        Product product = Product.builder()
                .id(1L)
                .reviewCount(3L)
                .score(4.5F)
                .build();
        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));
        given(reviewRepository.findReviewsByCursor(1L, null, 1))
                .willReturn(List.of());

        //when
        ReviewPagingResult reviewPagingResult = reviewService.getReviewList(1L, null, 1);

        //then
        assertEquals(0L, reviewPagingResult.getCursor());
    }

    @Test
    void 리뷰_조회_성공() {
        //given
        Product product = Product.builder()
                .id(1L)
                .reviewCount(3L)
                .score(4.5F)
                .build();
        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));
        given(reviewRepository.findReviewsByCursor(1L, null, 1))
                .willReturn(List.of(
                        Review.builder()
                                .id(15L)
                                .build()
                ));

        //when
        ReviewPagingResult reviewPagingResult = reviewService.getReviewList(1L, null, 1);

        //then
        assertEquals(3L, reviewPagingResult.getTotalCount());
        assertEquals(4.5F, reviewPagingResult.getScore());
        assertEquals(15L, reviewPagingResult.getCursor());
        assertEquals(1, reviewPagingResult.getReviews().size());
    }
}