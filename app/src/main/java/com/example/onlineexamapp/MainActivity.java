package com.example.onlineexamapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineexamapp.databinding.ActivityMainBinding;

import models.UserDatabaseHelper;
import models.User;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        EdgeToEdge.enable(this);
        setContentView(v);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button login = findViewById(R.id.btn_login);
        TextView signUp = findViewById(R.id.signup);
        TextView errMsg = findViewById(R.id.err_message);
        UserDatabaseHelper db =new UserDatabaseHelper(this);

//        signUp.setOnClickListener(view -> {
//            Intent myIntent = new Intent(MainActivity.this, SignUp.class);
//            startActivity(myIntent);
//            finish();
//        });

        binding.signup.setOnClickListener(view->{
            Intent myIntent = new Intent(MainActivity.this, SignUp.class);
            startActivity(myIntent);
            finish();
        });

        login.setOnClickListener(view->{
            User userLogin = new User(email.getText().toString(),password.getText().toString());
            User findUser = db.getUserByPasswordAndEmail(userLogin);
            if(findUser == null){
                errMsg.setText("Wrong email or password!!! ");
            }else{
                Intent myIntent = new Intent(MainActivity.this, Home.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}