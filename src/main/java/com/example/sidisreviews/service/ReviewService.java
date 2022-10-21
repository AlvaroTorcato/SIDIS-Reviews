package com.example.sidisreviews.service;

import com.example.sidisreviews.model.Review;
import com.example.sidisreviews.model.ReviewDTO;
import com.example.sidisreviews.model.ReviewDetailsDTO;
import com.example.sidisreviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    public ReviewDTO createReview(final ReviewDetailsDTO resource,String sku, int userId) throws IOException {
        //Verficar sku com request

        Review review = new Review(resource.getText(), resource.getRating(),sku,userId);
        ReviewDTO reviewDTO = new ReviewDTO(review);
        repository.save(review);
        return reviewDTO;
    }
}
