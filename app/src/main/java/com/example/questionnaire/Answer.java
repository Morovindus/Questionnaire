package com.example.questionnaire;

// Класс структуры ответа
public class Answer {
    String answer;
    boolean box;
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public boolean isBox() {
        return box;
    }
    public void setBox(boolean box) {
        this.box = box;
    }

    // Конструктор класса, инициализирующий поля
    public Answer(String answer, boolean box) {
        this.answer = answer;
        this.box = box;
    }
}
