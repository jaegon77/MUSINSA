package com.example.musinsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musinsa.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
	boolean existsByName(String name);
}
