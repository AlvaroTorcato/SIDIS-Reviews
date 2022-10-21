package com.example.sidisreviews.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;

@Entity
@Table(name = "reviews")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String text;
    @Column(nullable = true)
    private float rating;

    @Column(nullable = true)
    private int totalVotes;

    @Column(nullable = true)
    private int upVotes;

    @Column(nullable = true)
    private int downVotes;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;
    @Column(columnDefinition = "TEXT")
    private String funFact;

    @Column(nullable = true)
    private String sku;

    @Column(nullable = true)
    private int userid;


    public Review() {
    }

    public Review(String text, float rating, String sku, int userid) throws IOException {
        this.status = "PENDING";
        setText(text);
        setRating(rating);
        retrieveDataFromApi();
        setCreationDateTime();
        this.totalVotes = 0;
        this.upVotes = 0;
        this.downVotes = 0;
        this.sku = sku;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(ChangeStatus status) {
        if (status.getApproved()){
            this.status = "APPROVED";
        }
        else {
            this.status = "REJECTED";
        }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null){
            this.text = "";
        }
        this.text = text;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("'rating' must be between 0 and 5 stars");
        }
        else if (rating % 0.5 != 0 ) {
            throw new IllegalArgumentException("'rating' can have 0.5 stars");
        }
        else if (rating == 0.0f){
            this.rating = 0;
        }
        else {
            this.rating = rating;
        }
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime() {
        this.creationDateTime = getDate();
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }


    public void retrieveDataFromApi() throws IOException {
        int month = getMonth();
        int day = getDayOfMonth();
        String baseUrl = "http://www.numbersapi.com/";
        String url = baseUrl + month + "/" + day + "/date";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            setFunFact(result);
        }
    }


    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    private int getMonth(){
        Date dNow = getDate();
        LocalDate localDate = dNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    private int getDayOfMonth(){
        Date dNow = getDate();
        LocalDate localDate = dNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfMonth();
    }

    private Date getDate(){
        Date dNow = new Date();
        return dNow;
    }
}