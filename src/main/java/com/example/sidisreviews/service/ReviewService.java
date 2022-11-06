package com.example.sidisreviews.service;

import com.example.sidisreviews.model.*;
import com.example.sidisreviews.repository.ReviewRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private RequestService service;

    public ReviewDTO createReview(final ReviewDetailsDTO resource,String sku,HttpServletRequest request) throws IOException {
        int statusCode = getStatusCodeOfProduct(sku);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);

        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            System.out.println(user.getRoles());
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        Review review = new Review(resource.getText(), resource.getRating(),sku, user.getId());
        repository.save(review);
        ReviewDTO reviewDTO = new ReviewDTO(review);
        return reviewDTO;
    }
    public List<ReviewDTO> findAllReviewsPending(Integer pageNo, Integer pageSize,HttpServletRequest request) {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review = repository.findAllPendingReviews(paging);
        List<ReviewDTO> reviews = review.getContent();
        /*if (reviews.isEmpty()){
            reviews = service.
        }
        */
        return reviews;
    }
    public ReviewDTO changeStatus(int idReview, ChangeStatus resource, HttpServletRequest request) {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        String updateString = resource.updateString();
        repository.updateReview(updateString,idReview);
        ReviewDTO reviewDTO = repository.findReviewById(idReview);
        return reviewDTO;
    }

    public List<ReviewDTO> findAllApprovedReviews(String sku,Integer pageNo,Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review =  repository.findAllApprovedReviews(sku,paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }

    public List<ReviewDTO> findAllReviewsByUser(Integer pageNo,Integer pageSize, HttpServletRequest request ) {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review= repository.findAllReviewsByUser(user.getId(),paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }

    public int getStatusCodeOfProduct(String sku){
        String urlRequest = "http://localhost:8081/products/" + sku;
        int statusCode = service.getStatusOfRequest(urlRequest);
        return statusCode;
    }

    public AggregateRating findAllRates(String sku) {
        List<ReviewDTO> review= repository.findAllAprovedReviewsBySku(sku);
        AggregateRating rating = new AggregateRating(review);
        if (rating.getSku().equals("0")){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviews Not Found");
        }
        return rating;
    }

    public ReviewDTO findReviewById(int reviewId) {
        ReviewDTO review= repository.findReviewByIdAndApproved(reviewId);
        if (review == null){
            review = service.retriveReviewFromApi(reviewId);
            if (review == null){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
            }
        }
        return review;
    }

    public void deleteById(int idReview,HttpServletRequest request){
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        ReviewDTO review= findReviewById(idReview);
        String urlRequest = "http://localhost:8083/votes/search/" + idReview;
        int statusCode = service.getStatusOfRequest(urlRequest);
        if (statusCode == 404 && review.getUserid() == user.getId()){
            repository.deleteByIdReview(idReview);
        }
    }

    public ReviewDTO updateReviewWithVote(int reviewId, String status) {
        ReviewDTO review= repository.findReviewByIdAndApproved(reviewId);
        if (review == null){
            review = service.retriveReviewFromApi(reviewId);
            if (review == null){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
            }
            return service.updateReviewFromApi(reviewId,status);
        }
        else {
            int totalVotes = review.getTotalVotes();
            int upVotes = review.getUpVotes();
            int downVotes = review.getDownVotes();
            totalVotes += 1;
            if (status == "true"){
                upVotes += 1;
            }
            else {
                downVotes += 1;
            }
            repository.updateReviewWithVote(review.getId(),upVotes,downVotes,totalVotes);
        }
        return repository.findReviewByIdAndApproved(reviewId);
    }

    public List<ReviewDTO> orderAllReviewsByVotes(String sku) {
        int statusCode = getStatusCodeOfProduct(sku);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        List<ReviewDTO> reviewDTOS = repository.orderByVotes(sku);
        return reviewDTOS;
    }
}
