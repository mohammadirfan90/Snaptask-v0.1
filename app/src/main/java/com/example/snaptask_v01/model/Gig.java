package com.example.snaptask_v01.model;

import java.util.List;

public class Gig {
    private String id;
    private String uid;
    private String title;
    private String description;
    private int price;
    private String imageUrl;
    private long timestamp;
    private List<String> tags; // or ArrayList<String>

    // Required empty constructor for Firebase
    public Gig() {
    }

    // Constructor for creating a gig
    public Gig(String id, String uid, String title, String description, int price, String imageUrl, long timestamp) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }
    // Getters and Setters

    public String getId() {
        return id;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
