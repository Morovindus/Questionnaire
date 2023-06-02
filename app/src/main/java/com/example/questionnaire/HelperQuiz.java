package com.example.questionnaire;

import java.util.ArrayList;

public class HelperQuiz {
    String describe, creator;
    ArrayList<String> users;

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public HelperQuiz(String describe, ArrayList<String> users, String creator){
        this.describe = describe;
        this.users = users;
        this.creator = creator;
    }
}