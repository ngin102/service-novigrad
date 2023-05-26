package com.example.servicenovigrad;

    /*
    This class allows the admin to edit an existing document.
     */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminEditDocument extends AppCompatActivity {
    Button editDescriptionButton;
    EditText editTextDescription;
    Button cancelButton;
    Button chooseNewTypeButton;
    private RadioGroup chooseFileTypeRadioGroup;

    private Intent requirementScreen;
    private String serviceName;
    private String requirementName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference documentInfoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_document);

        editDescriptionButton = (Button) findViewById(R.id.editDescriptionButton);
        cancelButton = (Button) findViewById(R.id.cancelButtonDocument);
        editTextDescription = (EditText) findViewById(R.id.editTextDescriptionDialog);

        chooseFileTypeRadioGroup = findViewById(R.id.chooseFileTypeRadioGroup2);
        chooseNewTypeButton = findViewById(R.id.chooseNewTypeButton);

        requirementScreen = getIntent();
        serviceName = requirementScreen.getStringExtra("selectedServiceName");
        requirementName = requirementScreen.getStringExtra("requirementName");

        firebaseDatabase = firebaseDatabase.getInstance();
        documentInfoReference = firebaseDatabase.getReference("Services").child(serviceName).child(requirementName);

        // when the cancel button is clicked, navigate to ViewServiceRequirements
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToRequirements1 = new Intent(AdminEditDocument.this, AdminViewServiceRequirements.class);
                moveToRequirements1.putExtra("serviceName", serviceName);
                moveToRequirements1.putExtra("requirementName", requirementName);
                startActivity(moveToRequirements1);
            }
        });

        // when the edit description button is clicked, overwrite the existing description of the document with the new description
        editDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newDescription = editTextDescription.getText().toString().trim();

                // require the admin to provide a description for a document
                if (newDescription.equals("")) {
                    Toast.makeText(AdminEditDocument.this, "Please enter a description", Toast.LENGTH_LONG).show();
                    return;
                }

                // if the description is not empty, then overwrite the existing description
                // after the task is complete, navigate to moveToRequirements
                documentInfoReference.child("description").setValue(newDescription).addOnCompleteListener(AdminEditDocument.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminEditDocument.this, "The description has been updated.", Toast.LENGTH_SHORT).show();
                            finish();

                            Intent moveToRequirements = new Intent(AdminEditDocument.this, AdminViewServiceRequirements.class);
                            moveToRequirements.putExtra("serviceName", serviceName);
                            startActivity(moveToRequirements);
                        } else {
                            // display an error if the overwrite was unsuccessful
                            Toast.makeText(AdminEditDocument.this, "There was a problem updating the description.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //
        chooseNewTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chooseFileType = checkRadioButtonChoice(chooseFileTypeRadioGroup);
                // display an error if the admin hasn't selected a file type
                if (chooseFileType.equals("-1")) {
                    Toast.makeText(AdminEditDocument.this, "Please select a file type.", Toast.LENGTH_SHORT).show();
                    return;
                }

                documentInfoReference.child("fileType").setValue(chooseFileType).addOnCompleteListener(AdminEditDocument.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminEditDocument.this, "The file type has been updated.", Toast.LENGTH_SHORT).show();
                            finish();

                            Intent moveToRequirements = new Intent(AdminEditDocument.this, AdminViewServiceRequirements.class);
                            moveToRequirements.putExtra("serviceName", serviceName);
                            startActivity(moveToRequirements);
                        } else {
                            // display an error if the type overwrite is unsuccesful
                            // after the task is complete, navigate to moveToRequirements
                            Toast.makeText(AdminEditDocument.this, "There was a problem updating the description.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    /*
    Returns the text of the selected radio button choice, or "-1" if no radio button has been selected.
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
}