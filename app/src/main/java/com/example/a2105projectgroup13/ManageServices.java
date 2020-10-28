package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageServices extends AppCompatActivity {

    private Button addServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        initializeInstanceVariables();

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ManageServices.this, NewService.class));
            }
        });
    }


    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        addServiceButton = findViewById(R.id.addServiceButton);
    }

}