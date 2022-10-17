package com.example.triviaquizpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaquizpractice.data.Prefs;
import com.example.triviaquizpractice.data.QuestionBank;
import com.example.triviaquizpractice.data.quesAsyncResponse;
import com.example.triviaquizpractice.model.Questions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGE_ID = "message_id";

    TextView questText, quesCount, scoreText, lastHighScoreText;
    Button tBtn, fBtn, jBtn;
    ImageButton nBtn, pBtn;
    EditText jumpEditText;
    CardView cView;
    int currentIndex = 0;
    int quesIndex = 1;
    int score = 0;


    String editTextValue;

    private  List<Questions> questionsList;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questText = findViewById(R.id.questTextView);
        quesCount = findViewById(R.id.count);
        scoreText = findViewById(R.id.score);
        lastHighScoreText = findViewById(R.id.lastHcore);

        jumpEditText = findViewById(R.id.quesNumberEdit);

        jBtn = findViewById(R.id.jumpBtn);

        tBtn = findViewById(R.id.trueBtn);
        fBtn = findViewById(R.id.falseBtn);
        nBtn = findViewById(R.id.nexButton);
        pBtn = findViewById(R.id.preButton);

         cView = findViewById(R.id.cardView);

        tBtn.setOnClickListener(this);
        fBtn.setOnClickListener(this);
        nBtn.setOnClickListener(this);
        pBtn.setOnClickListener(this);

        jBtn.setOnClickListener(this);

        prefs = new Prefs(MainActivity.this);

        currentIndex = prefs.getState();

        Log.d("state_check","onIndex: "+currentIndex);


         questionsList = new QuestionBank().getQuestions(new quesAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Questions> questionsArrayList) {
               questText.setText(questionsArrayList.get(currentIndex).getQuest());
                quesCount.setText((quesIndex + currentIndex)+" / "+ questionsList.size());


            }
        });





        lastHighScoreUpdate();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.trueBtn:
                checkAnswer(true);

                updateQuestion();
                break;

            case R.id.falseBtn:
                checkAnswer(false);

                updateQuestion();

                break;

            case R.id.nexButton:

                goNext();

                break;

            case R.id.preButton:
                if (currentIndex > 0){
                    currentIndex--;
                    updateQuestion();
                   quesCountNumberUpdate();
                  //  updateCardColor();
                }
                break;

            case R.id.jumpBtn:
                editTextValue = jumpEditText.getText().toString();
                int myEditInt = Integer.parseInt(editTextValue) -1;
                currentIndex = myEditInt;

                if(currentIndex < questionsList.size()) {

                    if (currentIndex > 0){
                        updateQuestion();
                        quesCountNumberUpdate();
                    }


                  //  updateCardColor();
                }
              //  Toast.makeText(MainActivity.this, ""+currentIndex, Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void updateQuestion(){
        questText.setText(questionsList.get(currentIndex).getQuest());
    }

    private void quesCountNumberUpdate(){
        quesCount.setText((quesIndex + currentIndex)+" / "+ questionsList.size());
    }

    private void scoreUpdate(){

       /* SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("mes_key",score+"");
        editor.apply();*/

        prefs.saveHighScore(score);

        scoreText.setText("Score: "+score);
    }

    private void lastHighScoreUpdate(){

       /* SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        String value = sharedPreferences.getString("mes_key","0");*/



        lastHighScoreText.setText("LastHighScore: "+prefs.getHighScore());
    }

  /*  private void updateCardColor(){
        cView.setCardBackgroundColor(getResources().getColor(R.color.white));
    }*/

    private void checkAnswer(boolean userAns){
        boolean answerTrue = questionsList.get(currentIndex).getAnswer();
        if (answerTrue == userAns){
            fadeAnimation();
            addScore();
            toastMessage("That\'s Correct");
        } else {
            shakeAnimation();
            minusScore();
            toastMessage("That\'s Wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void addScore() {
        score+=10;
        scoreUpdate();
    }

    private void minusScore() {
        if (score > 0){
            score-=10;
            scoreUpdate();
        }
    }

    private void fadeAnimation(){

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(/*int color*/){
        Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);

      //  cView.setCardBackgroundColor(getResources().getColor(color));
        cView.setAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void goNext(){
        if(currentIndex < questionsList.size() - 1){
            currentIndex++;
            updateQuestion();
            quesCountNumberUpdate();
            // updateCardColor();
        }
    }


    @Override
    protected void onPause() {
        prefs.setState(currentIndex);
        super.onPause();
    }
}