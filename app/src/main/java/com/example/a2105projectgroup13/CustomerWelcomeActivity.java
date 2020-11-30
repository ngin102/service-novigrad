package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class is the Customer Welcome Screen.
 */
public class CustomerWelcomeActivity extends AppCompatActivity {
    //Variables required to retrieve information from Firebase Authentication and Firebase Database:
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    //Text that appears on screen:
    private TextView firstNameText;

    private Button viewBranchListButton, searchBranchListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welcome);

        initializeInstanceVariables();

        //The unique user uId of the user who is currently logged in to the app.
        getUid();

        //Get the references to the user's first name and account type in Firebase Database based on the user's unique uId.
        DatabaseReference firstName = firebaseDatabase.getReference("Users").child(uid).child("firstName");

        //Use dataSnapshot to set a TextView to display the user's first name.
        firstName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                firstNameText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerWelcomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        viewBranchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToBranchList = new Intent(CustomerWelcomeActivity.this, CustomerViewBranchList.class);
                startActivity(moveToBranchList);
            }
        });

        searchBranchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToBranchSearch = new Intent(CustomerWelcomeActivity.this, CustomerBranchSearch.class);
                startActivity(moveToBranchSearch);
            }
        });
    }

    /**
     * Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        firstNameText = (TextView) findViewById(R.id.firstNameText);

        viewBranchListButton = (Button) findViewById(R.id.viewBranchListButton);
        searchBranchListButton = (Button) findViewById(R.id.searchBranchList);

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
/*
    private void onSearchButtonClicked(View view) {
        Intent moveToBranchSearch = new Intent(CustomerWelcomeActivity.this, BranchSearch.class);
        startActivity(moveToBranchSearch);
    }

 */
}