package com.example.sidisreviews.controller;

import com.example.sidisreviews.model.ReviewDTO;
import com.example.sidisreviews.model.ReviewDetailsDTO;
import com.example.sidisreviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

public class ReviewController {
    @Autowired
    ReviewService service;
    @Operation(summary = "Create a review")
    @PostMapping(value = "/{sku}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDTO> create(@RequestBody final ReviewDetailsDTO resource, @PathVariable("sku") final String sku,@RequestParam int userId) throws IOException {
        final var review = service.createReview(resource,sku, userId);
        return ResponseEntity.ok().body(review);
    }
}
