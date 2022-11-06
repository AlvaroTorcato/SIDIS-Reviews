package com.example.sidisreviews.service;

import com.example.sidisreviews.model.ReviewAPOD;
import com.example.sidisreviews.model.ReviewDTO;
import com.example.sidisreviews.model.UserDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RequestService {
    String baseURL="http://localhost:8086/";

/*
    public List<ReviewDTO> retrievePendingReviewFromApi() throws IOException {
        String baseUrl = baseURL+"moderator/pending";
        List<ReviewDTO> reviews = new ArrayList<>();
        try {
            InputStream responseStream = openConn(baseUrl).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            reviews = Arrays.asList(mapper.readValue(responseStream, ProductAPOD.class);
            product= new ProductDetailsDTO(apod.sku, apod.name, apod.description);
        } catch (IOException e) {
            System.out.println(e);
        }
        return product;
    }
    */
    public ReviewDTO retriveReviewFromApi(int reviewId){
        String baseUrl = baseURL+"search/" + reviewId;
        ReviewDTO review = null;
        try {
            InputStream responseStream = openConn(baseUrl).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            ReviewAPOD reviewAPOD = mapper.readValue(responseStream, ReviewAPOD.class);
            review = new ReviewDTO(reviewAPOD);
        } catch (IOException e) {
            System.out.println(e);
        }
        return review;
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

    public String parseJwt(HttpServletRequest request) {
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

    public ReviewDTO updateReviewFromApi(int reviewId, String status) {
        String urlRequest = "http://localhost:8086/reviews/vote/" + reviewId + "/" + status;
        ReviewAPOD reviewDTO = null;
        try {
            InputStream responseStream = openConn(urlRequest).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            reviewDTO = mapper.readValue(responseStream, ReviewAPOD.class);
        } catch (IOException e) {
            System.out.println(e);
        }
        ReviewDTO dto = new ReviewDTO(reviewDTO);

        return dto;
    }
}
