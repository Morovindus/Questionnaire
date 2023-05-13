package com.example.questionnaire;

import java.util.ArrayList;

public class HelperClass2 {

    String describe;
    ArrayList<String> users;

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
    public HelperClass2(String describe, ArrayList<String> users){
        this.describe = describe;
        this.users = users;
    }
}
