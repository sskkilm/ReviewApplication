package com.example.reviewapplication.dto;

import com.example.reviewapplication.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewDto implements Serializable {
    private Long id;
    private Long userId;
    private Integer score;
    private String content;
    private String imageUrl;
    private LocalDateTime createAt;

    public static ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .score(review.getScore())
                .content(review.getContent())
                .imageUrl(review.getImageUrl())
                .createAt(review.getCreatedAt())
                .build();
    }
}
