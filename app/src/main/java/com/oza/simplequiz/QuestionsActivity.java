package com.oza.simplequiz;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * This activity will show the quiz questions. In the beginning, it will show the first question
 * along with its choices and a button that the user will use submit the selected answer and move
 * to the next question.
 */
public class QuestionsActivity extends AppCompatActivity {

    private final String EXTRA_RESULT = "result";
    private final String EXTRA_COUNTER = "counter";
    private final String EXTRA_SELECTED_ID = "selected_id";

    /**
     * An integer variable to save the total correct answers
     */
    private int result = 0;

    /**
     * An Integer to be used as an index to the current question
     */
    private int counter = 0;

    /**
     * A string array that will hold all the quiz questions
     */
    private String[] questions;

    /**
     * A string array that will hold all the correct answers
     */
    private String[] correctAnswers;

    /**
     * Array of arrays that hold all answers for all questions
     * Each item is an array that hold answers of one question
     */
    private String[][] answers;

    /**
     * References to user interface components
     */
    private TransitionDrawable correctAnswerTransition;
    private TransitionDrawable wrongAnswerTransition;
    private TextView counterTextView;
    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private RadioButton firstChoiceRadioButton;
    private RadioButton secondChoiceRadioButton;
    private RadioButton thirdChoiceRadioButton;
    private Button confirmNextButton;

    /**
     * An OnClickListener that will be used to handle confirm action
     */
    private View.OnClickListener confirmClickListener;

    /**
     * An OnClickListener that will be used to handle moving to next question
     */
    private View.OnClickListener nextClickListener;

    /**
     * An OnClickListener that will be used to handle finish quiz action
     */
    private View.OnClickListener finishClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Initialize all data variables
        initData();

        // Initialize all user interface objects
        initComponents();

        // Initialize all needed listeners
        initListener();

        if (savedInstanceState == null) {
            // Show the first question
            nextQuestion();
        } else {
            result = savedInstanceState.getInt(EXTRA_RESULT);
            counter = savedInstanceState.getInt(EXTRA_COUNTER);
            int selectedRadioButtonId = savedInstanceState.getInt(EXTRA_SELECTED_ID);
            if (selectedRadioButtonId != -1) {
                // Restore the question and selection state
                restoreSelectedAnswer(selectedRadioButtonId);
            } else {
                // Restore the question
                nextQuestion();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(EXTRA_RESULT, result);
        outState.putInt(EXTRA_COUNTER, counter);
        // Save current selection if the question already confirmed
        if (!confirmNextButton.getText().equals(getString(R.string.confirm))) {
            outState.putInt(EXTRA_SELECTED_ID, answersRadioGroup.getCheckedRadioButtonId());
        } else {
            outState.putInt(EXTRA_SELECTED_ID, -1);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Read all the questions data from xml resources and assign it to java arrays
     */
    private void initData() {
        questions = getResources().getStringArray(R.array.questions);
        correctAnswers = getResources().getStringArray(R.array.correctAnswers);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.answers);
        answers = new String[questions.length][3];
        for (int i = 0; i < questions.length; i++) {
            answers[i] = getResources().getStringArray(typedArray.getResourceId(i, 0));
        }
        typedArray.recycle();
    }

    /**
     * Initialize all UI components
     */
    private void initComponents() {
        correctAnswerTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.transition_correct_answer);
        wrongAnswerTransition = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.transition_wrong_answer);
        counterTextView = findViewById(R.id.text_view_counter);
        questionTextView = findViewById(R.id.text_view_question);
        answersRadioGroup = findViewById(R.id.radio_group_answers);
        firstChoiceRadioButton = findViewById(R.id.radio_button_first_answer);
        secondChoiceRadioButton = findViewById(R.id.radio_button_second_answer);
        thirdChoiceRadioButton = findViewById(R.id.radio_button_third_answer);
        confirmNextButton = findViewById(R.id.button_next_confirm);
    }

