package com.example.onlineexamapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.User;
import models.UserDatabaseHelper;

public class Home extends AppCompatActivity {
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";
    public static final String FIRSTNAME_KEY = "firstname_key";
    public static final String LASTNAME_KEY = "lastname_key";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String emailShare, firstNameShare, lastNameShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserDatabaseHelper userDB = new UserDatabaseHelper(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        emailShare = sharedpreferences.getString(EMAIL_KEY, null);
        firstNameShare = sharedpreferences.getString(FIRSTNAME_KEY, null);
        lastNameShare = sharedpreferences.getString(LASTNAME_KEY, null);

        int userTotalPoints = userDB.getTotalQuestionsAndPointsFromEmail(emailShare);

        TextView name = findViewById(R.id.name);
        name.setText(firstNameShare + " " + lastNameShare);
        TextView total_questions = findViewById(R.id.total_questions);
        //total_questions.setText(userTotalPointsAndQuestions.getTotalPoints());
        TextView total_points = findViewById(R.id.total_points);
        total_points.setText(String.valueOf(userTotalPoints));
        Log.i("HOme","Msg"+String.valueOf(userTotalPoints));
        Button startQuiz = findViewById(R.id.startQuiz);
        Button createQuiz = findViewById(R.id.createQuiz);
        RelativeLayout solvedQuizzes = findViewById(R.id.solvedQuizzes);
        RelativeLayout your_quizzes = findViewById(R.id.your_quizzes);
        EditText quiz_title = findViewById(R.id.quiz_title);
        EditText start_quiz_id = findViewById(R.id.start_quiz_id);
        ImageView signout = findViewById(R.id.signout);

        signout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();

            Intent i = new Intent(Home.this, MainActivity.class);
            startActivity(i);
            finish();
        });
        createQuiz.setOnClickListener(v -> {
            if (quiz_title.getText().toString().equals("")) {
                quiz_title.setError("Quiz title cannot be empty");
                return;
            }
            Intent i = new Intent(Home.this, ExamEditor.class);
            i.putExtra("Quiz Title", quiz_title.getText().toString());
            quiz_title.setText("");
            startActivity(i);
        });

        startQuiz.setOnClickListener(v-> {
            if (start_quiz_id.getText().toString().equals("")) {
                start_quiz_id.setError("Quiz title cannot be empty");
                return;
            }
            Intent i = new Intent(Home.this, Exam.class);
            i.putExtra("Quiz Title", start_quiz_id.getText().toString());
            start_quiz_id.setText("");
            startActivity(i);
        });

        solvedQuizzes.setOnClickListener(v -> {
            Intent i = new Intent(Home.this, ListQuizzes.class);
            i.putExtra("Operation", "List Solved Quizzes");
            startActivity(i);
        });

        your_quizzes.setOnClickListener(v -> {
            Intent i = new Intent(Home.this, ListQuizzes.class);
            i.putExtra("Operation", "List Created Quizzes");
            startActivity(i);
        });
    }
}