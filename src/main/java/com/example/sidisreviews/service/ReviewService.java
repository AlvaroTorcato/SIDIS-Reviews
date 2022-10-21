package com.example.sidisreviews.service;

import com.example.sidisreviews.model.Review;
import com.example.sidisreviews.model.ReviewDTO;
import com.example.sidisreviews.model.ReviewDetailsDTO;
import com.example.sidisreviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    public ReviewDTO createReview(final ReviewDetailsDTO resource,String sku, int userId) throws IOException {
        //Verficar sku com request
        ReviewDTO reviewDTO = null;
        int statusCode = getStatusCodeOfProduct(sku);
        if (statusCode == 200){
            Review review = new Review(resource.getText(), resource.getRating(),sku,userId);
            reviewDTO = new ReviewDTO(review);
            repository.save(review);
        }
        return reviewDTO;
    }

    public int getStatusCodeOfProduct(String sku){
        int statusCode = 0;
        try{
            URL url = new URL("localhost:8081/products/" + sku);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            statusCode = http.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return statusCode;
    }
}
