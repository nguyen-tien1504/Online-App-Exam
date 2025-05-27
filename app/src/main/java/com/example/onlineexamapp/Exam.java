package com.example.onlineexamapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

import models.ExamDatabaseHelper;
import models.Question;

public class Exam extends AppCompatActivity {
    private ArrayList<Question> data;
    private String quizTitle;
//    private int oldTotalPoints = 0;
//    private int oldTotalQuestions = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExamDatabaseHelper examDB = new ExamDatabaseHelper(this);
        super.onCreate(savedInstanceState);
        quizTitle = getIntent().getStringExtra("Quiz Title");
        ListView listview = findViewById(R.id.listview);
        Button submit = findViewById(R.id.submit);
        TextView title = findViewById(R.id.title);
        setContentView(R.layout.activity_exam);
        title.setText(quizTitle);

        data = examDB.getQuestionFromExamTitle(quizTitle);
        submit.setOnClickListener(v->{
            int points = (int) data.stream().filter(question -> question.getSelectedAnswer() == question.getCorrectAnswer()).count();
            Intent i = new Intent(Exam.this, Result.class);
            Bundle bundle = new Bundle();
            i.putExtra("Quiz Title", quizTitle);
            i.putExtra("Quiz points", points);
            i.putExtra("Data",(Serializable) data);
            startActivity(i);
            finish();
        });
    }

    public class ListAdapter extends BaseAdapter{
        Question[] arr;
        ListAdapter (Question[] arr2){
            arr = arr2;
        }
        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return arr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.question,null);

            TextView question = v.findViewById(R.id.question);
            RadioButton option1 = v.findViewById(R.id.option1);
            RadioButton option2 = v.findViewById(R.id.option2);
            RadioButton option3 = v.findViewById(R.id.option3);
            RadioButton option4 = v.findViewById(R.id.option4);

            question.setText(data.get(i).getQuestion());
            option1.setText(data.get(i).getOption1());
            option2.setText(data.get(i).getOption2());
            option3.setText(data.get(i).getOption3());
            option4.setText(data.get(i).getOption4());

            option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) data.get(i).setSelectedAnswer(1);
                }
            });
            option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) data.get(i).setSelectedAnswer(2);
                }
            });
            option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) data.get(i).setSelectedAnswer(3);
                }
            });
            option4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) data.get(i).setSelectedAnswer(4);
                }
            });

            switch (data.get(i).getSelectedAnswer()){
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
            return v;
        }
    }
}