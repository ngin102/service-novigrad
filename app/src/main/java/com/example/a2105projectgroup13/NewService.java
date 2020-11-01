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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

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

    /**
     * Validates the Service name and price entered into the appropriate text fields.
     * If the data is entered incorrectly or the attempted service name already exists on Firebase,
     * an appropriate error message is displayed.
     */
    public void continueOnClick(View v) {
        //Convert whatever was inputted by the user into the text fields on the register screen to strings.
        //Trim any potential whitespace from the inputted email address. The password is allowed to have whitespace.
        String serviceName = editTextServiceName.getText().toString().trim();
        String price = editTextNumberPrice.getText().toString().trim();

        //validate the price
        String validatedPrice = ValidateString.validatePrice(price);
        if (validatedPrice == "-1") {
            Toast.makeText(NewService.this, "Please enter a valid form of price", Toast.LENGTH_SHORT).show();
            return;
        } else {
            price = validatedPrice;
        }

        //Checks if the inputted serviceName is empty. Prompts user to enter a service name.
        if (serviceName.equals("")){
            Toast.makeText(NewService.this, "Please enter a service name.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checks if the inputted price is empty. Prompts user to enter a valid price.
        if (price.equals("")){
            Toast.makeText(NewService.this, "Please enter a price.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Verifies that the inputted serviceName begins with a letter and is alphanumeric.
        String validatedServiceName = ValidateString.validateServiceName(serviceName);
        if (validatedServiceName.equals("-1")) {
            Toast.makeText(NewService.this, "Invalid Service name. Make sure your Service name begins with a letter and is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            serviceName = validatedServiceName;
        }

        //Initializes a Service object which its info will be added to Firebase.
        Admin admin = new Admin("Admin", "Admin", "Admin Account");
        final Service serviceToAdd = admin.createService(serviceName, price);

        final String newServiceName = serviceName;
        firebaseDatabase.getReference("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot service : snapshot.getChildren() ) {
                    String storedServiceName = service.getKey();

                    if (storedServiceName.toLowerCase().equals(newServiceName.toLowerCase())){
                        Toast.makeText(NewService.this, "There is already a Service with this name. Please choose a new Service name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                //Starts process of saving serviceToAdd info to Firebase to the directory "Services".
                firebaseDatabase.getReference("Services").child(newServiceName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) { //Stops process if a service of the same name is on Firebase.
                            Toast.makeText(NewService.this, "A service already exists under this name. Please use a different name for your service.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            firebaseDatabase.getReference("Services").child(serviceToAdd.getName()).child("price").setValue( serviceToAdd.getPrice() ).addOnCompleteListener(NewService.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) { //Adds info to Firebase if there were no complications.
                                        Toast.makeText(NewService.this, "Service created. Now add Forms and Documents.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent moveToNextScreen = new Intent(NewService.this, AddFormsAndDocuments.class);
                                        moveToNextScreen.putExtra("serviceName", serviceToAdd.getName());
                                        startActivity(moveToNextScreen);
                                    } else {
                                        //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                                        Toast.makeText(NewService.this, "There was a problem creating this Service.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(NewService.this, "There was a problem creating this Service.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewService.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

    }
}