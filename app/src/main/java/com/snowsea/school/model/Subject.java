package com.snowsea.school.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("course_number")
    @Expose
    private Integer courseNumber;
    @SerializedName("level_number")
    @Expose
    private Integer levelNumber;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("chapters")
    @Expose
    private ArrayList<Chapter> chapters = null;

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Integer levelNumber) {
        this.levelNumber = levelNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) { this.price = price; }

    public Float getPrice() { return price; }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    Boolean bPaid = false;

    public void setPaid(Boolean bPaid) {
        this.bPaid = bPaid;
    }

    public Boolean isPaid() {
        return bPaid;
    }

}