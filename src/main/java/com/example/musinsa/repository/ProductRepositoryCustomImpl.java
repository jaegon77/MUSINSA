package com.example.musinsa.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.musinsa.dto.BrandTotalPriceDto;
import com.example.musinsa.dto.CategoriesForLowestPriceDto;
import com.example.musinsa.dto.CategoryLowestHighestPriceDto;
import com.example.musinsa.entity.QBrand;
import com.example.musinsa.entity.QProduct;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class  ProductRepositoryCustomImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<CategoriesForLowestPriceDto> findLowestPriceProductsByCategory() {
		QProduct product = QProduct.product;

		// 카테고리별 최소 가격
		List<Tuple> minPrices = queryFactory
				.select(product.category.id, product.price.min())
				.from(product)
				.groupBy(product.category.id)
				.fetch();

		// 최소 가격 상품 ID 조회
		List<Long> lowestProductIds = minPrices.stream()
				.flatMap(tuple -> queryFactory
						.select(product.id)
						.from(product)
						.where(product.category.id.eq(tuple.get(product.category.id))
								.and(product.price.eq(tuple.get(product.price.min()))))
						.orderBy(product.brand.name.desc())
						.limit(1)
						.fetch().stream())
				.collect(Collectors.toList());

		// 최종 데이터 조회
		return queryFactory
				.select(Projections.constructor(CategoriesForLowestPriceDto.class,
						product.category.name,
						product.brand.name,
						product.price
				))
				.from(product)
				.where(product.id.in(lowestProductIds))
				.orderBy(product.category.name.asc())
				.fetch();

	}

	@Override
	public List<BrandTotalPriceDto> findBrandTotalPrices() {
		QProduct product = QProduct.product;
		QBrand brand = QBrand.brand;

		// 특정 브랜드의 카테고리별 상품 정보 조회
		return queryFactory
				.select(Projections.constructor(BrandTotalPriceDto.class,
						brand.name,
						product.price.sum()
				))
				.from(product)
				.join(product.brand, brand)
				.groupBy(brand.name)
				.fetch();
	}

	@Override
	public List<BrandTotalPriceDto.LowestPriceDto> findProductsByBrand(String brandName) {
		QProduct product = QProduct.product;
		QBrand brand = QBrand.brand;

		// 특정 브랜드의 카테고리별 상품 정보 조회
		return queryFactory
				.select(Projections.constructor(BrandTotalPriceDto.LowestPriceDto.class,
						product.category.name,
						product.price
				))
				.from(product)
				.join(product.brand, brand)
				.where(brand.name.eq(brandName))
				.orderBy(product.category.name.asc())
				.fetch();
	}

	@Override
	public CategoryLowestHighestPriceDto.LowestPriceByCategory findLowestPriceByCategory(String categoryName) {
		QProduct product = QProduct.product;
		QBrand brand = QBrand.brand;

		// 특정 카테고리의 최저 가격 상품 조회
		return queryFactory
				.select(Projections.constructor(CategoryLowestHighestPriceDto.LowestPriceByCategory.class,
						brand.name,
						product.price
				))
				.from(product)
				.join(product.brand, brand)
				.where(product.category.name.eq(categoryName))
				.orderBy(product.price.asc(), brand.name.desc()) // 최저가, 브랜드 이름 정렬
				.limit(1)
				.fetchOne();
	}

	@Override
	public CategoryLowestHighestPriceDto.HighestPriceByCategory findHighestPriceByCategory(String categoryName) {
		QProduct product = QProduct.product;
		QBrand brand = QBrand.brand;

		// 특정 카테고리의 최고 가격 상품 조회
		return queryFactory
				.select(Projections.constructor(CategoryLowestHighestPriceDto.HighestPriceByCategory.class,
						brand.name,
						product.price
				))
				.from(product)
				.join(product.brand, brand)
				.where(product.category.name.eq(categoryName))
				.orderBy(product.price.desc(), brand.name.desc()) // 최저가, 브랜드 이름 정렬
				.limit(1)
				.fetchOne();
	}
}
