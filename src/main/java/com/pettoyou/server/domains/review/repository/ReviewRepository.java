package com.pettoyou.server.domains.review.repository;

import com.pettoyou.server.domains.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
