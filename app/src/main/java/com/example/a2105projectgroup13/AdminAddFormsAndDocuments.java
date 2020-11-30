package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 The class allows the admin to navigate between activities where they can add and manage
 Service Novigrad services: NewForm, NewDocument, and ManageServices.
 */

public class AdminAddFormsAndDocuments extends AppCompatActivity {
    private Intent previousScreen;
    private String serviceName;

    private TextView serviceNameOnScreen;

    private Button addFormButton;
    private Button addDocumentButton;
    private Button doneButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forms_and_documents);

        initializeInstanceVariables();

        serviceNameOnScreen.setText(serviceName);

        // Starts to the NewForm activity.
        // NewForm is used to create a new required form in the database for a service (i.e. a required field that the user will need to give information for).
        addFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToForms = new Intent(AdminAddFormsAndDocuments.this, AdminNewForm.class);
                moveToForms.putExtra("serviceName", serviceName);
                startActivity(moveToForms);
            }
        });

        // Starts to the NewDocument activity.
        // NewDocument is used to create a new required document in the database for a service (i.e. a document type like an image or a PDF that the user has to upload).
        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToDocuments = new Intent(AdminAddFormsAndDocuments.this, AdminNewDocument.class);
                moveToDocuments.putExtra("serviceName2", serviceName);
                startActivity(moveToDocuments);
            }
        });

        // Starts to the ManageServices activity.
        // ManageServices is used by the admin to access activites for adding (new services), or viewing/editing/deleting (existing) services.
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
                startActivity(new Intent(AdminAddFormsAndDocuments.this, AdminManageServices.class));
            }
        });
    }

    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        addFormButton = findViewById(R.id.firstAddFormButton);
        addDocumentButton = findViewById(R.id.firstAddDocumentButton);
        doneButton = findViewById(R.id.doneButton);
        previousScreen = getIntent();
        serviceName = previousScreen.getStringExtra("serviceName");
        serviceNameOnScreen = findViewById(R.id.serviceNameOnScreen);

    }
}