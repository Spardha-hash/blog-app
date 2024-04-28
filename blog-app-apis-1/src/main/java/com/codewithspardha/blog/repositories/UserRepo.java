package com.codewithspardha.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithspardha.blog.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

}
