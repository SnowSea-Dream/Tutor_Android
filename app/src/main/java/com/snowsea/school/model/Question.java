package com.snowsea.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("course_number")
    @Expose
    private Integer courseNumber;
    @SerializedName("level_number")
    @Expose
    private Integer levelNumber;
    @SerializedName("subject_number")
    @Expose
    private Integer subjectNumber;
    @SerializedName("question")
    @Expose
    private Ask question;
    @SerializedName("is_answered")
    @Expose
    private Boolean isAnswered;
    @SerializedName("answer")
    @Expose
    private Answer answer;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public Integer getSubjectNumber() {
        return subjectNumber;
    }

    public void setSubjectNumber(Integer subjectNumber) {
        this.subjectNumber = subjectNumber;
    }

    public Ask getQuestion() {
        return question;
    }

    public void setQuestion(Ask question) {
        this.question = question;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

}