    /**
     * Initialize and create new OnClickListeners objects
     */
    private void initListener() {
        confirmClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAnswer();
            }
        };
        nextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        };
        finishClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishQuiz();
            }
        };
    }

    /**
     * This method used to confirm the current answer and save the result.It will also change the
     * status of the confirmNextButton to next or finish depending on the current question index
     */
    private void confirmAnswer() {

        // Get a reference to the selected radio button
        RadioButton selectedRadioButton = findViewById(answersRadioGroup.getCheckedRadioButtonId());

        // Check if the user select an answer or not
        if (selectedRadioButton != null) {
            // Read selected answer string
            String selectedAnswer = selectedRadioButton.getText().toString();
            // If the user selected the correct answer
            if (selectedAnswer.equals(correctAnswers[counter])) {
                // Indicate the answer was correct by changing background color to green
                selectedRadioButton.setBackground(correctAnswerTransition);
                correctAnswerTransition.startTransition(500);
                // Increase the result
                result++;
            } else {
                // Indicate the answer was wrong by changing background color to red
                selectedRadioButton.setBackground(wrongAnswerTransition);
                wrongAnswerTransition.startTransition(500);
            }
            // If the current question is the last one in the quiz
            if (counter == questions.length - 1) {
                // Change confirmNextButton state to finish
                confirmNextButton.setText(getString(R.string.finish));
                confirmNextButton.setOnClickListener(finishClickListener);
            } else {
                // Change confirmNextButton state to next
                confirmNextButton.setText(getString(R.string.next));
                confirmNextButton.setOnClickListener(nextClickListener);
            }

            // Increase the index to point to next question
            counter++;

        } else {
            // Show a short message to the user to tell him he must select an answer before confirm!
            Toast.makeText(this, getString(R.string.no_answer_selected), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This method will reset the UI and show the next question
     */
    private void nextQuestion() {

        // Clear previous selection if any
        answersRadioGroup.clearCheck();

        // Reset radio buttons background to transparent
        firstChoiceRadioButton.setBackgroundColor(Color.TRANSPARENT);
        secondChoiceRadioButton.setBackgroundColor(Color.TRANSPARENT);
        thirdChoiceRadioButton.setBackgroundColor(Color.TRANSPARENT);

        // Show the current question index
        int currentQuestion = counter + 1;
        counterTextView.setText(getString(R.string.counter, currentQuestion, questions.length));

        // Change the question
        questionTextView.setText(questions[counter]);

        // Change the answers
        firstChoiceRadioButton.setText(answers[counter][0]);
        secondChoiceRadioButton.setText(answers[counter][1]);
        thirdChoiceRadioButton.setText(answers[counter][2]);

        // Change confirmNextButton state to confirm!
        confirmNextButton.setText(getString(R.string.confirm));
        confirmNextButton.setOnClickListener(confirmClickListener);

    }

    /**
     * Show the ResultActivity and exist the current activity
     */
    private void finishQuiz() {

        // Create an explicit intent to open the ResultActivity
        Intent intent = new Intent(this, ResultActivity.class);

        // Pass the result to ResultActivity via Intent
        intent.putExtra(ResultActivity.EXTRA_RESULT, result);
        intent.putExtra(ResultActivity.EXTRA_TOTAL, questions.length);

        // Open ResultActivity
        startActivity(intent);

        // Exit current activity
        finish();

    }

    /**
     * Restore selected answer after configuration changes
     *
     * @param selectedRadioButtonId selected answer radio button id
     */
    private void restoreSelectedAnswer(int selectedRadioButtonId) {
        // Back to current question
        counter--;
        // Show the question
        nextQuestion();
        // Restore correction indicator
        RadioButton radioButton = findViewById(selectedRadioButtonId);
        if (radioButton.getText().equals(correctAnswers[counter])) {
            radioButton.setBackgroundResource(R.drawable.shape_correct_answer);
        } else {
            radioButton.setBackgroundResource(R.drawable.shape_wrong_answer);
        }
        // Increase counter to show next question after clicking next
        counter++;
        // Restore the previous button functionality
        if (counter == questions.length) {
            confirmNextButton.setText(R.string.finish);
            confirmNextButton.setOnClickListener(finishClickListener);
        } else {
            confirmNextButton.setText(R.string.next);
            confirmNextButton.setOnClickListener(nextClickListener);
        }
    }

}
