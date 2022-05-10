package com.codesoom.assignment.controller;

import com.codesoom.assignment.AutoConfigureUtf8MockMvc;
import com.codesoom.assignment.application.product.ProductCommandService;
import com.codesoom.assignment.application.product.ProductQueryService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.TestProductRepositoryDouble;
import com.codesoom.assignment.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureUtf8MockMvc
@DisplayName("ProductController MockMVC 에서")
class ProductControllerWebTest {
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "메이커1";
    private static final Integer PRODUCT_PRICE = 100000;
    private static final String PRODUCT_IMAGE_URL = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F9941A1385B99240D2E";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestProductRepositoryDouble productRepository;

    @Autowired
    private ProductQueryService productQueryService;
    @Autowired
    private ProductCommandService productCommandService;

    /**
     * 하나의 Product 를 생성해 등록합니다.
     *
     * @return 생성한 Product
     */
    private Product createProduct() {
        ProductDto productDto = ProductDto.builder()
                .name(PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(PRODUCT_PRICE)
                .imageUrl(PRODUCT_IMAGE_URL)
                .build();
        return productCommandService.create(productDto);
    }

    @Nested
    @DisplayName("GET - /products")
    class Describe_of_GET {

        @Nested
        @DisplayName("조회할 수 있는 Product가 있을 경우")
        class Content_with_list {

            @BeforeEach
            void setUp() {
                createProduct();
            }

            @Test
            @DisplayName("모든 Prouct를 보여준다")
            void it_return_all_products() throws Exception {
                List<Product> products = productQueryService.products();
                System.out.println(products);
                mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content()
                                .string(containsString(
                                        objectMapper.writeValueAsString(products)
                                )));
            }
        }

        @Nested
        @DisplayName("Product가 없을 경우")
        class Content_with_empty_list {
            @BeforeEach
            void setUp() {
                productRepository.deleteAll();
            }

            @Test
            @DisplayName("빈 배열을 보여준다")
            void it_returns_empty_list() throws Exception {
                mockMvc.perform(get("/products")
                                .contentType(APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
                        .andExpect(content()
                                .string(StringContains.containsString("[]")));
            }
        }
    }

    @Nested
    @DisplayName("GET - /products/{id}")
    class Describe_of_detail_product {
        private Product product;

        @BeforeEach
        void setUp() {
            product = createProduct();
        }

        @Nested
        @DisplayName("조회할수 있는 {id} 가 주어지면")
        class Context_with_product {
            private long productId;

            @BeforeEach
            void setUp() {
                productId = product.getId();
            }

            @Test
            @DisplayName("{id}와 동일한 Product를 보여준다")
            void it_return_product() throws Exception {
                mockMvc.perform(get("/products/" + productId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists())
                        .andExpect(jsonPath("price").exists())
                        .andExpect(jsonPath("imageUrl").exists());
            }
        }

        @Nested
        @DisplayName("조회할수 없는 {id} 가 주어지면")
        class Context_without_product {
            private long productId;

            @BeforeEach
            void setUp() {
                productId = product.getId();
                productRepository.delete(product);
            }

            @Test
            @DisplayName("404 에러를 던진다")
            void it_throw_productNotFoundException() throws Exception {
                mockMvc.perform(get("/products/{id}", productId))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
