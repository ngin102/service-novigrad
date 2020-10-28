package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.VISIBLE;

public class NewService extends AppCompatActivity {
    private EditText editTextServiceName;
    private EditText editTextNumberPrice;
    private Button continueButton;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service);

        initializeInstanceVariables();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueOnClick(v);
            }
        });

    }

    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextNumberPrice = findViewById(R.id.editTextNumberPrice);
        continueButton = findViewById(R.id.continueButton);
        firebaseDatabase = firebaseDatabase.getInstance();
    }

    public void continueOnClick(View v) {
        //Convert whatever was inputted by the user into the text fields on the register screen to strings.
        //Trim any potential whitespace from the inputted email address. The password is allowed to have whitespace.
        String serviceName = editTextServiceName.getText().toString().trim();
        String price = editTextNumberPrice.getText().toString().trim();

        firebaseDatabase.getReference("Services").child(serviceName).child(price).setValue(price).addOnCompleteListener(NewService.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewService.this, "Service created. Now add Forms and Documents.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(NewService.this, AddFormsAndDocuments.class));
                } else {
                    //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                    Toast.makeText(NewService.this, "There was a problem creating this Service.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}