package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AdminWelcomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button deleteUsersButton;
    private Button manageServicesButton;
    private String uid;

    private TextView adminFirstNameText;

    /*

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        initializeInstanceVariables();

        deleteUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AdminWelcomeActivity.this, BranchViewUserList.class));
            }
        });

        manageServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AdminWelcomeActivity.this, AdminManageServices.class));
            }
        });


    }

    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        adminFirstNameText = (TextView) findViewById(R.id.adminFirstNameText);
        deleteUsersButton = (Button) findViewById(R.id.deleteUsersButton);
        manageServicesButton = (Button) findViewById(R.id.manageServicesButton);
    }

    private void getUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }
}