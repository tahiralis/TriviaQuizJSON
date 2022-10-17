package com.example.triviaquizpractice.data;

import com.example.triviaquizpractice.model.Questions;

import java.util.ArrayList;

public interface quesAsyncResponse {

    public void processFinished(ArrayList<Questions> questionsArrayList);
}
