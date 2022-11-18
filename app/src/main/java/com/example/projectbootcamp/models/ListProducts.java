package com.example.projectbootcamp.models;

public class ListProducts {
    String title;
    String image;
    String rating;

    public ListProducts(String title, String image, String rating) {
        this.title = title;
        this.image = image;
        this.rating = rating;
    }

    public String getTitleProduct() {
        return title;
    }

    public void setTitleProduct(String title) {
        this.title = title;
    }

    public String getImageProduct() {
        return image;
    }

    public void setImageProduct(String image) {
        this.image = image;
    }

    public String getRatingProduct() {
        return rating;
    }

    public void setRatingProduct(String rating) {
        this.rating = rating;
    }
}
