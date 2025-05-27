package com.example.onlineexamapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing
    public static final String EMAIL_KEY = "email_key";
    public static final String FIRSTNAME_KEY = "firstname_key";
    public static final String LASTNAME_KEY = "lastname_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String emailShare, firstNameShare, lastNameShare;
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

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        emailShare = sharedpreferences.getString(EMAIL_KEY, null);
        firstNameShare = sharedpreferences.getString(EMAIL_KEY, null);
        lastNameShare = sharedpreferences.getString(EMAIL_KEY, null);

        // check if the fields are not null then one current user is loggedIn
        if (emailShare != null) {
            Intent i = new Intent(MainActivity.this, Home.class);
            startActivity(i);
            finish();
        }
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
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(EMAIL_KEY, findUser.getEmail());
                editor.putString(FIRSTNAME_KEY, findUser.getFirstName());
                editor.putString(LASTNAME_KEY, findUser.getLastName());

                // to save our data with key and value.
                editor.apply();

                Intent myIntent = new Intent(MainActivity.this, Home.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}