package com.example.a2105projectgroup13;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityServiceRequest extends AppCompatActivity {
    //instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button acceptButton, rejectButton, backButton;
    private ListView formFilledFieldsListView, attachmentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);

        initializeInstanceVariables();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: move user to previous activity
            }
        });
    }

    private void initializeInstanceVariables() {
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        //initialize buttons
        acceptButton = (Button)findViewById(R.id.acceptButton);
        rejectButton = (Button)findViewById(R.id.rejectButton);
        backButton = (Button)findViewById(R.id.backButton);

        //initialize ListViews
        formFilledFieldsListView = (ListView)findViewById(R.id.formFilledFieldsListView);
        attachmentsListView = (ListView)findViewById(R.id.attachmentsListView);
    }
}
