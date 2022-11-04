package com.example.sidisreviews.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ReviewAPOD {
    private int id;
    private String status;
    private String text;
    private float rating;
    private int totalVotes;
    private int upVotes;
    private int downVotes;
    private Date creationDateTime;
    private String funFact;
    private String sku;
    private int userid;

    public ReviewAPOD(@JsonProperty("id") int id,
                      @JsonProperty("status") String status,
                      @JsonProperty("text") String text,
                      @JsonProperty("rating") float rating,
                      @JsonProperty("totalVotes") int totalVotes,
                      @JsonProperty("upVotes") int upVotes,
                      @JsonProperty("downVotes") int downVotes,
                      @JsonProperty("creationDateTime") Date creationDateTime,
                      @JsonProperty("funFact") String funFact,
                      @JsonProperty("sku") String sku,
                      @JsonProperty("userid") int userid) {
        this.id = id;
        this.status = status;
        this.text = text;
        this.rating = rating;
        this.totalVotes = totalVotes;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.creationDateTime = creationDateTime;
        this.funFact = funFact;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
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
}
