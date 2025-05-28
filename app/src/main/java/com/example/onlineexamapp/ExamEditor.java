package com.example.onlineexamapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipboardManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collections;

import models.ExamDatabaseHelper;
import models.Question;

public class ExamEditor extends AppCompatActivity {
    private static ArrayList<Question> data;
    private RecyclerView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExamDatabaseHelper examDB = new ExamDatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_editor);

        Bundle b = getIntent().getExtras();
        String quizTitle = b.getString("Quiz Title");

        TextView title = findViewById(R.id.title);
        title.setText(quizTitle);

        Button submit = findViewById(R.id.submit);

        data = new ArrayList<>();
        data.add(new Question());
        listview = findViewById(R.id.listview);
        listview.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter customAdapter = new CustomAdapter(data);
        listview.setAdapter(customAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(listview);

        submit.setOnClickListener(v -> {
            examDB.addExam(quizTitle, data.size());
            int examId = examDB.getIdExamOnTitle(quizTitle);
            data.forEach(question -> {
                Question questionInsert = new Question(question.getQuestion(),question.getOption1(),
                        question.getOption2(),question.getOption3(),question.getOption4(),question.getCorrectAnswer());
                examDB.addQuestion(questionInsert,examId);
            });

//            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("Quiz ID", String.valueOf(quizID));
//            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Create quiz successfully",Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
            int position_dragged = dragged.getAdapterPosition();
            int position_target = target.getAdapterPosition();

            Collections.swap(data, position_dragged, position_target);
            listview.getAdapter().notifyItemMoved(position_dragged, position_target);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    public static class CustomAdapter extends RecyclerView.Adapter<ExamEditor.CustomAdapter.ViewHolder>{
        private final ArrayList<Question> arr;
        public static class ViewHolder extends RecyclerView.ViewHolder{
            private final EditText question;
            private final RadioButton option1rb;
            private final RadioButton option2rb;
            private final RadioButton option3rb;
            private final RadioButton option4rb;
            private final EditText option1et;
            private final EditText option2et;
            private final EditText option3et;
            private final EditText option4et;
            private final LinearLayout new_question;
            private final RadioGroup radio_group;
            public ViewHolder(@NonNull View view) {
                super(view);
                question = view.findViewById(R.id.question);
                option1rb = view.findViewById(R.id.option1rb);
                option2rb = view.findViewById(R.id.option2rb);
                option3rb = view.findViewById(R.id.option3rb);
                option4rb = view.findViewById(R.id.option4rb);
                option1et = view.findViewById(R.id.option1et);
                option2et = view.findViewById(R.id.option2et);
                option3et = view.findViewById(R.id.option3et);
                option4et = view.findViewById(R.id.option4et);
                new_question = view.findViewById(R.id.new_question);
                radio_group = view.findViewById(R.id.radio_group);
            }

            public EditText getQuestion() {
                return question;
            }

            public RadioButton getOption1rb() {
                return option1rb;
            }

            public RadioButton getOption2rb() {
                return option2rb;
            }

            public RadioButton getOption3rb() {
                return option3rb;
            }

            public RadioButton getOption4rb() {
                return option4rb;
            }

            public EditText getOption1et() {
                return option1et;
            }

            public EditText getOption2et() {
                return option2et;
            }

            public EditText getOption3et() {
                return option3et;
            }

            public EditText getOption4et() {
                return option4et;
            }

            public LinearLayout getNew_question() {
                return new_question;
            }
            public RadioGroup getRadio_group(){return radio_group;}
        }

        public CustomAdapter(ArrayList<Question> data){
            arr = data;
        }
        @NonNull
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_edit,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder,@SuppressLint("RecyclerView") int position) {
            holder.setIsRecyclable(false);
            holder.getQuestion().setText(data.get(position).getQuestion());
            holder.getOption1et().setText(data.get(position).getOption1());
            holder.getOption2et().setText(data.get(position).getOption2());
            holder.getOption3et().setText(data.get(position).getOption3());
            holder.getOption4et().setText(data.get(position).getOption4());

            switch (data.get(position).getCorrectAnswer()){
                case 1:
                    holder.getOption1rb().setChecked(true);
                    break;
                case 2:
                    holder.getOption2rb().setChecked(true);
                    break;
                case 3:
                    holder.getOption3rb().setChecked(true);
                    break;
                case 4:
                    holder.getOption4rb().setChecked(true);
                    break;
            }
            holder.getQuestion().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    data.get(position).setQuestion(s.toString());
                }
            });

            holder.getOption1et().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    data.get(position).setOption1(s.toString());
                }
            });

            holder.getOption2et().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    data.get(position).setOption2(s.toString());
                }
            });

            holder.getOption3et().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    data.get(position).setOption3(s.toString());
                }
            });

            holder.getOption4et().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    data.get(position).setOption4(s.toString());
                }
            });

            holder.getRadio_group().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(holder.getOption1rb().isChecked()) data.get(position).setCorrectAnswer(1);
                    if(holder.getOption2rb().isChecked()) data.get(position).setCorrectAnswer(2);
                    if(holder.getOption3rb().isChecked()) data.get(position).setCorrectAnswer(3);
                    if(holder.getOption4rb().isChecked()) data.get(position).setCorrectAnswer(4);

                }
            });

            if(position == (data.size()-1)){
                holder.getNew_question().setVisibility(View.VISIBLE);
                holder.getNew_question().setOnClickListener(v->{
                    data.add(new Question());
                    notifyDataSetChanged();
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}