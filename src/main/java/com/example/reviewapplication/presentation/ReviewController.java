package com.example.reviewapplication.presentation;

import com.example.reviewapplication.application.ReviewService;
import com.example.reviewapplication.dto.ReviewCreateRequest;
import com.example.reviewapplication.dto.ReviewPagingResult;
import com.example.reviewapplication.exception.AppException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/products/{productId}/reviews")
    public void createReview(
            @PathVariable Long productId,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(name = "data") @Valid ReviewCreateRequest reviewCreateRequest) {
        reviewService.createReview(productId, file, reviewCreateRequest);
    }

    @GetMapping("/products/{productId}/reviews")
    public ReviewPagingResult getReviewList(
            @PathVariable Long productId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") long size
    ) {
        if (cursor != null && cursor <= 0 || size <= 0) {
            throw new AppException("cursor and size must be positive");
        }

        return reviewService.getReviewList(productId, cursor, size);
    }
}
