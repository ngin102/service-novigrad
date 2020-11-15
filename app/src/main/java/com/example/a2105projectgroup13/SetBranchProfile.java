package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

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

    Button editStreetAddressButton;
    Button editCityButton;
    Button editPostalCodeButton;
    Button editPhoneNumberButton;

    EditText enterAddressEditText;
    EditText streetNumberEditText;
    EditText cityEditText;
    EditText postalCodeEditText;
    EditText enterPhoneEditText;

    TextView showPhoneNumberTextView;
    TextView showAddressTextView;
    TextView showCityTextView;
    TextView showPostalCodeTextView;
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

        editStreetAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editStreetAddressOnClick(v);
            }
        });

        editPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoneNumberOnClick(v);
            }
        });

        editCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCityOnClick(v);
            }
        });

        editPostalCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPostalCodeOnClick(v);
            }
        });
    }


    /**
     Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        editStreetAddressButton = findViewById(R.id.editStreetAddressButton);
        editPhoneNumberButton = findViewById(R.id.editPhoneNumberButton);
        editPostalCodeButton = findViewById(R.id.editPostalCodeButton);;
        editCityButton = findViewById(R.id.editCityButton);;

        enterAddressEditText = findViewById(R.id.enterAddressEditText);
        streetNumberEditText = findViewById(R.id.streetNumberEditText);;
        cityEditText = findViewById(R.id.cityEditText);;
        postalCodeEditText = findViewById(R.id.postalCodeEditText);;
        enterPhoneEditText = findViewById(R.id.enterPhoneEditText);

        showPhoneNumberTextView = findViewById(R.id.showPhoneNumberTextView);
        showAddressTextView = findViewById(R.id.showStreetAddressTextView);
        showCityTextView = findViewById(R.id.showCityTextView);;
        showPostalCodeTextView = findViewById(R.id.showPostalCodeTextView);;
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

        DatabaseReference streetAddressReference = firebaseDatabase.getReference("User Info").child(uid).child("Street Address");
        DatabaseReference cityReference = firebaseDatabase.getReference("User Info").child(uid).child("City");
        DatabaseReference postalCodeReference = firebaseDatabase.getReference("User Info").child(uid).child("Postal Code");
        DatabaseReference phoneNumberReference = firebaseDatabase.getReference("User Info").child(uid).child("Phone Number");


        streetAddressReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showAddressTextView.setText("No street address has been set for this Branch.");

                } else {
                    showAddressTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SetBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        cityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showCityTextView.setText("No city has been set for this Branch.");
                } else {
                    showCityTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SetBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        postalCodeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showPostalCodeTextView.setText("No postal code has been set for this Branch.");
                } else {
                    showPostalCodeTextView.setText(value);
                }
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

                if (value == null){
                    showPhoneNumberTextView.setText("No phone number has been set for this Branch.");
                } else {
                    showPhoneNumberTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SetBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

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
        else {
            DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("Street Address");
            addressReference.setValue(streetNumber + " " + streetAddress);
        }
    }

    private void editCityOnClick(View view) {
        String city = cityEditText.getText().toString().trim();

        if (city.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please enter a city name.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("City");
            addressReference.setValue(city + ", " + "Novigrad");
        }
    }

    private void editPostalCodeOnClick(View view) {
        String postalCode = postalCodeEditText.getText().toString().trim();

        if (postalCode.isEmpty()){
            Toast.makeText(SetBranchProfile.this, "Please enter a postal code.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            DatabaseReference addressReference = firebaseDatabase.getReference("User Info").child(uid).child("Postal Code");
            addressReference.setValue(postalCode);
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
}