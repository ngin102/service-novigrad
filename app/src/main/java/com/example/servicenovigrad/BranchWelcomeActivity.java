package com.example.servicenovigrad;

import androidx.annotation.NonNull;
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

import static android.view.View.VISIBLE;

/**
 * This class is the Branch welcome activity screen.
 */
public class BranchWelcomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    private Button viewBranchProfileButton;
    private Button offerServicesCreatedByAdminButton;
    private Button viewOfferedServicesButton;
    private Button viewWorkingHoursButton;
    private Button viewServiceRequestsButton;
    private Button branchLogOutButton;

    //Text that appears on screen:
    private TextView firstNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_welcome);

        initializeInstanceVariables();

        //The unique user uId of the user who is currently logged in to the app.
        getUid();



        offerServicesCreatedByAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(BranchWelcomeActivity.this, BranchViewAdminServiceList.class));
            }
        });

        firebaseDatabase.getReference().child("User Info").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Street Address") && snapshot.hasChild("Postal Code") && snapshot.hasChild("City") && snapshot.hasChild("Phone Number")) {
                   offerServicesCreatedByAdminButton.setVisibility(VISIBLE);
                   viewOfferedServicesButton.setVisibility(VISIBLE);
                   viewWorkingHoursButton.setVisibility(VISIBLE);
                   viewServiceRequestsButton.setVisibility(VISIBLE);
                } else {
                   return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchWelcomeActivity.this, "ERROR.", Toast.LENGTH_SHORT).show();
            }
        });

        viewOfferedServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(BranchWelcomeActivity.this, BranchViewOfferedServiceList.class));
            }
        });

        viewServiceRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(BranchWelcomeActivity.this, BranchViewServiceRequests.class));
            }
        });

        viewBranchProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToProfile = new Intent(BranchWelcomeActivity.this, BranchSetProfile.class);
                moveToProfile.putExtra("uid", uid);
                startActivity(moveToProfile);
            }
        });


        viewWorkingHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToWorkingHours = new Intent(BranchWelcomeActivity.this, BranchAddWorkingHours.class);
                moveToWorkingHours.putExtra("uid", uid);
                startActivity(moveToWorkingHours);
            }
        });

        branchLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                logOut();
            }
        });


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
                Toast.makeText(BranchWelcomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    /**
     * Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        offerServicesCreatedByAdminButton = (Button) findViewById(R.id.viewAdminServicesFromBranchButton);
        viewOfferedServicesButton = (Button) findViewById(R.id.viewOfferedServicesButton);
        viewBranchProfileButton = (Button) findViewById(R.id.viewBranchProfileButton);
        viewWorkingHoursButton = (Button) findViewById(R.id.viewWorkingHoursButton);
        viewServiceRequestsButton = (Button)findViewById(R.id.viewServiceRequestsButton);
        branchLogOutButton = (Button)findViewById(R.id.branchLogOutButton);

        firstNameText = (TextView) findViewById(R.id.nameBranchText);
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
     * Method to logout of the user's account.
     */
    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(BranchWelcomeActivity.this, UserLogin.class));
        finish();
        Toast.makeText(BranchWelcomeActivity.this, "You are now logged out!", Toast.LENGTH_SHORT).show();
    }
}