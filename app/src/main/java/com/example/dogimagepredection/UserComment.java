package com.example.dogimagepredection;

public class UserComment {
    private String comment;
    private  String rating;
    private String name;

    public UserComment() {
        this.comment = comment;
        this.rating = rating;
        this.name = name;
    }

    public UserComment(String comment, String rating, String name) {
        this.comment = comment;
        this.rating = rating;
        this.name = name;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public String getRating() {

        return rating;
    }

    public void setRating(String rating) {

        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
