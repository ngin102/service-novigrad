package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class AdminWelcomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button deleteUsersButton;
    private Button manageServicesButton;
    private Button adminLogOutButton;
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
                startActivity(new Intent(AdminWelcomeActivity.this, AdminViewUserList.class));
            }
        });

        manageServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AdminWelcomeActivity.this, AdminViewServiceList.class));
            }
        });

        adminLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                logOut();
            }
        });


    }

    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        adminFirstNameText = (TextView) findViewById(R.id.adminFirstNameText);
        deleteUsersButton = (Button) findViewById(R.id.deleteUsersButton);
        manageServicesButton = (Button) findViewById(R.id.manageServicesButton);
        adminLogOutButton = (Button) findViewById(R.id.adminLogOutButton);
    }

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
        startActivity(new Intent(AdminWelcomeActivity.this, UserLogin.class));
        finish();
        Toast.makeText(AdminWelcomeActivity.this, "You are now logged out!", Toast.LENGTH_SHORT).show();
    }
}