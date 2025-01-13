package com.example.musinsa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.musinsa.common.exception.CustomException;
import com.example.musinsa.common.util.OperationType;
import com.example.musinsa.dto.BrandRequest;
import com.example.musinsa.dto.BrandTotalPriceDto;
import com.example.musinsa.dto.CategoriesForLowestPriceDto;
import com.example.musinsa.dto.CategoryLowestHighestPriceDto;
import com.example.musinsa.dto.CategoryRequest;
import com.example.musinsa.dto.ProductRequest;
import com.example.musinsa.entity.Brand;
import com.example.musinsa.entity.Category;
import com.example.musinsa.entity.Product;
import com.example.musinsa.repository.BrandRepository;
import com.example.musinsa.repository.CategoryRepository;
import com.example.musinsa.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private BrandRepository brandRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Test
	void testGetLowestPriceProductsByCategory_Success() {
		// Mock 데이터
		List<CategoriesForLowestPriceDto> mockData = List.of(
				new CategoriesForLowestPriceDto("상의", "C", 10000),
				new CategoriesForLowestPriceDto("아우터", "E", 5000),
				new CategoriesForLowestPriceDto("바지", "D", 3000),
				new CategoriesForLowestPriceDto("스니커즈", "G", 9000),
				new CategoriesForLowestPriceDto("가방", "A", 2000),
				new CategoriesForLowestPriceDto("모자", "D", 1500),
				new CategoriesForLowestPriceDto("양말", "I", 1700),
				new CategoriesForLowestPriceDto("액세서리", "F", 1900)
		);
		when(productRepository.findLowestPriceProductsByCategory()).thenReturn(mockData);

		// 서비스 호출
		Map<String, Object> result = productService.getLowestPriceProductsByCategory();

		// 검증
		assertEquals("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회.", result.get("message"));
		assertEquals(34100, Integer.parseInt(result.get("totalPrice").toString().replace(",", "")));
		assertEquals(mockData, result.get("categories"));
	}

	@Test
	void testGetLowestPriceProductsByCategory_EmptyResult() {
		when(productRepository.findLowestPriceProductsByCategory()).thenReturn(List.of());

		CustomException ex = assertThrows(CustomException.class,
				() -> productService.getLowestPriceProductsByCategory());

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
		assertEquals("No products found for the given brand and category.", ex.getMessage());
	}

	@Test
	void testGetLowestPriceProductsByBrand_Success() {
		// Mock 데이터
		List<BrandTotalPriceDto.LowestPriceDto> mockProducts = List.of(
				new BrandTotalPriceDto.LowestPriceDto("상의", 10100),
				new BrandTotalPriceDto.LowestPriceDto("아우터", 5100),
				new BrandTotalPriceDto.LowestPriceDto("바지", 3000),
				new BrandTotalPriceDto.LowestPriceDto("스니커즈", 9500),
				new BrandTotalPriceDto.LowestPriceDto("가방", 2500),
				new BrandTotalPriceDto.LowestPriceDto("모자", 1500),
				new BrandTotalPriceDto.LowestPriceDto("양말", 2400),
				new BrandTotalPriceDto.LowestPriceDto("액세서리", 2000)
		);
		BrandTotalPriceDto mockBrand = new BrandTotalPriceDto("D", 36100);
		mockBrand.setLowestPriceDto(mockProducts);

		when(productRepository.findBrandTotalPrices()).thenReturn(List.of(mockBrand));
		when(productRepository.findProductsByBrand("D")).thenReturn(mockProducts);

		// 서비스 호출
		Map<String, Object> result = productService.getLowestPriceProductsByBrand();

		//검증
		assertEquals("D", result.get("brand"));
		assertEquals(36100, Integer.parseInt(result.get("totalPrice").toString().replace(",", "")));
		assertEquals("총합 최저가 단일 브랜드로 모든 카테고리 상품의 가격 조회.", result.get("message"));
		assertEquals(mockProducts, result.get("categories"));
	}

	@Test
	void testGetLowestPriceProductsByBrand_NoData() {
		when(productRepository.findBrandTotalPrices()).thenReturn(List.of());

		CustomException ex = assertThrows(CustomException.class, () -> productService.getLowestPriceProductsByBrand());

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
		assertEquals("No data available for the requested operation.", ex.getMessage());
	}

	@Test
	void testGetHighestAndLowestPriceByCategory_Success() {
		// Mock 데이터
		CategoryLowestHighestPriceDto.LowestPriceByCategory lowestPrice = new CategoryLowestHighestPriceDto.LowestPriceByCategory("브랜드A", 5000);
		CategoryLowestHighestPriceDto.HighestPriceByCategory highestPrice = new CategoryLowestHighestPriceDto.HighestPriceByCategory("브랜드B", 15000);

		// Mocking
		when(categoryRepository.existsByName("상의")).thenReturn(true);
		when(productRepository.findLowestPriceByCategory("상의")).thenReturn(lowestPrice);
		when(productRepository.findHighestPriceByCategory("상의")).thenReturn(highestPrice);

		// 서비스 호출
		Map<String, Object> result = productService.getHighestAndLowestPriceByCategory("상의");

		// 검증
		assertEquals("상의", result.get("category")); // category가 예상값인지 확인
		assertEquals(lowestPrice.getBrandName(), ((CategoryLowestHighestPriceDto.LowestPriceByCategory)result.get("lowestPrice")).getBrandName());
		assertEquals(lowestPrice.getPrice(), ((CategoryLowestHighestPriceDto.LowestPriceByCategory)result.get("lowestPrice")).getPrice());
		assertEquals(highestPrice.getBrandName(), ((CategoryLowestHighestPriceDto.HighestPriceByCategory)result.get("highestPrice")).getBrandName());
		assertEquals(highestPrice.getPrice(), ((CategoryLowestHighestPriceDto.HighestPriceByCategory)result.get("highestPrice")).getPrice());
	}

	@Test
	void testGetHighestAndLowestPriceByCategory_Failure_CategoryNotFound() {
		// Mocking: 존재하지 않는 카테고리
		when(categoryRepository.existsByName("없는카테고리")).thenReturn(false);

		// 서비스 호출 및 예외 검증
		CustomException exception = assertThrows(CustomException.class,
				() -> productService.getHighestAndLowestPriceByCategory("없는카테고리"));

		// 검증
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
		assertEquals("Category name does not exist.", exception.getMessage());
	}

	@Test
	void testHandleBrandOperation_ADDBrand_Success() {
		// 요청 객체 생성
		BrandRequest request = new BrandRequest();
		request.setOperation(OperationType.ADD);
		request.setName("Z");

		// Mock 설정
		when(brandRepository.existsByName(request.getName())).thenReturn(false);
		when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// 서비스 호출
		Map<String, Object> result = productService.handleBrandOperation(request);

		// 검증
		assertEquals("Brand added successfully.", result.get("message"));

		// Repository 호출 여부 검증
		verify(brandRepository).existsByName(request.getName());
		verify(brandRepository).save(any(Brand.class));
	}

	@Test
	void testHandleBrandOperation_ADDBrand_Conflict() {
		// 요청 객체 생성
		BrandRequest request = new BrandRequest();
		request.setOperation(OperationType.ADD);
		request.setName("Z");

		// Mock 설정
		when(brandRepository.existsByName(request.getName())).thenReturn(true);

		// 서비스 호출 및 예외 검증
		CustomException ex = assertThrows(CustomException.class,
				() -> productService.handleBrandOperation(request));

		assertEquals(HttpStatus.CONFLICT, ex.getStatus());
		assertEquals("Brand with name Z already exists.", ex.getMessage());

		// Repository 호출 여부 검증
		verify(brandRepository).existsByName(request.getName());
		verify(brandRepository, never()).save(any(Brand.class));
	}

	@Test
	void testHandleCategoryOperation_UPDATECategory_Success() {
		// 요청 객체 생성
		CategoryRequest request = new CategoryRequest();
		request.setOperation(OperationType.UPDATE);
		request.setId(1L);
		request.setName("티");

		// 기존 카테고리 Mock 데이터
		Category existingCategory = new Category();
		existingCategory.setId(request.getId());
		existingCategory.setName("상의");

		// Mock 설정
		when(categoryRepository.findById(request.getId())).thenReturn(Optional.of(existingCategory));
		when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

		// 서비스 호출
		Map<String, Object> result = productService.handleCategoryOperation(request);

		// 검증
		assertEquals("Category updated successfully.", result.get("message"));

		// Repository Interaction 검증
		verify(categoryRepository).findById(request.getId());
		verify(categoryRepository).save(existingCategory);
	}

	@Test
	void testHandleProductOperation_ADDProduct_Success() {
		// 요청 객체 생성
		ProductRequest request = new ProductRequest();
		request.setOperation(OperationType.ADD);
		request.setName("상품1");
		request.setPrice(10000);
		request.setBrandId(1L);
		request.setBrandId(1L);
		request.setCategoryId(1L);

		// Mock 데이터
		Brand mockBrand = new Brand();
		mockBrand.setId(1L);
		mockBrand.setName("A");

		Category mockCategory = new Category();
		mockCategory.setId(1L);
		mockCategory.setName("상의");

		Product mockProduct = new Product();
		mockProduct.setName(request.getName());
		mockProduct.setPrice(request.getPrice());
		mockProduct.setBrand(mockBrand);
		mockProduct.setCategory(mockCategory);

		// Mock 설정
		when(productRepository.existsByName(request.getName())).thenReturn(false);
		when(brandRepository.findById(request.getBrandId())).thenReturn(Optional.of(mockBrand));
		when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(mockCategory));
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// 서비스 호출
		Map<String, Object> result = productService.handleProductOperation(request);

		// 검증
		assertEquals("Product added successfully.", result.get("message"));

		// Repository 호출 여부 검증
		verify(productRepository).existsByName(request.getName());
		verify(brandRepository).findById(request.getBrandId());
		verify(categoryRepository).findById(request.getCategoryId());
		verify(productRepository).save(any(Product.class));
	}

	@Test
	void testHandleProductOperation_ADDProduct_Conflict() {
		// 요청 객체 생성
		ProductRequest request = new ProductRequest();
		request.setOperation(OperationType.ADD);
		request.setName("상품1");

		// Mock 설정
		when(productRepository.existsByName(request.getName())).thenReturn(true);

		// 서비스 호출 및 예외 검증
		CustomException ex = assertThrows(CustomException.class,
				() -> productService.handleProductOperation(request));

		assertEquals(HttpStatus.CONFLICT, ex.getStatus());
		assertEquals("Product with name 상품1 already exists.", ex.getMessage());

		// Repository 호출 여부 검증
		verify(productRepository).existsByName(request.getName());
		verify(brandRepository, never()).findById(anyLong());
		verify(categoryRepository, never()).findById(anyLong());
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	void testHandleProductOperation_UPDATEProduct_Success() {
		// 요청 객체 생성
		ProductRequest request = new ProductRequest();
		request.setOperation(OperationType.UPDATE);
		request.setId(1L);
		request.setName("업데이트상품");
		request.setPrice(12000);
		request.setBrandId(1L);
		request.setCategoryId(1L);

		// Mock 데이터
		Brand mockBrand = new Brand();
		mockBrand.setId(1L);
		mockBrand.setName("브랜드A");

		Category mockCategory = new Category();
		mockCategory.setId(1L);
		mockCategory.setName("카테고리A");

		Product mockProduct = new Product();
		mockProduct.setId(1L);
		mockProduct.setName("기존상품");
		mockProduct.setPrice(10000);
		mockProduct.setBrand(mockBrand);
		mockProduct.setCategory(mockCategory);

		// Mock 설정
		when(productRepository.findById(request.getId())).thenReturn(Optional.of(mockProduct));
		when(brandRepository.findById(request.getBrandId())).thenReturn(Optional.of(mockBrand));
		when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(mockCategory));
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// 서비스 호출
		Map<String, Object> result = productService.handleProductOperation(request);

		// 검증
		assertEquals("Product updated successfully.", result.get("message"));
		assertEquals(request.getName(), mockProduct.getName());
		assertEquals(request.getPrice(), mockProduct.getPrice());

		// Repository 호출 여부 검증
		verify(productRepository).findById(request.getId());
		verify(brandRepository).findById(request.getBrandId());
		verify(categoryRepository).findById(request.getCategoryId());
		verify(productRepository).save(mockProduct);
	}

	@Test
	void testHandleProductOperation_DELETEProduct_Success() {
		// 요청 객체 생성
		ProductRequest request = new ProductRequest();
		request.setOperation(OperationType.DELETE);
		request.setId(1L);

		// Mock 데이터
		Product mockProduct = new Product();
		mockProduct.setId(1L);
		mockProduct.setName("삭제상품");

		// Mock 설정
		when(productRepository.findById(request.getId())).thenReturn(Optional.of(mockProduct));

		// 서비스 호출
		Map<String, Object> result = productService.handleProductOperation(request);

		// 검증
		assertEquals("Product deleted successfully.", result.get("message"));

		// Repository 호출 여부 검증
		verify(productRepository).findById(request.getId());
		verify(productRepository).delete(mockProduct);
	}

	@Test
	void testHandleProductOperation_DELETEProduct_NotFound() {
		// 요청 객체 생성
		ProductRequest request = new ProductRequest();
		request.setOperation(OperationType.DELETE);
		request.setId(1L);

		// Mock 설정
		when(productRepository.findById(request.getId())).thenReturn(Optional.empty());

		// 서비스 호출 및 예외 검증
		CustomException ex = assertThrows(CustomException.class,
				() -> productService.handleProductOperation(request));

		assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
		assertEquals("Product not found.", ex.getMessage());

		// Repository 호출 여부 검증
		verify(productRepository).findById(request.getId());
		verify(productRepository, never()).delete(any(Product.class));
	}
}