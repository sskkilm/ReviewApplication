package com.example.reviewapplication.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private String content;

    private String imageUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Review createReview(Product product, Long userId, Integer score, String content) {
        return Review.builder()
                .product(product)
                .userId(userId)
                .score(score)
                .content(content)
                .build();
    }

    public void updateImageUrl(MultipartFile file) {
        this.imageUrl = file.getOriginalFilename();
    }
}
