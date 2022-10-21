package com.example.sidisreviews.repository;

import com.example.sidisreviews.model.Review;
import com.example.sidisreviews.model.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.status = 'PENDING'")
    Page<ReviewDTO> findAllPendingReviews(Pageable paging);

    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.id = :idReview")
    ReviewDTO findReviewById(@Param("idReview") int idReview);

    @Modifying
    @Transactional
    @Query("update Review u set u.status = :status")
    void updateReview(@Param("status") String status);

}
