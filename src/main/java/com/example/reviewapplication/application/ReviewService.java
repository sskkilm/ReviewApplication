package com.example.reviewapplication.application;

import com.example.reviewapplication.domain.Product;
import com.example.reviewapplication.domain.ProductManager;
import com.example.reviewapplication.domain.Review;
import com.example.reviewapplication.dto.ReviewCreateRequest;
import com.example.reviewapplication.dto.ReviewDto;
import com.example.reviewapplication.dto.ReviewPagingResult;
import com.example.reviewapplication.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final S3Repository s3Repository;
    private final ProductManager productManager;

    @Transactional
    public Review createReview(Long productId, MultipartFile file, ReviewCreateRequest reviewCreateRequest) {
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new AppException("product not found"));

        if (reviewRepository.existByProductAndUserId(product, reviewCreateRequest.getUserId())) {
            throw new AppException("user already exist");
        }

        Review review = Review.createReview(
                product, reviewCreateRequest.getUserId(), reviewCreateRequest.getScore(), reviewCreateRequest.getContent()
        );

        productManager.updateProduct(product, review);

        if (file != null) {
            review.updateImageUrl(file);
            s3Repository.save(file);
        }

        return reviewRepository.save(review);
    }

    @Cacheable(value = "ReviewPagingResult", key = "#productId + ':' + #cursor + ':' + #size")
    public ReviewPagingResult getReviewList(Long productId, Long cursor, long size) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("product not found"));

        List<ReviewDto> reviews = reviewRepository.findReviewsByCursor(productId, cursor, size)
                .stream().map(ReviewDto::toDto).toList();

        Long lastCursor = 0L;
        if (!reviews.isEmpty()) {
            lastCursor = reviews.get(reviews.size() - 1).getId();
        }

        return new ReviewPagingResult(
                product.getReviewCount(), product.getScore(), lastCursor, reviews
        );
    }

}
