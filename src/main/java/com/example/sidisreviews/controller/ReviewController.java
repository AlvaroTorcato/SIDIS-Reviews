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

import javax.servlet.http.HttpServletRequest;
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
    public ReviewDTO create(@RequestBody final ReviewDetailsDTO resource, @PathVariable("sku") final String sku, HttpServletRequest request) throws IOException {
        return service.createReview(resource,sku, request);
    }

    @Operation(summary = "Get reviews approved")
    @GetMapping(value = "/{sku}")
    List<ReviewDTO> findAllReviews(@PathVariable("sku") final String sku,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
        return service.findAllApprovedReviews(sku,pageNo,pageSize);
    }

    @Operation(summary = "Gets all reviews that the user made")
    @GetMapping(value = "/user")
    List<ReviewDTO> findAllReviewsByUser(@RequestParam Integer pageNo, @RequestParam Integer pageSize, HttpServletRequest request){
        return service.findAllReviewsByUser(pageNo,pageSize,request);
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

    @Operation(summary = "Get reviews by id to another API")
    @GetMapping(value = "/Internalsearch/{reviewId}")
    ReviewDTO findReviewByIdInternal(@PathVariable("reviewId") int reviewId){
        return service.findReviewByIdInternal(reviewId);
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping(value = "/{idReview}")
    public ResponseEntity<Review> delete(@PathVariable("idReview") final int id,HttpServletRequest request) {
        service.deleteById(id,request);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Add vote to review")
    @GetMapping(value = "/vote/{reviewId}/{string}")
    ReviewDTO updateReviewWithVote(@PathVariable("reviewId") int reviewId,@PathVariable("string") String status){
        return service.updateReviewWithVote(reviewId,status);
    }

    @Operation(summary = "Add vote to review to another API")
    @GetMapping(value = "/Internalvote/{reviewId}/{string}")
    ReviewDTO updateReviewWithVoteInternal(@PathVariable("reviewId") int reviewId,@PathVariable("string") String status){
        return service.updateReviewWithVoteInternal(reviewId,status);
    }

    @Operation(summary = "Get all reviews order by total votes ")
    @GetMapping(value = "/order/{sku}")
    List<ReviewDTO> orderReviewsReviews(@PathVariable("sku") final String sku){
        return service.orderAllReviewsByVotes(sku);
    }

}
