package com.example.sidisreviews.controller;

import com.example.sidisreviews.model.ChangeStatus;
import com.example.sidisreviews.model.ReviewDTO;
import com.example.sidisreviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;
import java.util.List;

@Tag(name = "Moderator", description = "Endpoints for managing reviews")
@RequestMapping("/moderator")
@RestController
@Controller
public class ModeratorController {
    @Autowired
    ReviewService service;

    @Operation(summary = "Get all pending reviews")
    @GetMapping(value = "/pending")
    List<ReviewDTO> findAllReviewsPending(@RequestParam Integer pageNo, @RequestParam Integer pageSize, HttpServletRequest request){
        return service.findAllReviewsPending(pageNo,pageSize,request);
    }

    @Operation(summary = "Get all pending reviews from another API")
    @GetMapping(value = "/internalSearch/pending")
    List<ReviewDTO> findAllReviewsPendingInternal(@RequestParam Integer pageNo, @RequestParam Integer pageSize){
        return service.findAllReviewsPendingInternal(pageNo,pageSize);
    }

    @Operation(summary = "Change the status of the review")
    @PutMapping(value = "/pending/{idReview}")
    public ReviewDTO changeStatus(@PathVariable("idReview") final int idReview, @RequestBody final ChangeStatus resource, HttpServletRequest request){
        return service.changeStatus(idReview,resource,request);
    }
}
