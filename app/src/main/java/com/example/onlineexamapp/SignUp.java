package com.example.onlineexamapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import models.UserDatabaseHelper;
import models.User;

public class SignUp extends AppCompatActivity {

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        Button btn_create = findViewById(R.id.btn_create);
        TextView btn_already = findViewById(R.id.btn_already);
        TextView err_message = findViewById(R.id.err_message);

        btn_already.setOnClickListener(view->{
            Intent i = new Intent(SignUp.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        UserDatabaseHelper db = new UserDatabaseHelper(this);
        btn_create.setOnClickListener(view->{
            boolean emailValidate = Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
            if (!emailValidate){
                err_message.setText("Invalid email");
                return;
            }
            if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                err_message.setText("Password Not matching");
                return;
            }
            err_message.setText("");

            User findUserExist = db.getUserByPasswordAndEmail(new User(email.getText().toString()));
            if(findUserExist != null){
                err_message.setText("Email already exist");
                return;
            }
            db.addUser(new User(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),password.getText().toString()));
            Intent myIntent = new Intent(SignUp.this, Home.class);
            startActivity(myIntent);
            finish();
        });

    }
}