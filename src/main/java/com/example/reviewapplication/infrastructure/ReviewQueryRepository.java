package com.example.reviewapplication.infrastructure;

import com.example.reviewapplication.domain.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.reviewapplication.domain.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Review> findReviewsByCursor(Long productId, Long cursor, long size) {

        return jpaQueryFactory.selectFrom(review)
                .where(productIdEq(productId), idLessThan(cursor))
                .orderBy(review.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression productIdEq(Long productId) {
        return productId != null ? review.product.id.eq(productId) : null;
    }

    private BooleanExpression idLessThan(Long cursor) {
        return cursor != null ? review.id.lt(cursor) : null;
    }

}
