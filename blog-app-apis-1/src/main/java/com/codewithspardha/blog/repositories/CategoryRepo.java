package com.codewithspardha.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithspardha.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	
}
