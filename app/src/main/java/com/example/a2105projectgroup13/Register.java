package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

    //NOTE: OnCreate is the method that is first run when the activity starts!
        //Treat it almost like the "main" of this Register class.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize needed instance varaibles by calling the initializeInstanceVariables method within this class.
        this.initializeInstanceVariables();

        //Let's code for what happens when the register button is clicked on by the user.
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }

        });
    }

    //Method to initialize instance variables
    private void initializeInstanceVariables(){
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


    //Code adapted from official Android Developers website:
        //In the xml, we have implemented radio buttons which determine whether the user is setting up a customer account or a branch account.
        //This method, onRadioButtonClicked, determines which radio button has been selected by the user.
        //It will return a string indicating which button was selected; the string represents the account type.
    private String onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.customerAccountButton:
                if (checked) {
                    return "customer";
                }
            case R.id.branchButton:
                if (checked) {
                    return "branch";
                }
        }
    }

}

