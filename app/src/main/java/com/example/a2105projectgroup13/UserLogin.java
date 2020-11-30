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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * This class is the activity used to login to an account that has already been registered through the Service Novigrad app.
 * Using an Android Emulator, registering takes an indeterminate time (for an undetermined reason). However, registration
 * is consistently fast when the program is run on a real device.
 */
public class UserLogin extends AppCompatActivity {

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

    private String uid;


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
                startActivity(new Intent(UserLogin.this, UserRegister.class));
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
            Toast.makeText(UserLogin.this, "Invalid email address. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }

        //Both an email and password must be inputted by the user in order to continue.
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(UserLogin.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }

        //Signs in using email and password authentication.
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getUid();


                    firebaseDatabase.getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {
                                FirebaseUser signedInUser = firebaseAuth.getCurrentUser();
                                signedInUser.delete();
                                Toast.makeText(UserLogin.this, "This account was recently deleted by the Admin. Please register again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserLogin.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                DatabaseReference accountType = firebaseDatabase.getReference("Users").child(uid).child("accountType");
                                //Sends logged in user to the main screen.
                                //An Admin will go to the Admin Main Activity.
                                //Other Users will go to the other Main Acitivty.
                                accountType.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.getValue(String.class);

                                        if (value.equals("Branch Account")) {
                                            startActivity(new Intent(UserLogin.this, BranchWelcomeActivity.class));
                                        } else if (value.equals("Admin Account")) {
                                            startActivity(new Intent(UserLogin.this, AdminWelcomeActivity.class));
                                        } else {
                                            startActivity(new Intent(UserLogin.this, CustomerWelcomeActivity.class));
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Toast.makeText(UserLogin.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(UserLogin.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else {
                    Toast.makeText(UserLogin.this, "The password and/or email was incorrect.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(INVISIBLE);
            }
        });
    }

    /**
     * Gets (but does not return) the unique user uId of the user who
     * is currently logged into the app via Firebase Authentication.
     */
    private void getUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }

    /**
     * Override onBackPressed to disable the Back Button.
     * This prevents a logged out User who has returned to the Login activity from
     * entering back into previous activities where he or she was still logged in.
     */
    @Override
    public void onBackPressed() {
    }
}