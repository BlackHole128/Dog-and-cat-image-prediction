package com.example.dogimagepredection;

public class Postmodel {
    private String postId;
    private String postImage;
    private String postedby;
    private String postDec;
    private long postedAt;

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public Postmodel(String postId, String postImage, String postedby, String postDec) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedby = postedby;
        this.postDec = postDec;
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

    public String getPostDec() {
        return postDec;
    }

    public void setPostDec(String postDec) {
        this.postDec = postDec;
    }
}
