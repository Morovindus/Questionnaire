package com.example.questionnaire;

import java.util.ArrayList;

public class Quiz {
    String nameQuiz;
    ArrayList<String> nameQuestion;
    ArrayList<Integer> countAnswers;
    ArrayList<String> answers;
    ArrayList<Boolean> flagSelection;

    public ArrayList<Boolean> getFlagSelection() {
        return flagSelection;
    }

    public void setFlagSelection(ArrayList<Boolean> flagSelection) {
        this.flagSelection = flagSelection;
    }

    public String getNameQuiz() {
        return nameQuiz;
    }

    public void setNameQuiz(String nameQuiz) {
        this.nameQuiz = nameQuiz;
    }

    public ArrayList<String> getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(ArrayList<String> nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public ArrayList<Integer> getCountAnswers() {
        return countAnswers;
    }

    public void setCountAnswers(ArrayList<Integer> countAnswers) {
        this.countAnswers = countAnswers;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public Quiz(){
        this.nameQuiz = "";
        this.flagSelection = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.nameQuestion = new ArrayList<>();
        this.countAnswers = new ArrayList<>();
    }
}
