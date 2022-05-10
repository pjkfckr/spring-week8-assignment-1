package com.codesoom.assignment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Produt 엔티티 에서")
class ProductTest {
    private static final Integer PRODUCT_PRICE = 1000;
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "maker";
    private static final String PRODUCT_IMAGE_URL = "imageUrl";


    @Test
    @DisplayName("of() 메소드를 사용하여 Product를 생성할 때")
    void creationWithBuilder() {
        Product product = Product.of(
                PRODUCT_NAME,
                PRODUCT_MAKER,
                PRODUCT_PRICE,
                PRODUCT_IMAGE_URL
        );

        assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(product.getMaker()).isEqualTo(PRODUCT_MAKER);
        assertThat(product.getPrice()).isEqualTo(PRODUCT_PRICE);
        assertThat(product.getImageUrl()).isEqualTo(PRODUCT_IMAGE_URL);
    }

}
