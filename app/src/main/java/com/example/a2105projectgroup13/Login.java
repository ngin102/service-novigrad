package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * This class is the activity used to login to an account that has already been registered through the Service Novigrad app.
 *
 * NOTE: While testing our login activity in the Android Emulator, we noticed that in some random cases, it takes a fairly long
 * amount of time (40 seconds+) for the user's information to be verified with Firebase and, therefore, for this user's account to be
 * successfully logged in. However, these occurrences seem to have been caused on the server-end by Firebase and not by our app.
 * In the majority of cases, the registration process only took a few seconds to complete (<=5 seconds).
 * These issues did not seem to persist when we tested our app on an Android phone and an Android tablet, which were connected to
 * two different Wifi networks.
 */
public class Login extends AppCompatActivity {

    //Login text fields that will receive user input:
    private EditText editTextEmailAddress, editTextPassword;

    //Buttons:
    private Button loginButton;
    private TextView notRegistered;

    //Progress bar that notifies user of processes in progress:
    private ProgressBar progressBar;

    //Firebase authentication/Database:
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeInstanceVariables();

        //Progress bar is invisible because there was no user interactions.
        progressBar.setVisibility(View.INVISIBLE);

        //When "Don't have an account? Register." is clicked, the user is moved to Register.java
        notRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        //If the user clicks on the login button, the loginOnClick method will be called
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginOnClick(view);
            }
        });
    }

    /**
     * Initializes all instance variables
     */
    private void initializeInstanceVariables() {
        //Initializes login text fields
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        //Initializes everything interacted via clicking
        loginButton = (Button) findViewById(R.id.loginButton);
        notRegistered = (TextView) findViewById(R.id.notRegistered);

        //Progress bar notifies user of ongoing processes
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Variables required to store information in Firebase Authentication and Firebase Database:
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
    }

    /**
     * Takes text from user-inputted text fields to carry out both the account login process
     * through Firebase Authentication.
     */
    private void loginOnClick(View view) {
        //Login process started, so the progress bar is now visible.
        progressBar.setVisibility(VISIBLE);

        //The email must be trimmed, but the password may have whitespace.
        String email = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        //The sole "admin" of this application can login using the username, "admin," and the password, "admin."
        //Firebase Authentication only accepts an email address for a 'username,' so this normally would
        //not be possible. The password, "admin," also does not meet the criteria we have laid out in terms of
        //what is valid for password formatting.
        //To side-step these issues, we will check to see if the user inputted "admin" into the email text field
        //and "admin" into the password text field by the time they clicked "Login." If so, these text prompts will
        //trigger this if statement, which will set the user's inputted email to "admin@admin.ca" and the user's
        //inputted password to "AdminAdmin." The normal login process in this method will continue from here.
        //The "Admin" account was created manually in Firebase Authentication and in Firebase Database using
        //this email address and this password.
        if (email.equals("admin") && password.equals("admin")) {
            email = "admin@admin.ca";
            password = "AdminAdmin";
        }

        //The email must be a possible email address to continue.
        String validateEmail = ValidateString.validateEmail(email);
        if (validateEmail.equals("-1")) {
            Toast.makeText(Login.this, "Invalid email address. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }

        //Both an email and password must be inputted by the user in order to continue.
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }

        //Signs in using email and password authentication.
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(Login.this, "Welcome!", Toast.LENGTH_SHORT).show();


                    //Sends logged in user to the main screen.
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(Login.this, "The password and/or email was incorrect.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(INVISIBLE);
            }
        });
    }
}