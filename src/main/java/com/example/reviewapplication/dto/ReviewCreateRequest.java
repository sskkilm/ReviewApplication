package com.example.reviewapplication.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewCreateRequest {

    @NotNull
    private Long userId;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer score;

    @NotNull
    private String content;

}
