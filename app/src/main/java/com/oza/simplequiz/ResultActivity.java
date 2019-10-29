package com.oza.simplequiz;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity show the quiz result to the user. It will be called once the user completed the
 * last question in the quiz and click finish.
 */
public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT = "result";
    public static final String EXTRA_TOTAL = "total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Check the required data
        if (getIntent() == null
                || !getIntent().hasExtra(EXTRA_RESULT)
                || !getIntent().hasExtra(EXTRA_TOTAL)) {
            finish();
            return;
        }

        // Extract data from Intent
        int result = getIntent().getIntExtra(EXTRA_RESULT, 0);
        int total = getIntent().getIntExtra(EXTRA_TOTAL, 0);

        // Format result string
        String resultText = getString(R.string.quiz_result, result, total);

        // Show the result
        TextView resultTextView = findViewById(R.id.text_view_result);
        resultTextView.setText(Html.fromHtml(resultText));

    }

}
