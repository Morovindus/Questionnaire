package com.example.questionnaire;


import java.util.ArrayList;

public class Surveys {

    String describe;
    boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    public Surveys(String describe, boolean check) {
        this.describe = describe;
        this.check = check;
    }

}
