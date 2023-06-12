package com.example.questionnaire;

// Класс, благодаря которому заполняются поля в БД связанные с вопросами
public class HelperQuestion {
    String describe;
    Boolean selection;
    public Boolean getSelection() {
        return selection;
    }
    public void setSelection(Boolean selection) {
        this.selection = selection;
    }
    public String getDescribe() {
        return describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    // Конструктор класс, инициализирующий поля
    public HelperQuestion(String describe, Boolean selection){
        this.describe = describe;
        this.selection = selection;
    }
}
