package com.example.a2105projectgroup13;

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

public class BranchWelcomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    private Button offerServicesCreatedByAdminButton;
    private Button viewOfferedServicesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_welcome);

        initializeInstanceVariables();

        offerServicesCreatedByAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(BranchWelcomeActivity.this, BranchViewAdminServiceList.class));
            }
        });

        viewOfferedServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(BranchWelcomeActivity.this, BranchViewOfferedServiceList.class));
            }
        });


    }

    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        offerServicesCreatedByAdminButton = (Button) findViewById(R.id.offerServicesFromAdminListButton);
        viewOfferedServicesButton = (Button) findViewById(R.id.viewOfferedServicesButton);
    }

    private void getUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }
}