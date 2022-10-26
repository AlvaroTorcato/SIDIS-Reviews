package com.example.sidisreviews.controller;

import com.example.sidisreviews.model.*;
import com.example.sidisreviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Reviews", description = "Endpoints for managing reviews")
@RequestMapping("/reviews")
@RestController
@Controller
public class ReviewController {
    @Autowired
    ReviewService service;
    @Operation(summary = "Create a review")
    @PostMapping(value = "/{sku}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO create(@RequestBody final ReviewDetailsDTO resource, @PathVariable("sku") final String sku, @RequestParam int userId) throws IOException {
        return service.createReview(resource,sku, userId);
    }

    @Operation(summary = "Get reviews approved")
    @GetMapping(value = "/{sku}")
    List<ReviewDTO> findAllReviews(@PathVariable("sku") final String sku,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
        return service.findAllApprovedReviews(sku,pageNo,pageSize);
    }

    @Operation(summary = "Gets all reviews that the user made")
    @GetMapping(value = "/user/{userId}")
    List<ReviewDTO> findAllReviewsByUser(@RequestParam Integer pageNo, @RequestParam Integer pageSize,@PathVariable("userId") int userId){
        return service.findAllReviewsByUser(pageNo,pageSize,userId);
    }
    @Operation(summary = "Gets the rating of the given sku")
    @GetMapping(value = "/{sku}/rating")
    public AggregateRating findAggregateRatingBySku(@PathVariable("sku") String sku){
        return service.findAllRates(sku);
    }

    @Operation(summary = "Get reviews by id")
    @GetMapping(value = "/search/{reviewId}")
    ReviewDTO findReviewById(@PathVariable("reviewId") int reviewId){
        return service.findReviewById(reviewId);
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping(value = "/{idReview}")
    public ResponseEntity<Review> delete(@PathVariable("idReview") final int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
