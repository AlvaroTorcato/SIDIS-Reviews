package com.example.sidisreviews.service;

import com.example.sidisreviews.model.*;
import com.example.sidisreviews.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    public ReviewDTO createReview(final ReviewDetailsDTO resource,String sku, int userId) throws IOException {
        int statusCode = getStatusCodeOfProduct(sku);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        Review review = new Review(resource.getText(), resource.getRating(),sku,userId);
        repository.save(review);
        ReviewDTO reviewDTO = new ReviewDTO(review);
        return reviewDTO;
    }
    public List<ReviewDTO> findAllReviewsPending(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review = repository.findAllPendingReviews(paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }
    public ReviewDTO changeStatus(int idReview, ChangeStatus resource) {
        String updateString = resource.updateString();
        repository.updateReview(updateString);
        ReviewDTO reviewDTO = repository.findReviewById(idReview);
        return reviewDTO;
    }

    public List<ReviewDTO> findAllApprovedReviews(String sku,Integer pageNo,Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review =  repository.findAllApprovedReviews(sku,paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }

    public List<ReviewDTO> findAllReviewsByUser(Integer pageNo,Integer pageSize, int userId) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review= repository.findAllReviewsByUser(userId,paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }

    public int getStatusCodeOfProduct(String sku){
        int statusCode;
        try{
            String urlRequest = "http://localhost:8081/products/" + sku;
            URL url = new URL(urlRequest);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            statusCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return statusCode;
    }

    public AggregateRating findAllRates(String sku) {
        Page<ReviewDTO> review= repository.findAllReviewsBySku(sku);
        List<ReviewDTO> reviews = review.getContent();
        AggregateRating rating = new AggregateRating(reviews);
        return rating;
    }
}
