package com.example.sidisreviews.service;

import com.example.sidisreviews.model.ReviewAPOD;
import com.example.sidisreviews.model.ReviewDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

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
    private HttpURLConnection openConn(String Url) throws IOException {

        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        return connection;
    }
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
        String baseUrl = baseURL+"moderator/pending";
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
}
