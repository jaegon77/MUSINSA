package com.example.musinsa.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// 1. 카테고리별 최저가 상품 조회 테스트
	@Test
	@DisplayName("GET /api/products/category/lowestPrice - 카테고리별 최저가 상품 조회 성공")
	void testGetLowestPriceProductsByCategory_Success() throws Exception {
		mockMvc.perform(get("/api/products/category/lowestPrice"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.categories", hasSize(8)))
				.andExpect(jsonPath("$.data.categories[0].category").value("가방"))
				.andExpect(jsonPath("$.data.categories[0].price").value("2,000"))
				.andExpect(jsonPath("$.data.categories[0].brandName").value("A"))
				.andDo(print());
	}

	// 2. 브랜드별 최저가 상품 조회 테스트
	@Test
	@DisplayName("GET /api/products/brand/lowestPrice - 브랜드별 최저가 상품 조회 성공")
	void testGetLowestPriceProductsByBrand_Success() throws Exception {
		mockMvc.perform(get("/api/products/brand/lowestPrice"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.brand").value("D"))
				.andExpect(jsonPath("$.data.totalPrice").value("36,100"))
				.andExpect(jsonPath("$.data.categories", hasSize(8)))
				.andDo(print());
	}

	// 3. 특정 카테고리 최저/최고가 상품 조회 테스트
	@Test
	@DisplayName("GET /api/products/category/highestLowest - 특정 카테고리 최저/최고가 상품 조회 성공")
	void testGetHighestAndLowestPriceByCategory_Success() throws Exception {
		mockMvc.perform(get("/api/products/category/highestLowest")
						.param("categoryName", "상의"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.category").value("상의"))
				.andExpect(jsonPath("$.data.lowestPrice.brandName").value("C"))
				.andExpect(jsonPath("$.data.lowestPrice.price").value("10,000"))
				.andExpect(jsonPath("$.data.highestPrice.brandName").value("I"))
				.andExpect(jsonPath("$.data.highestPrice.price").value("11,400"))
				.andDo(print());
	}

	// 4. 브랜드 추가 테스트
	@Test
	@DisplayName("POST /api/products/brand - 브랜드 추가 성공")
	void testAddBrand_Success() throws Exception {
		String requestBody = """
            {
                "operation": "ADD",
                "name": "Z"
            }
        """;

		mockMvc.perform(post("/api/products/brand")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Brand added successfully."))
				.andDo(print());
	}

	// 5. 카테고리 추가 테스트
	@Test
	@DisplayName("POST /api/products/category - 카테고리 추가 성공")
	void testAddCategory_Success() throws Exception {
		String requestBody = """
            {
                "operation": "ADD",
                "name": "신발"
            }
        """;

		mockMvc.perform(post("/api/products/category")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Category added successfully."))
				.andDo(print());
	}

	// 6. 상품 추가 테스트
	@Test
	@DisplayName("POST /api/products/product - 상품 추가 성공")
	void testAddProduct_Success() throws Exception {
		String requestBody = """
            {
                "operation": "ADD",
                "name": "모자_J",
                "price": 1200,
                "brandId": 1,
                "categoryId": 6
            }
        """;

		mockMvc.perform(post("/api/products/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Product added successfully."))
				.andDo(print());
	}
}