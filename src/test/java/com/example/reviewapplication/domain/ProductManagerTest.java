package com.example.reviewapplication.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductManagerTest {

    @Test
    void 상품에_리뷰가_추가되면_상품의_리뷰_개수와_평점을_갱신한다() {
        //given
        ProductManager productManager = new ProductManager();
        Product product = Product.builder()
                .reviewCount(1L)
                .score(3F)
                .build();
        Review review = Review.createReview(product, 1L, 1, "content");

        //when
        productManager.updateProduct(product, review);

        //then
        assertEquals(2L, product.getReviewCount());
        assertEquals(2F, product.getScore());
    }

}