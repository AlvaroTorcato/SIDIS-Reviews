package com.example.sidisreviews.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.IOException;
import java.util.Date;
@Data
public class ReviewDTO {
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

    public ReviewDTO(int id, String status, String text, float rating, int totalVotes, int upVotes, int downVotes, Date creationDateTime, String funFact, String sku, int userid) {
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

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.status = review.getStatus();
        this.text = review.getText();
        this.rating = review.getRating();
        this.totalVotes = review.getTotalVotes();
        this.upVotes = review.getUpVotes();
        this.downVotes = review.getDownVotes();
        this.creationDateTime = review.getCreationDateTime();
        this.funFact = review.getFunFact();
        this.sku = review.getSku();
        this.userid = review.getUserid();
    }
}
