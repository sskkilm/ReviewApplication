package com.example.reviewapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewPagingResult implements Serializable {
    private Long totalCount;
    private Float score;
    private Long cursor;
    private List<ReviewDto> reviews;
}
