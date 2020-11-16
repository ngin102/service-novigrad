
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

        editHoursButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText1, endTimeEditText1, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Monday"));
            }
        });

        editHoursButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText2, endTimeEditText2, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Tuesday"));
            }
        });

        editHoursButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText3, endTimeEditText3, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Wednesday"));
            }
        });

        editHoursButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText4, endTimeEditText4, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Thursday"));
            }
        });

        editHoursButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText5, endTimeEditText5, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Friday"));
            }
        });

        editHoursButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText6, endTimeEditText6, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Saturday"));
            }
        });

        editHoursButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHoursOnClick(v, startTimeEditText7, endTimeEditText7, firebaseDatabase.getReference("User Info").child(uid).child("Working Hours").child("Sunday"));
            }
        });
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


    private void editHoursOnClick(View view, EditText startTimeEditText, EditText endTimeEditText, DatabaseReference referenceToWorkingHours) {
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        if (startTime.isEmpty()){
            Toast.makeText(BranchAddWorkingHours.this, "Please enter a open time.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (endTime.isEmpty()){
            Toast.makeText(BranchAddWorkingHours.this, "Please enter an close time.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String validateStartTime = ValidateString.validateTime(startTime);
            if (validateStartTime.equals("-1")) {
                Toast.makeText(BranchAddWorkingHours.this, "Please enter a valid open time.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String validateEndTime = ValidateString.validateTime(endTime);
                if (validateEndTime.equals("-1")) {
                    Toast.makeText(BranchAddWorkingHours.this, "Please enter a valid close time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if (validateStartTime.equals(validateEndTime)){
                        Toast.makeText(BranchAddWorkingHours.this, "Your open time and close time occur at the same time, indicating that your Branch is closed for the day.", Toast.LENGTH_SHORT).show();
                        referenceToWorkingHours.setValue("CLOSED");
                        return;
                    }
                    else {
                        referenceToWorkingHours.setValue(validateStartTime + " - " + validateEndTime);
                    }
                }
            }
        }
    }


    private void goToWelcomeScreenOnClick(View view){
        startActivity(new Intent(BranchAddWorkingHours.this, BranchWelcomeActivity.class));
    }
}