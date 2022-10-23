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
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.status = 'PENDING'")
    Page<ReviewDTO> findAllPendingReviews(Pageable paging);

    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.id = :idReview")
    ReviewDTO findReviewById(@Param("idReview") int idReview);

    @Modifying
    @Transactional
    @Query("update Review u set u.status = :status")
    void updateReview(@Param("status") String status);

    @Query("select f from Review f where f.sku = :sku and f.status = 'APPROVED' order by f.creationDateTime desc ")
    Page<ReviewDTO> findAllApprovedReviews(@Param("sku") String sku, Pageable paging);

    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.userid = :idUser")
    Page<ReviewDTO> findAllReviewsByUser(@Param("idUser") int idUser,Pageable paging);

    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.sku = :sku")
    List<ReviewDTO> findAllReviewsBySku(@Param("sku") String sku);

    @Query("select new com.example.sidisreviews.model.ReviewDTO(f) from Review f where f.id = :reviewId and f.status = 'APPROVED'")
    ReviewDTO findReviewByIdAndApproved(int reviewId);
    @Modifying
    @Query("delete from Review f where f.id = :idReview")
    void deleteByIdReview(@Param("idReview") int idReview);
}
