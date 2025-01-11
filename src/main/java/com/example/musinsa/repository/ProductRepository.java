package com.example.musinsa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musinsa.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}

