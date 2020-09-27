package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Register extends AppCompatActivity {
    //Declare instance variables for everything the user inputs on the register screen/activity.
    //Each variable corresponds with their id in the activity.
    private EditText editTextFirstName, editTextLastName, editTextUserName, editTextEmailAddress, editTextPassword;
    private Button registerButton;
    private RadioButton accountTypeButton;
    private TextView alreadyRegistered;

    //Declaring instance variable for progress bar and for Firebase connection.
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    //NOTE: OnCreate is the method that is first run when the activity starts!
    //Treat it almost like the "main" of this Register class.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initializeInstanceVariables();

        //The progress bar will initially start off as invisible to the user.
        progressBar.setVisibility(INVISIBLE);

        //If the user clicks on the prompt that says: "Already registered? Sign in," they will be redirected to the login page.
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        //Let's code for what happens when the register button is clicked on by the user.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Once the user clicks the register button, the progress bar will become visible to them to indicate that their
                //information is being processed to Firebase.
                progressBar.setVisibility(VISIBLE);

                //Validation of proper inputs for all text fields on the register screen is to be added later.

                //Let's say all inputs are validated...so, send the information to the Firebase database.

                //Convert whatever was inputted by the user into the text fields on the register page to strings.
                //Trim any potential whitespace from the inputted email address. The password is allowed to have whitespace.
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String userName = editTextUserName.getText().toString().trim();
                String emailAddress = editTextEmailAddress.getText().toString().trim();
                String password = editTextPassword.getText().toString();


                //We will now create a user on Firebase using the inputted email address and password.
                //Other specifications added by the user, like username and account type will be accounted for later.
                //Firebase only creates user accounts using an email address and a password.
                firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    //On completion of the process of adding a user to the Firebase database...

                    //Code adapted from "createAccount" method provided by Android Studio in Tools --> Firebase --> Authentication
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Account registered!", Toast.LENGTH_SHORT).show();
                            //Since the registration was successful, send the user to the main activity.
                            startActivity(new Intent(Register.this, MainActivity.class));
                            //Indicate that the above process is complete, even if registration was not successful.
                            progressBar.setVisibility(INVISIBLE);
                        } else {
                            //If the registration was not successful, give the user this message prompt.
                            Toast.makeText(Register.this, "There was a problem registering your account. Please try again.", Toast.LENGTH_SHORT).show();
                            //Indicate that the above process is complete, even if registration was not successful.
                            progressBar.setVisibility(INVISIBLE);
                        }
                    }
                });

            }

        });

    }

    private void initializeInstanceVariables(){
        //Initialize each instance variable by finding the first view that corresponds with their id in the activity.
        editTextFirstName = (EditText)findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText)findViewById(R.id.editTextLastName);
        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextEmailAddress = (EditText)findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        registerButton = (Button)findViewById(R.id.registerButton);
        alreadyRegistered = (TextView)findViewById(R.id.alreadyRegistered);

        //Initialize progress bar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Initialize firebase authenticator by getting its instance.
        firebaseAuth = FirebaseAuth.getInstance();
    }
}