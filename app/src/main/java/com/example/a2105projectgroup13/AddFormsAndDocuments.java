package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddFormsAndDocuments extends AppCompatActivity {
    Intent previousScreen;
    String serviceName;

    TextView serviceNameOnScreen;

    Button addFormButton;
    Button addDocumentButton;
    Button doneButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forms_and_documents);

        initializeInstanceVariables();

        serviceNameOnScreen.setText(serviceName);

        addFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AddFormsAndDocuments.this, NewForm.class));
            }
        });

        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AddFormsAndDocuments.this, NewDocument.class));
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AddFormsAndDocuments.this, ManageServices.class));
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