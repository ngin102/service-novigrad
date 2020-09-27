package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    //Declaring instance variables for everything the user inputs on the register screen/activity.
    //Each variable corresponds with their id in the activity.
    EditText editTextFirstName, editTextLastName, editTextUserName, editTextEmailAddress, editTextPassword;
    Button registerButton;
    TextView alreadyRegistered;

    //Declaring instance variable for progress bar and for Firebase connection.
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    //onCreate is called when
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize each instance variable by finding the first view that corresponds with their id in the activity.
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmailAddress = findViewById(editTextEmailAddress);
        editTextPassword = findViewById(editTextPassword);

        //Initialize progress bar
        progressBar = findViewById(R.id.progressBar)

        //Initialize firebase authenticator by getting its instance.
        firebaseAuth = FirebaseAuth.getInstance();
    }


}