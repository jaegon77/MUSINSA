package com.example.musinsa.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import com.example.musinsa.common.config.QuerydslConfiguration;
import com.example.musinsa.dto.BrandTotalPriceDto;
import com.example.musinsa.dto.CategoriesForLowestPriceDto;
import com.example.musinsa.dto.CategoryLowestHighestPriceDto;

@DataJpaTest
@Import(QuerydslConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("findLowestPriceProductsByCategory - 카테고리별 최저가 상품 조회 성공 테스트")
	void testFindLowestPriceProductsByCategory_Success() {
		List<CategoriesForLowestPriceDto> results = productRepository.findLowestPriceProductsByCategory();

		assertFalse(results.isEmpty());
		assertEquals(8, results.size());
		assertEquals("가방", results.get(0).getCategory());
		assertEquals(2000, results.get(0).getPrice());
		assertEquals("A", results.get(0).getBrandName());
	}

	@Test
	@DisplayName("findBrandTotalPrices - 브랜드별 총 상품 가격 조회 성공 테스트")
	void testFindBrandTotalPrices_Success() {
		List<BrandTotalPriceDto> results = productRepository.findBrandTotalPrices();

		assertFalse(results.isEmpty());
		assertEquals(9, results.size());
		assertTrue(results.stream().anyMatch(dto -> dto.getBrandName().equals("A")));
	}

	@Test
	@DisplayName("findLowestPriceByCategory - 특정 카테고리 최저가 상품 조회 성공 테스트")
	void testFindLowestPriceByCategory_Success() {
		String categoryName = "상의";
		CategoryLowestHighestPriceDto.LowestPriceByCategory result = productRepository.findLowestPriceByCategory(categoryName);

		assertNotNull(result);
		assertEquals("C", result.getBrandName());
		assertEquals(10000, result.getPrice());
	}

	@Test
	@DisplayName("findProductsByBrand - 특정 브랜드의 상품 조회")
	void testFindProductsByBrand() {
		List<BrandTotalPriceDto.LowestPriceDto> results = productRepository.findProductsByBrand("A");

		assertNotNull(results);
		assertEquals(8, results.size());
	}

	@Test
	@DisplayName("findHighestPriceByCategory - 특정 카테고리 최고가 상품 조회 성공 테스트")
	void testFindHighestPriceByCategory_Success() {
		String categoryName = "상의";
		CategoryLowestHighestPriceDto.HighestPriceByCategory result = productRepository.findHighestPriceByCategory(categoryName);

		assertNotNull(result);
		assertEquals("I", result.getBrandName());
		assertEquals(11400, result.getPrice());
	}

	@Test
	@DisplayName("findHighestPriceByCategory - 특정 카테고리의 최고 가격 상품 조회")
	void testFindHighestPriceByCategory() {
		CategoryLowestHighestPriceDto.HighestPriceByCategory result = productRepository.findHighestPriceByCategory("상의");

		assertNotNull(result);
		assertEquals("I", result.getBrandName());
		assertEquals(11400, result.getPrice());
	}

	@Test
	@DisplayName("데이터베이스 초기화 확인 테스트")
	void testDatabaseInitialization() {
		assertTrue(productRepository.count() > 0, "Products should exist in database.");
	}
}
