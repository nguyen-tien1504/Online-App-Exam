package com.example.onlineexamapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import models.Question;
import models.UserDatabaseHelper;

public class Result extends AppCompatActivity {

    private ArrayList<Question> data;
    private String quizTitle;
    private int quizpoint;
    private String oper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        quizTitle = getIntent().getStringExtra("Quiz Title");
        quizpoint = getIntent().getIntExtra("Quiz points",0);
        oper = getIntent().getStringExtra("Operation");


        TextView title = findViewById(R.id.title);
        title.setText(quizTitle);
        ListView listview = findViewById(R.id.listview);
        TextView total = findViewById(R.id.total);
        TextView pointsView = findViewById(R.id.points);
        Button homebtn = findViewById(R.id.homebtn);

        if(oper.equals("Exam Result")){
            data = (ArrayList<Question>)getIntent().getSerializableExtra("Data");
        } else if (oper.equals("Reslut Detail")) {
            UserDatabaseHelper userDb = new UserDatabaseHelper(this);
            String userEmail = getIntent().getStringExtra("User email");
            data = userDb.getUserRedultDetailt(userEmail,quizTitle);
        }

        int totalAnswerChecked = (int)data.stream().filter(question -> question.getSelectedAnswer()!=0).count();
        total.setText("Total " + totalAnswerChecked + "/" + data.size());
        pointsView.setText("Points: " + quizpoint);

        ListAdapter listAdapter = new ListAdapter(data);
        listview.setAdapter(listAdapter);

        homebtn.setOnClickListener(v->{
            Intent i =new Intent(Result.this, Home.class);
            startActivity(i);
            finish();
        });
    }

    public class ListAdapter extends BaseAdapter {
        ArrayList<Question> arr;

        ListAdapter(ArrayList<Question> arr2) {
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
            View v = inflater.inflate(R.layout.question, null);

            TextView question = v.findViewById(R.id.question);
            RadioButton option1 = v.findViewById(R.id.option1);
            RadioButton option2 = v.findViewById(R.id.option2);
            RadioButton option3 = v.findViewById(R.id.option3);
            RadioButton option4 = v.findViewById(R.id.option4);
            TextView result = v.findViewById(R.id.result);

            question.setText(data.get(i).getQuestion());
            option1.setText(data.get(i).getOption1());
            option2.setText(data.get(i).getOption2());
            option3.setText(data.get(i).getOption3());
            option4.setText(data.get(i).getOption4());

            switch (data.get(i).getSelectedAnswer()) {
                case 1:
                    option1.setChecked(true);
                    break;
                case 2:
                    option2.setChecked(true);
                    break;
                case 3:
                    option3.setChecked(true);
                    break;
                case 4:
                    option4.setChecked(true);
                    break;
            }

            option1.setEnabled(false);
            option2.setEnabled(false);
            option3.setEnabled(false);
            option4.setEnabled(false);

            result.setVisibility(View.VISIBLE);

            if (data.get(i).getSelectedAnswer()==data.get(i).getCorrectAnswer()) {
                result.setBackgroundResource(R.drawable.green_background);
                result.setTextColor(ContextCompat.getColor(Result.this, R.color.green_dark));
                result.setText("Correct Answer");
            } else {
                result.setBackgroundResource(R.drawable.red_background);
                result.setTextColor(ContextCompat.getColor(Result.this, R.color.red_dark));
                result.setText("Wrong Answer");

                switch (data.get(i).getCorrectAnswer()) {
                    case 1:
                        option1.setBackgroundResource(R.drawable.green_background);
                        break;
                    case 2:
                        option2.setBackgroundResource(R.drawable.green_background);
                        break;
                    case 3:
                        option3.setBackgroundResource(R.drawable.green_background);
                        break;
                    case 4:
                        option4.setBackgroundResource(R.drawable.green_background);
                        break;
                }

            }
            return v;
        }
    }
}