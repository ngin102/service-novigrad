package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 Class allowing the Admin to create a new document for a service.
 The class stores the document in the database under the applicable service.
 */


public class NewDocument extends AppCompatActivity {
    private EditText editTextDocumentName;
    private EditText editTextDescription;
    private Button chooseImageButton;
    private Button choosePDFButton;
    private Button addDocumentButton;
    private RadioGroup chooseFileTypeRadioGroup;
    private FirebaseDatabase firebaseDatabase;

    private String serviceName;
    private Intent previousScreen;

    private TextView serviceNameOnScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_document);

        initializeInstanceVariables();

        // display the name of the new service
        serviceNameOnScreen.setText(serviceName);

        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                submitDocumentOnClick(view);
            }
        });
    }

    /**
     Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        editTextDocumentName = findViewById(R.id.editTextDocumentName);
        editTextDescription = findViewById(R.id.editTextDescription);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        choosePDFButton = findViewById(R.id.choosePDFButton);
        addDocumentButton = findViewById(R.id.addDocumentButton);
        chooseFileTypeRadioGroup = findViewById(R.id.chooseFileTypeRadioGroup);
        firebaseDatabase = firebaseDatabase.getInstance();

        previousScreen = getIntent();
        serviceName = previousScreen.getStringExtra("serviceName2");

        serviceNameOnScreen = (TextView) findViewById(R.id.serviceNameOnScreen3);
    }

    /**
     * Checks to see which radio button is selected within a radio group.
     * Returns a String indicating which radio button is selected within the radio group.
     * Alternatively, returns the String "-1" if no radio button is selected within the radio group.
     */
    public String checkRadioButtonChoice(RadioGroup radioGroup){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if (radioButtonId == -1){
            //The radioButtonId would be equal to the int, -1, if no radio button is selected.
            return "-1";
        }
        RadioButton selection = (RadioButton)radioGroup.findViewById(radioButtonId);
        String selectionString = (String) selection.getText().toString();

        return selectionString;
    }

    /**
     Method that validates the data the admin has entered and then stores the information if it is valid.
     */
    public void submitDocumentOnClick(View v) {
        String documentName = editTextDocumentName.getText().toString().trim();

        String description =  editTextDescription.getText().toString().trim();

        // display an error if the document name is empty
        if (documentName.equals("")){
            Toast.makeText(NewDocument.this, "Please enter a name for the Document.", Toast.LENGTH_SHORT).show();
            return;
        }

        // display an error if the document description is empty
        if (description.equals("")){
            Toast.makeText(NewDocument.this, "Please enter a description for the Document.", Toast.LENGTH_SHORT).show();
            return;
        }

        // display an error if the document file type is unspecified
        String chooseFileType = checkRadioButtonChoice(chooseFileTypeRadioGroup);
        if (chooseFileType.equals("-1")){
            Toast.makeText(NewDocument.this, "Please select a file type.", Toast.LENGTH_SHORT).show();
            return;
        }

        // check that the fields are not empty before proceeding
        if ( ( ! chooseFileType.equals("-1") ) && (! documentName.equals("") ) && (! description.equals("") )) {

            // display an error if the document name is invalid
            String validatedDocumentName = ValidateString.validateServiceName(documentName);
            if (validatedDocumentName.equals("-1")) {
                Toast.makeText(NewDocument.this, "Invalid Document name. Make sure your Document name begins with a letter and is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            } else { // if validation is succesful, put the document name in the proper format per the validation
                documentName = validatedDocumentName;
            }

            // create an instance of Admin and use it to create a document object
            Admin admin = new Admin("Admin", "Admin", "Admin Account");
            final Document documentToAddToService = admin.createDocument("document", documentName, chooseFileType, description);

            // create a service with the specified name and price
            firebaseDatabase.getReference("Services").child(serviceName).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String servicePrice = dataSnapshot.getValue(String.class);
                    Service selectedService = new Service(serviceName, servicePrice);
                    documentToAddToService.setService(selectedService);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(NewDocument.this, "There was a problem creating this Form.", Toast.LENGTH_SHORT).show();
                }
            });



            final String newDocumentName = documentName;
            firebaseDatabase.getReference("Services").child(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot requirement : snapshot.getChildren()) {
                        String storedRequirementName = requirement.getKey();

                        if (storedRequirementName.toLowerCase().equals(newDocumentName.toLowerCase())) {
                            Toast.makeText(NewDocument.this, "There is already a requirement with this name. Please choose a new Requirement name.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }


                    firebaseDatabase.getReference("Services").child(serviceName).child(newDocumentName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(NewDocument.this, "A requirement already exists under this name. Please use a different name for your Document.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                firebaseDatabase.getReference("Services").child(serviceName).child(newDocumentName).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // display and error if a requirement already exists with the name specified by the Admin
                                        if (dataSnapshot.exists()) {
                                            Toast.makeText(NewDocument.this, "A requirement already exists under this name. Please use a different name for your Document.", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else { // add the new document to the database
                                            firebaseDatabase.getReference("Services").child(serviceName).child(documentToAddToService.getName()).setValue(documentToAddToService).addOnCompleteListener(NewDocument.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(NewDocument.this, "Document added to service", Toast.LENGTH_SHORT).show();
                                                        finish();

                                                        Intent moveToAdd = new Intent(NewDocument.this, ViewServiceRequirements.class);
                                                        moveToAdd.putExtra("serviceName", serviceName);
                                                        startActivity(moveToAdd);
                                                    } else {
                                                        //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                                                        Toast.makeText(NewDocument.this, "There was a problem adding this Document to your service.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    // display an error if there is a problem creating the document
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(NewDocument.this, "There was a problem creating this Document.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        // display an error if there was a problem saving the form to the database
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(NewDocument.this, "There was a problem creating this Form.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(NewDocument.this, "ERROR.", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}