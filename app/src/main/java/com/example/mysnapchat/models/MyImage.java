package com.example.mysnapchat.models;

import android.graphics.Bitmap;

public class MyImage {
    private String id;
    private String user;
    private Bitmap imageBitmap;
    private String text;

//    public MyImage(String id, String user, Bitmap imageBitmap, String text) {
//        this.id = id;
//        this.user = user;
//        this.imageBitmap = imageBitmap;
//        this.text = text;
//    }

    public MyImage(String id, String user, String text) {
        this.id = id;
        this.user = user;
        this.text = text;
    }

    public MyImage(String user, Bitmap imageBitmap, String text) {
        this.user = user;
        this.imageBitmap = imageBitmap;
        this.text = text;
    }

//    public MyImage() {
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

//    public void setUser(String user) {
//        this.user = user;
//    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

//    public void setImageBitmap(Bitmap imageBitmap) {
//        this.imageBitmap = imageBitmap;
//    }

    public String getText() {
        return text;
    }

//    public void setText(String text) {
//        this.text = text;
//    }
}
