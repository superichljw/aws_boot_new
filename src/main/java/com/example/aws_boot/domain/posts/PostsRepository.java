package com.example.aws_boot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//db layer 접근자
public interface PostsRepository extends JpaRepository<Posts,Long> {

}
