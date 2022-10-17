package com.example.triviaquizpractice.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaquizpractice.controller.MySingleton;
import com.example.triviaquizpractice.model.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements.json";

    ArrayList<Questions> questionsArrayList = new ArrayList<>();

    public List<Questions> getQuestions(final quesAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,

                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++){
                    try {
                        String jsonArray = response.getJSONArray(i).get(0).toString();

                        Questions questions = new Questions();
                        questions.setQuest(jsonArray);
                        questions.setAnswer(response.getJSONArray(i).getBoolean(1));

                        questionsArrayList.add(questions);


                      //  Log.d("JSON","onResponse: " + jsonArray);
                      //  Log.d("JSON","onResponseB: " + response.getJSONArray(i).getBoolean(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }


               /* for (int i = 0; i< response.length(); i++){
                    Log.d("JSON","onResponse: " + response);
                }*/
                if (callBack != null){
                    callBack.processFinished(questionsArrayList);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } );

        MySingleton.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionsArrayList;
    }

}
