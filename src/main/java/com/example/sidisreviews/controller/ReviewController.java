package com.example.sidisreviews.controller;

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
}
