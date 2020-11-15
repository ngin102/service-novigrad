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


public class SetBranchProfile extends AppCompatActivity {

    Button editAddressButton;
    Button editPhoneNumberButton;

    EditText enterAddressEditText;
    EditText enterPhoneEditText;

    TextView showPhoneNumberTextView;
    TextView showAddressTextView;
    TextView uidBranchTextView;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_branch_profile);

        initializeInstanceVariables();
        getUid();

        getProfileInfo();

        editAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddressOnClick(v);
            }
        });

        editPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoneNumberOnClick(v);
            }
        });

    }


    /**
     Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        editAddressButton = findViewById(R.id.editAddressButton);
        editPhoneNumberButton = findViewById(R.id.editPhoneNumberButton);

        enterAddressEditText = findViewById(R.id.enterAddressEditText);
        enterPhoneEditText = findViewById(R.id.enterPhoneEditText);

        showPhoneNumberTextView = findViewById(R.id.showPhoneNumberTextView);
        showAddressTextView = findViewById(R.id.showAddressTextView);
        uidBranchTextView = findViewById(R.id.uidBranchTextView);

        firebaseDatabase = firebaseDatabase.getInstance();
    }

    private void getUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }

    private void getProfileInfo(){
        uidBranchTextView.setText(uid);

        DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("Address");
        DatabaseReference phoneNumberReference = firebaseDatabase.getReference("User Info").child(uid).child("Phone Number");


        addressReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                showAddressTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SetBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        //Use dataSnapshot to set a TextView to display the user's account type.
        phoneNumberReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                showPhoneNumberTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SetBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    private void editAddressOnClick(View view) {
        String address = enterAddressEditText.getText().toString().trim();

        if (address.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please indicate the new address.", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("Address");
            addressReference.setValue(address);
        }
    }


    private void editPhoneNumberOnClick(View view) {
        String phoneNumber = enterPhoneEditText.getText().toString().trim();

        if (phoneNumber.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please indicate the new phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            DatabaseReference phoneNumberReference = firebaseDatabase.getReference("User Info").child(uid).child("Phone Number");
            phoneNumberReference.setValue(phoneNumber);
        }
    }
}