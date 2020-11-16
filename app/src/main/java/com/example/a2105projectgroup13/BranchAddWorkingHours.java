package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BranchAddWorkingHours extends AppCompatActivity {

    TextView mondayWorkingHoursTextView;
    TextView tuesdayWorkingHoursTextView;
    TextView wednesdayWorkingHoursTextView;
    TextView thursdayWorkingHoursTextView;
    TextView fridayWorkingHoursTextView;
    TextView saturdayWorkingHoursTextView;
    TextView sundayWorkingHoursTextView;

    TextView uidBranchTextView;

    EditText startTimeEditText1;
    EditText endTimeEditText1;
    EditText startTimeEditText2;
    EditText endTimeEditText2;
    EditText startTimeEditText3;
    EditText endTimeEditText3;
    EditText startTimeEditText4;
    EditText endTimeEditText4;
    EditText startTimeEditText5;
    EditText endTimeEditText5;
    EditText startTimeEditText6;
    EditText endTimeEditText6;
    EditText startTimeEditText7;
    EditText endTimeEditText7;

    Button editHoursButton1;
    Button editHoursButton2;
    Button editHoursButton3;
    Button editHoursButton4;
    Button editHoursButton5;
    Button editHoursButton6;
    Button editHoursButton7;
    Button goToBranchWelcomeScreenButton;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_add_working_hours);

        initializeInstanceVariables();
        getUid();

        getWorkingHours();
    }


    /**
     Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.

        //Display working hours
        mondayWorkingHoursTextView = findViewById(R.id.mondayWorkingHoursTextView);
        tuesdayWorkingHoursTextView = findViewById(R.id.tuesdayWorkingHoursTextView);
        wednesdayWorkingHoursTextView = findViewById(R.id.wednesdayWorkingHoursTextView);
        thursdayWorkingHoursTextView = findViewById(R.id.thursdayWorkingHoursTextView);
        fridayWorkingHoursTextView = findViewById(R.id.fridayWorkingHoursTextView);
        saturdayWorkingHoursTextView = findViewById(R.id.saturdayWorkingHoursTextView);
        sundayWorkingHoursTextView = findViewById(R.id.sundayWorkingHoursTextView);

        //Display uid
        uidBranchTextView = findViewById(R.id.uidBranchTextView2);

        //Edit open and close time
        startTimeEditText1 = findViewById(R.id.startTimeEditText1);
        endTimeEditText1 = findViewById(R.id.endTimeEditText1);
        editHoursButton1 = findViewById(R.id.editHoursButton1);

        startTimeEditText2 = findViewById(R.id.startTimeEditText2);
        endTimeEditText2 = findViewById(R.id.endTimeEditText2);
        editHoursButton2 = findViewById(R.id.editHoursButton2);

        startTimeEditText3 = findViewById(R.id.startTimeEditText3);
        endTimeEditText3 = findViewById(R.id.endTimeEditText3);
        editHoursButton3 = findViewById(R.id.editHoursButton3);

        startTimeEditText4 = findViewById(R.id.startTimeEditText4);
        endTimeEditText4 = findViewById(R.id.endTimeEditText4);
        editHoursButton4 = findViewById(R.id.editHoursButton4);

        startTimeEditText5 = findViewById(R.id.startTimeEditText5);
        endTimeEditText5 = findViewById(R.id.endTimeEditText5);
        editHoursButton5 = findViewById(R.id.editHoursButton5);

        startTimeEditText6 = findViewById(R.id.startTimeEditText6);
        endTimeEditText6 = findViewById(R.id.endTimeEditText6);
        editHoursButton6 = findViewById(R.id.editHoursButton6);

        startTimeEditText7 = findViewById(R.id.startTimeEditText7);
        endTimeEditText7 = findViewById(R.id.endTimeEditText7);
        editHoursButton7 = findViewById(R.id.editHoursButton7);


        //Return to welcome screen
        goToBranchWelcomeScreenButton = findViewById(R.id.goToBranchWelcomeScreenButton2);


        firebaseDatabase = firebaseDatabase.getInstance();
    }

    private void getUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }

    private void getWorkingHours(){
        uidBranchTextView.setText(uid);

        DatabaseReference mondayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Monday");
        DatabaseReference tuesdayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Tuesday");
        DatabaseReference wednesdayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Wednesday");
        DatabaseReference thursdayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Thursday");
        DatabaseReference fridayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Friday");
        DatabaseReference saturdayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Saturday");
        DatabaseReference sundayHoursReference = firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Sunday");

        mondayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    mondayWorkingHoursTextView.setText("Monday: CLOSED");

                } else {
                    mondayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        tuesdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    tuesdayWorkingHoursTextView.setText("Tuesday: CLOSED");
                } else {
                    tuesdayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        wednesdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    wednesdayWorkingHoursTextView.setText("Wednesday: CLOSED");
                } else {
                    wednesdayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        thursdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    thursdayWorkingHoursTextView.setText("Thursday: CLOSED");
                } else {
                    thursdayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        fridayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    fridayWorkingHoursTextView.setText("Friday: CLOSED");
                } else {
                    fridayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        saturdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    saturdayWorkingHoursTextView.setText("Saturday: CLOSED");
                } else {
                    saturdayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        sundayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    sundayWorkingHoursTextView.setText("Sunday: CLOSED");
                } else {
                    sundayWorkingHoursTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BranchAddWorkingHours.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    /**
    private void editStreetAddressOnClick(View view) {
        String streetNumber = streetNumberEditText.getText().toString().trim();
        String streetAddress = enterAddressEditText.getText().toString().trim();

        if (streetNumber.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please enter the new street number.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (streetAddress.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please enter the new street address.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (streetAddress.equals("-")){
            Toast.makeText(SetBranchProfile.this, "' - ' is not a valid street address.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String validateStreetAddress = ValidateString.validateAddressOrCity(streetAddress);
            if (validateStreetAddress.equals("-1")) {
                Toast.makeText(SetBranchProfile.this, "Street addresses can only be alphabetic; they can also include hyphens. Please enter a valid street address.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("Street Address");
                addressReference.setValue(streetNumber + " " + validateStreetAddress);
            }
        }
    }

    private void editCityOnClick(View view) {
        String city = cityEditText.getText().toString().trim();

        if (city.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please enter a city name.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (city.equals("-")){
            Toast.makeText(SetBranchProfile.this, "' - ' is not a city name.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String validateCity = ValidateString.validateAddressOrCity(city);
            if (validateCity.equals("-1")) {
                Toast.makeText(SetBranchProfile.this, "City names can only be alphabetic; they can also include hyphens. Please enter a valid city name.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("City");
                addressReference.setValue(validateCity + ", " + "Novigrad");
            }
        }
    }

    private void editPostalCodeOnClick(View view) {
        String postalCode = postalCodeEditText.getText().toString().trim();

        if (postalCode.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please enter a postal code.", Toast.LENGTH_SHORT).show();
            return;
        } else if (postalCode.length() != 6){
            Toast.makeText(SetBranchProfile.this, "Postal codes are 6 characters long. Please enter a valid postal code.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String validatePostalCode = ValidateString.validatePostalCode(postalCode);
            if (validatePostalCode.equals("-1")){
                Toast.makeText(SetBranchProfile.this, "Postal codes are 6 characters long. Postal codes follow the format 'A0A0A0', where A is any letter and 0 is any number. Please enter a valid postal code.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("Postal Code");
                addressReference.setValue(validatePostalCode);
            }
        }
    }


    private void editPhoneNumberOnClick(View view) {
        String phoneNumber = enterPhoneEditText.getText().toString().trim();

        if (phoneNumber.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please indicate the new phone number.", Toast.LENGTH_SHORT).show();
            return;
        } else if (phoneNumber.length() != 10){
            Toast.makeText(SetBranchProfile.this, "Phone numbers are 10 characters long. Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            DatabaseReference phoneNumberReference = firebaseDatabase.getReference("User Info").child(uid).child("Phone Number");
            phoneNumberReference.setValue("(" + phoneNumber.substring(0, 3) + ")" + " " + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6));
        }
    }
    **/


    private void goToWelcomeScreenOnClick(View view){
        startActivity(new Intent(BranchAddWorkingHours.this, BranchWelcomeActivity.class));
    }
}