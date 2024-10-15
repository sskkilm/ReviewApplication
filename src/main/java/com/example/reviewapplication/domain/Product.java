package com.example.reviewapplication.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Long reviewCount;

    @Column(nullable = false)
    private Float score;

    public Float calculateTotalScore() {
        return score * reviewCount;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void updateScore(Float totalScore) {
        this.score = totalScore / reviewCount;
    }
}
