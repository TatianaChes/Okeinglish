package com.example.myapplicationtest.lessons.selectCard;

public class PictureModel {

    String question;
    String answer;
    String picture;
    public PictureModel(String question, String answer, String picture) {
        this.question = question;
        this.answer = answer;
        this.picture = picture;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getPicture() {
        return  picture;
    }

}
