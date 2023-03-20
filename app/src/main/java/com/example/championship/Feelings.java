package com.example.championship;

public class Feelings {

    private int id;
    private String title;
    private String image;
    private Integer position;

    Feelings(int id, String title, String image, Integer position) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public Integer getPosition() {
        return position;
    }

}