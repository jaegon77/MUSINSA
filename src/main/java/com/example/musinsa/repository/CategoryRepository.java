package com.example.musinsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musinsa.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	boolean existsByName(String name);
}
