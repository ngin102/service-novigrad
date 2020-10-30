package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 This class allows the Admin to naviate to activities for managing Services offered by Service Novigrad,
 namely: NewService and ServiceList.
 */

public class ManageServices extends AppCompatActivity {

    private Button addServiceButton;
    private Button viewServicesButton;

    /**
     Class allowing the Admin to navigate to the NewService or ServiceList activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        initializeInstanceVariables();

        // launch the NewService activity
        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ManageServices.this, NewService.class));
            }
        });

        // launch the ServiceList activity
        viewServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ManageServices.this, ServiceList.class));
            }
        });
    }

    /**
     Helper method for initializing the Button objects on the activity.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        addServiceButton = findViewById(R.id.addServiceButton);
        viewServicesButton = findViewById(R.id.viewServicesButton);
    }

}