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
        registerButton = findViewById(registerButton);

        //Initialize progress bar
        progressBar = findViewById(R.id.progressBar)

        //Initialize firebase authenticator by getting its instance.
        firebaseAuth = FirebaseAuth.getInstance();

        //Validation of all fields where user input is required will be coded for later.
        //Code what happens when the register button is clicked.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Assuming validation of these fields has occurred...
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String userName = editTextUserName.getText().toString().trim();
                String emailAddress = editTextEmailAddress.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String accountType = onRadioButtonClicked(View v);

                //Make the progress bar visible to the user.
                //The progress bar will serve as an indicator to the user that the application is "loading," as it
                //sends the user's information to Firebase to be stored.
                progressBar.setVisibility(View.VISIBLE);

                //Now, let's send the user's information to be stored in Firebase.
                //F
                firebaseAuth.createUserWithEmailAndPassword(emailAddress, password);
            }


    //Code adapted from official Android Developers website
    public String onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.customerAccountButton:
                if (checked)
                    return "Customer";
            case R.id.branchButton:
                if (checked)
                    return "Branch";
            }
        }
    }



}