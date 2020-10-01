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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Register extends AppCompatActivity {
    //Declare instance variables.
        //Each variable corresponds with their id in the xml.

    //Text fields that will receive user text inputs:
    private EditText editTextFirstName, editTextLastName, editTextEmailAddress, editTextPassword;

    //Buttons:
    private RadioGroup accountTypeRadioGroup; 
    private Button registerButton;
    private TextView alreadyRegistered; //While this a TextView, it the user will click on it to be redirected to the main activity.

    //Cosmetic details on Registration screen:
    private ProgressBar progressBar;

    //Variables required to store information in Firebase Authentication and Firebase Database:
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    //NOTE: OnCreate is the method that is first run when the activity starts!
        //Treat it almost like the "main" of this Register class.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the instance variables using this method.
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
            public void onClick(View v) {
                RegisterOnClick(v);
            }
        });
    }


    private void initializeInstanceVariables(){
        //Initialize each instance variable by finding the first view that corresponds with their id in the activity.
        editTextFirstName = (EditText)findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText)findViewById(R.id.editTextLastName);
        editTextEmailAddress = (EditText)findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        accountTypeRadioGroup = (RadioGroup)findViewById(R.id.accountTypeRadioGroup);
        registerButton = (Button)findViewById(R.id.registerButton);
        alreadyRegistered = (TextView)findViewById(R.id.alreadyRegistered);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
    }


    public void RegisterOnClick(View v){
        //Once the user clicks the register button, the progress bar will become visible to them to indicate that their
        //information is being processed to Firebase.
        progressBar.setVisibility(VISIBLE);

        //Convert whatever was inputted by the user into the text fields on the register page to strings.
        //Trim any potential whitespace from the inputted email address. The password is allowed to have whitespace.
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String emailAddress = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString();


        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty()) {
            Toast.makeText(Register.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }

        String accountType = checkRadioButtonChoice(accountTypeRadioGroup);
        if (accountType.equals("-1")){
            Toast.makeText(Register.this, "Please select an account type.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }

        String validatedFirstName = ValidateString.validateName(firstName);
        String validatedLastName = ValidateString.validateName(lastName);
        String validatedEmail = ValidateString.validateEmail(emailAddress);
        String validatedPassword = ValidateString.validatePassword(password);

        if (validatedFirstName.equals("-1")) {
            Toast.makeText(Register.this, "Invalid first name. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            firstName = validatedFirstName;
        }

        if (validatedLastName.equals("-1")){
            Toast.makeText(Register.this, "Invalid last name. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            lastName = validatedLastName;
        }

        if (validatedEmail.equals("-1")){
            Toast.makeText(Register.this, "Invalid email address. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
        } else {
            emailAddress = validatedEmail;

        } if (validatedPassword.equals("-1")){
            Toast.makeText(Register.this, "Passwords must contain 1+ numbers, 1+ uppercase letters, and 8+ characters total.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            password = validatedPassword;
        }


        final User userInfo = new User(firstName, lastName, accountType);

        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Account registered!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(INVISIBLE);
                                finish();
                                startActivity(new Intent(Register.this, MainActivity.class));
                            } else {
                                //If the registration was not successful, give the user this message prompt.
                                Toast.makeText(Register.this, "There was a problem saving your account information. Please try again.", Toast.LENGTH_SHORT).show();
                                //Indicate that the above process is complete, even if registration was not successful.
                                progressBar.setVisibility(INVISIBLE);
                            }
                        }
                    });
                } else {
                    //If the registration was not successful, give the user this message prompt.
                    Toast.makeText(Register.this, "There was a problem registering your account. Please try again.", Toast.LENGTH_SHORT).show();
                    //Indicate that the above process is complete, even if registration was not successful.
                    progressBar.setVisibility(INVISIBLE);
                }
            }
        });
    }

    public String checkRadioButtonChoice(RadioGroup radioGroup){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if (radioButtonId == -1 ){
            Toast.makeText(Register.this, "Please select an account type.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return "-1";
        }
        RadioButton selection = (RadioButton)radioGroup.findViewById(radioButtonId);
        String selectionString = (String) selection.getText().toString();

        return selectionString;
    }
}