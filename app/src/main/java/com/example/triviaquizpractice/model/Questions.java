package com.example.triviaquizpractice.model;

public class Questions {


    private String quest;
    private boolean answer;

    @Override
    public String toString() {
        return "Questions{" +
                "quest='" + quest + '\'' +
                ", answer=" + answer +
                '}';
    }

    public Questions() {

    }

    public Questions(String quest, boolean answer) {
        this.quest = quest;
        this.answer = answer;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
