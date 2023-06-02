package com.example.questionnaire;

public class HelperAnswers {
    String answer;
    Integer quantity;
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public HelperAnswers(String answer, Integer quantity){
        this.answer = answer;
        this.quantity = quantity;
    }
}
