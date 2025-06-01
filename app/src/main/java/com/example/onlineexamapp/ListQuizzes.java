package com.example.onlineexamapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import models.ExamDatabaseHelper;
import models.UserDatabaseHelper;
import models.UserQuizzResult;

public class ListQuizzes extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";
    SharedPreferences sharedpreferences;
    String emailShare;
    private String oper;
    private boolean showGrade;
    private boolean solvedQuizzes;
    private boolean createdQuizzes;
    private boolean quizGrades;
//    private ArrayList<String> ids;
    private ArrayList<String> grades;
    private String quizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quizzes);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        emailShare = sharedpreferences.getString(EMAIL_KEY, null);

        UserDatabaseHelper userDB = new UserDatabaseHelper(this);
        oper = getIntent().getStringExtra("Operation");
        TextView title = findViewById(R.id.title);
        ListView listview = findViewById(R.id.listview);
//        ids = new ArrayList<>();
        grades = new ArrayList<>();
        ArrayList<UserQuizzResult> data = new ArrayList<UserQuizzResult>();

        if (oper.equals("List Solved Quizzes")) {
            showGrade = true;
            solvedQuizzes = true;
            data = userDB.getUserExamResult(emailShare);
        } else if (oper.equals("List Created Quizzes")) {
            showGrade = false;
            createdQuizzes = true;
        } else if (oper.equals("List Quiz Grades")) {
            quizID = getIntent().getStringExtra("Quiz ID");
            title.setText(quizID);
            quizGrades = true;
            showGrade = true;
            title.setOnLongClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Quiz ID", quizID);
                clipboard.setPrimaryClip(clip);
                return true;
            });
        }

        ListAdapter listAdapter = new ListAdapter(data);
        listview.setAdapter(listAdapter);
    }

    public class ListAdapter extends BaseAdapter {
        ArrayList<UserQuizzResult> arr;

        ListAdapter(ArrayList<UserQuizzResult> arr2) {
            arr = arr2;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int i) {
            return arr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.quizzes_listitem, null);

            TextView grade = v.findViewById(R.id.grade);
            TextView quiz = v.findViewById(R.id.quiz);
            RelativeLayout item = v.findViewById(R.id.item);

            quiz.setText(arr.get(i).getExam_title());

            if (showGrade) {
                grade.setVisibility(View.VISIBLE);
                grade.setText(arr.get(i).getUser_Total_Point() + "/" + arr.get(i).getExam_Total_Questions());
            } else {
                grade.setVisibility(View.GONE);
            }

            if (solvedQuizzes) {
                item.setOnClickListener(view1 -> {
                    Intent intent = new Intent(ListQuizzes.this, Result.class);
                    intent.putExtra("Operation","Reslut Detail");
                    intent.putExtra("User email",emailShare);
                    intent.putExtra("Quiz Title",arr.get(i).getExam_title());
                    startActivity(intent);
                });
            } else if (createdQuizzes) {
                item.setOnClickListener(view1 -> {
                    Intent intent = new Intent(ListQuizzes.this, ListQuizzes.class);
                    intent.putExtra("Operation", "List Quiz Grades");
//                    intent.putExtra("Quiz ID", ids.get(i));
                    intent.putExtra("Quiz Title", arr.get(i).getExam_title());
                    startActivity(intent);
                });
            } else if (quizGrades) {
                grade.setText(grades.get(i));
                item.setOnClickListener(view1 -> {
                    Intent intent = new Intent(ListQuizzes.this, Result.class);
//                    intent.putExtra("Quiz ID", quizID);
                    intent.putExtra("User email", emailShare);
                    startActivity(intent);
                });
            }

            return v;
        }
    }

}