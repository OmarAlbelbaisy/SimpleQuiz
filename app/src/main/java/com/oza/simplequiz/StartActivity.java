package com.oza.simplequiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the first activity the user will see after opening the application from the home screen.
 * It will contain a simple title and a button, once the user click the button the questions
 * activity will appear
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /**
     * This method will be called once the user clicks the start button
     *
     * @param view the view that has been clicked
     */
    public void startQuiz(View view) {

        // Show questions activity
        startActivity(new Intent(this, QuestionsActivity.class));

        // Exist current activity
        finish();

    }

}
