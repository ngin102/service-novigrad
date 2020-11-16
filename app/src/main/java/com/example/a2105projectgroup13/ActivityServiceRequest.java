package com.example.a2105projectgroup13;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

public class ActivityServiceRequest extends AppCompatActivity {
    //instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button acceptButton, rejectButton, backButton;
    private ListView formFilledFieldsListView, attachmentsListView;
    private String branchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);

        initializeInstanceVariables();

        //The back button returns the user to the list of service requests received by the branch account
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityServiceRequest.this, BranchViewServiceRequests.class));
            }
        });

        //The accept action sets the status of the service request to "Accepted"
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest();
            }
        });

        //The reject action sets the status of the service request to "Rejected"
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectRequest();
            }
        });
    }

    /**
     * Initializes all of the instance variables.
     */
    private void initializeInstanceVariables() {
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        //getting the branch id
        FirebaseUser branchAccount = firebaseAuth.getInstance().getCurrentUser();
        branchId = branchAccount.getUid();

        //initialize buttons
        acceptButton = (Button)findViewById(R.id.acceptButton);
        rejectButton = (Button)findViewById(R.id.rejectButton);
        backButton = (Button)findViewById(R.id.backButton);

        //initialize ListViews
        formFilledFieldsListView = (ListView)findViewById(R.id.formFilledFieldsListView);
        attachmentsListView = (ListView)findViewById(R.id.attachmentsListView);
    }

    /**
     * Sets the service request status to "Accepted"
     */
    private void acceptRequest() {

    }

    /**
     * Sets the service request status to "Rejected"
     */
    private void rejectRequest() {

    }
}
