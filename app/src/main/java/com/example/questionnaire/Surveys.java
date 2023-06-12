package com.example.questionnaire;

// Класс структуры опроса
public class Surveys {
    String describe;
    Integer check;
    public String getDescribe() {
        return describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public Integer getCheck() {
        return check;
    }
    public void setCheck(Integer check) {
        this.check = check;
    }

    // Конструктор класса, инициализирующий поля
    public Surveys(String describe, Integer check) {
        this.describe = describe;
        this.check = check;
    }

}
