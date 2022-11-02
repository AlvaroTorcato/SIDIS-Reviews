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

    public ReviewDTO createReview(final ReviewDetailsDTO resource,String sku,HttpServletRequest request) throws IOException {
        int statusCode = getStatusCodeOfProduct(sku);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        String jwt = parseJwt(request);
        UserDetailsDTO user = makeRequestToAutentication(jwt);

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
        String jwt = parseJwt(request);
        UserDetailsDTO user = makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review = repository.findAllPendingReviews(paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }
    public ReviewDTO changeStatus(int idReview, ChangeStatus resource, HttpServletRequest request) {
        String jwt = parseJwt(request);
        UserDetailsDTO user = makeRequestToAutentication(jwt);
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
        String jwt = parseJwt(request);
        UserDetailsDTO user = makeRequestToAutentication(jwt);
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
        int statusCode = getStatusOfRequest(urlRequest);
        return statusCode;
    }

    public AggregateRating findAllRates(String sku) {
        List<ReviewDTO> review= repository.findAllReviewsBySku(sku);
        AggregateRating rating = new AggregateRating(review);
        return rating;
    }

    public ReviewDTO findReviewById(int reviewId) {
        ReviewDTO review= repository.findReviewByIdAndApproved(reviewId);
        if (review == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
        }
        return review;
    }

    public void deleteById(int idReview,HttpServletRequest request){
        String jwt = parseJwt(request);
        UserDetailsDTO user = makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        ReviewDTO review= findReviewById(idReview);
        String urlRequest = "http://localhost:8083/votes/search/" + idReview;
        int statusCode = getStatusOfRequest(urlRequest);
        //7System.out.println(statusCode);
        //System.out.println(review.getUserid());
        //System.out.println(user.getId());
        if (statusCode == 404 && review.getUserid() == user.getId()){
            repository.deleteByIdReview(idReview);
        }
    }

    public int getStatusOfRequest(String urlRequest){
        int statusCode;
        try{
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

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }

    public UserDetailsDTO makeRequestToAutentication(String jwt){
        String urlRequest = "http://localhost:8084/auth/search/" + jwt;
        UserDetailsDTO user = null;
        try {
            InputStream responseStream = openConn(urlRequest).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            user = mapper.readValue(responseStream, UserDetailsDTO.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return user;
    }
    private HttpURLConnection openConn(String baseUrl) throws IOException {

        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        return connection;
    }
}
