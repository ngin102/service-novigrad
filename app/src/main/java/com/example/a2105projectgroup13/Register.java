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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * This class is the activity that is used to register an account through the Service Novigrad app.
 * The account registration process is implemented using two Firebase services: Firebase Authentication and Firebase Database.
 * Firebase Authentication will be used to store the user's login credentials.
 * Firebase Database will be used to store any extra information about the user that is not stored in Firebase Authentication.
 *
 * On the registration screen (the corresponding xml to this class), the user will input text into the designated text fields that reflect
 * the information the user must provide to register an account: the user's first name, last name, email address, and account password.
 * These four pieces of information, as well as indication by the user as to whether he or she wishes to create a Branch account or
 * a regular Customer account, must be provided before the user can register an account or even send off their information to
 * any of the two Firebase services. The activity uses methods to validate this information (that each piece of information meets
 * formatting standards required to register an account – see the ValidateString class for more information) and to ensure that the user
 * has not left any of the text fields blank before the user can proceed with the registration process.
 *
 * Once the user's inputted information is validated, the account registration process via Firebase will begin.
 * The user's inputted email address and password will be stored in Firebase Authentication, alongside a unique user uId that is linked to this info.
 * THe user can now use Firebase Authentication to log into the application (as long as the next steps are successful).
 * The user's other information provided at the time of registration (first name, last name, and account type) will then be stored in
 * Firebase Database. In Firebase Database, these three pieces of information will be stored under the unique user uId that was previously generated
 * by Firebase Authentication and is currently linked to the user's email address and password in that service. The user's unique uId, as well as
 * those of other users registered into our app (their uIds are unique as well), can be found in our Firebase Database under the "Users" path.
 *
 * After all information is successfully stored in Firebase Authentication and Firebase Database, the user will be redirected to the
 * main activity/the welcome screen to start using the app.
 *
 * Returning registered users to the app can bypass this registration activity (as it is the app's launcher activity) by selecting a text prompt
 * to take them to the login activity, where they can log in using the email address and password they provided at registration and allowed to be
 * stored in Firebase Authentication.
 *
 * NOTE: While testing our registration activity in the Android Emulator, we noticed that in some random cases, it takes a fairly long
 * amount of time (40 seconds+) for the user's information to be sent to Firebase and, therefore, for this user's account to be successfully
 * registered. However, these occurrences seem to have been caused on the server-end by Firebase and not by our app.
 * In the majority of cases, the registration process only took a few seconds to complete (<=5 seconds).
 * These issues did not seem to persist when we tested our app on an Android phone and an Android tablet, which were connected to
 * two different Wifi networks.
 */
public class Register extends AppCompatActivity {
    //Declare instance variables.
        //Each variable corresponds with its id in the xml.

    //Text fields that will receive user text inputs:
    private EditText editTextFirstName, editTextLastName, editTextEmailAddress, editTextPassword;

    //Buttons:
    private RadioGroup accountTypeRadioGroup; //This radio group contains the two radio buttons implemented in the xml.
                                              //It will be used to see which radio button the user checked on the registration screen.
    private Button registerButton;
    private TextView alreadyRegistered; //While this is a TextView (not really a button), the user will click on it to be redirected to the Login activity.

    //Cosmetic details on Registration screen:
    private ProgressBar progressBar; //The progress bar serves as a visual indicator to the user that his or her information is being sent to Firebase.

    //Variables required to store information in Firebase Authentication and Firebase Database:
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private String loginUid;



    /**
     * Initializes the activity.
     */
    //NOTE (for our group): OnCreate is the method that is first run when the activity starts!
        //Treat it almost like the "main" of this Register activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the instance variables using this method.
        this.initializeInstanceVariables();

        //Since the user has not clicked on any buttons to register yet, the progress bar will not yet be visible to the user.
        progressBar.setVisibility(INVISIBLE);

        //If the user clicks on the text prompt that says, "Already registered? Sign in.", they will be redirected to the login page.
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        //If the user clicks on the register button, the registerOnClick method will be called (this method will carry out the
        //entire account registration process of this activity.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOnClick(v);
            }
        });
    }


    /**
     * Initializes instance variables.
    */
    private void initializeInstanceVariables(){
        //Initialize each instance variable by finding the first view that corresponds with its id.

        //Text fields that will receive user text inputs:
        editTextFirstName = (EditText)findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText)findViewById(R.id.editTextLastName);
        editTextEmailAddress = (EditText)findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        //Buttons:
        accountTypeRadioGroup = (RadioGroup)findViewById(R.id.accountTypeRadioGroup);
        registerButton = (Button)findViewById(R.id.registerButton);
        alreadyRegistered = (TextView)findViewById(R.id.alreadyRegistered); //Not really a button.

        //Cosmetic details on Registration screen:
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Variables required to store information in Firebase Authentication and Firebase Database:
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
    }


     /**
      * Takes text from user-inputted text fields to carry out both the account registration process
      * through Firebase Authentication and the user information storing process through Firebase Database.
      * User-inputted text is validated (using methods from ValidateString) before it is sent to any
      * Firebase services.
      *
      * The sections of this method dealing with Firebase were adapted from the "Firebase Authentication"
      * slides found under "Additional Material" on our official course Brightspace page.
      * @CREDIT to Khalid Alam for those implementations.
     */
    public void registerOnClick(View v){
        //Once the user clicks the register button, the progress bar will become visible to them to indicate that their
        //information is being processed to Firebase.
        progressBar.setVisibility(VISIBLE);


        //Convert whatever was inputted by the user into the text fields on the register screen to strings.
        //Trim any potential whitespace from the inputted email address. The password is allowed to have whitespace.
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String emailAddress = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString();


        //If any one of the text fields has been left empty and unfilled out by the user, give the user this error message.
        //The user will then be forced to fix any issues before re-initiating the registration process by clicking on the Register button again.
        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty()) {
            Toast.makeText(Register.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }


        //Now, check to see which radio button the user selected on the registration screen by calling this method on the
        //radio group that contains them.
        //If the button choice is returned as the string "-1," then no button was selected by the user.
        //The user will then be forced to select a button before re-initiating the registration process by clicking on the Register button again.
        String accountType = checkRadioButtonChoice(accountTypeRadioGroup);
        if (accountType.equals("-1")){
            Toast.makeText(Register.this, "Please select an account type.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        }


        //Validate that the text the user has inputted on the registration screen (in the first name, last name, email address
        //and password fields) actually comply with the formatting standards required to register an account.
        //This is done by calling methods from the ValidateString helper class and storing whatever these methods output as strings.
        //For more information about the validation process and the formatting standards used to validate these text fields, refer to
        //the ValidateString class.
        String validatedFirstName = ValidateString.validateName(firstName);
        String validatedLastName = ValidateString.validateName(lastName);
        String validatedEmail = ValidateString.validateEmail(emailAddress);
        String validatedPassword = ValidateString.validatePassword(password);


        //If any of the methods from the ValidateString class returns the string "-1," the user's text inputs have not met the
        //formatting standards required to register an account through the app.
        //The user will then be forced to fix any issues before re-initiating the registration process by clicking on the Register button again.
        //If the methods from the ValidateString class do not return "-1," the user's text inputs have met the required formatting standards.
        //The strings initialized earlier (firstName, lastName, emailAddress and password) will be set to the outputs from the ValidateString methods.
        if (validatedFirstName.equals("-1")) {
            Toast.makeText(Register.this, "Invalid first name. Make sure not to use hyphens or accented characters. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            firstName = validatedFirstName;
        } if (validatedLastName.equals("-1")){
            Toast.makeText(Register.this, "Invalid last name. Make sure not to use hyphens or accented characters. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            lastName = validatedLastName;
        } if (validatedEmail.equals("-1")){
            Toast.makeText(Register.this, "Invalid email address. Please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            emailAddress = validatedEmail;
        } if (validatedPassword.equals("-1")){
            Toast.makeText(Register.this, "Passwords must be alphanumeric, and must contain 1+ numbers, 1+ uppercase letters, and 8+ characters total.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(INVISIBLE);
            return;
        } else {
            password = validatedPassword;
        }


        //Now create an instance of type User, with the string values of firstName, lastName and accountType serving as the constructor's parameters.
        final User userInfo = new User(firstName, lastName, accountType);
        //We will now try to register an account via Firebase Authentication using the .createUserWithEmailAndPassword() method.
        //Again, only the user-inputted email address and password will be stored in Firebase Authentication.
        //The user's unique uId in Firebase Authentication will be generated at this time as well.


        final String finalEmailAddress = emailAddress;
        final String finalPassword = password;

        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Since the account has been successfully registered through Firebase Authentication, it is now time to store the rest of the user's inputted
                    //information (first name, last name and account type) in Firebase Database.
                    //This method will store this information under the user's unique uId created by Firebase Authentication.
                    //The unique uIc itself can be found in the Database under the path "Users."
                    firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Since the user's information has been successfully stored in Firebase Database, the registration process is completed!
                                Toast.makeText(Register.this, "Account registered!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(INVISIBLE);
                                login(finalEmailAddress, finalPassword);

                            } else {
                                //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                                Toast.makeText(Register.this, "There was a problem saving your account information. Please try again.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(INVISIBLE);
                            }
                        }
                    });
                } else {
                    //If the registration in Firebase Authentication was not successful, give the user this message prompt.
                    //Firebase Authentication will check if an email address has already been registered to an account, so we can include this in the possible reasons why the
                    //registration process was unsuccessful.
                    Toast.makeText(Register.this, "There was a problem registering your account. You may already be registered. Please try again.", Toast.LENGTH_SHORT).show();
                    //If he or she wants to try again to register an account, the user must click on the register button again.
                    progressBar.setVisibility(INVISIBLE);
                }
            }
        });
    }


    /**
     * Checks to see which radio button is selected within a radio group.
     * Returns a String indicating which radio button is selected within the radio group.
     * Alternatively, returns the String "-1" if no radio button is selected within the radio group.
     */
    public String checkRadioButtonChoice(RadioGroup radioGroup){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if (radioButtonId == -1){
            //The radioButtonId would be equal to the int, -1, if no radio button is selected.
            return "-1";
        }
        RadioButton selection = (RadioButton)radioGroup.findViewById(radioButtonId);
        String selectionString = (String) selection.getText().toString();

        return selectionString;
    }



    private void login(String emailAddress, String password){
        firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Welcome!", Toast.LENGTH_SHORT).show();
                    getLoginUid();
                    DatabaseReference accountType = firebaseDatabase.getReference("Users").child(loginUid).child("accountType");

                    //Sends logged in user to the main screen.
                    //An Admin will go to the Admin Main Activity.
                    //Other Users will go to the other Main Acitivty.
                    accountType.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);

                            if (value.equals("Branch Account") ){
                                startActivity(new Intent(Register.this, BranchWelcomeActivity.class));
                            }

                            else if (value.equals("Admin Account")){
                                startActivity(new Intent(Register.this, AdminWelcomeActivity.class));
                            }

                            else {
                                startActivity(new Intent(Register.this, MainActivity.class));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(Register.this, "ERROR", Toast.LENGTH_SHORT).show();;
                        }
                    });

                } else {
                    Toast.makeText(Register.this, "The password and/or email was incorrect.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(INVISIBLE);
            }
        });
    }

    /**
     * Gets (but does not return) the unique user uId of the user who
     * is currently logged into the app via Firebase Authentication.
     */
    private void getLoginUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        loginUid = user.getUid();
    }
}