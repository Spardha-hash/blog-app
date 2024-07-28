package com.codewithspardha.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithspardha.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
