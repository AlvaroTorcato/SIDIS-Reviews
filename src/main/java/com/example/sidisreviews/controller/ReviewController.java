package com.example.sidisreviews.controller;

import com.example.sidisreviews.model.ChangeStatus;
import com.example.sidisreviews.model.Review;
import com.example.sidisreviews.model.ReviewDTO;
import com.example.sidisreviews.model.ReviewDetailsDTO;
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

    @Operation(summary = "Get all pending reviews")
    @GetMapping(value = "/pending")
    List<ReviewDTO> findAllReviewsPending(@RequestParam Integer pageNo, @RequestParam Integer pageSize){
        return service.findAllReviewsPending(pageNo,pageSize);
    }
    @Operation(summary = "Change the status of the review")
    @PutMapping(value = "/pending/{idReview}")
    public ReviewDTO changeStatus(@PathVariable("idReview") final int idReview, @RequestBody final ChangeStatus resource){
        return service.changeStatus(idReview,resource);
    }
}
