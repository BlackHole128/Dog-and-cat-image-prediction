package com.example.dogimagepredection;

public class Postmodel {
    private String postId;
    private String postImage;
    private String postedby;
    private long postLikes;
    private long comments;
    private long postedAt;

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public Postmodel(String postId, String postImage, String postedby) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedby = postedby;
    }

    public Postmodel() {
    }

    public Postmodel(long postedAt) {
        this.postedAt = postedAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public long getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(long postLikes) {
        this.postLikes = postLikes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }
}
