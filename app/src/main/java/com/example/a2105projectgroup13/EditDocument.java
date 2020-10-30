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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDocument extends AppCompatActivity {
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToRequirements1 = new Intent(EditDocument.this, ViewServiceRequirements.class);
                moveToRequirements1.putExtra("serviceName", serviceName);
                moveToRequirements1.putExtra("requirementName", requirementName);
                startActivity(moveToRequirements1);
            }
        });

        editDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newDescription = editTextDescription.getText().toString().trim();

                if (newDescription.equals("")) {
                    Toast.makeText(EditDocument.this, "Please enter a description", Toast.LENGTH_LONG).show();
                    return;
                }

                documentInfoReference.child("description").setValue(newDescription).addOnCompleteListener(EditDocument.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditDocument.this, "The description has been updated.", Toast.LENGTH_SHORT).show();
                            finish();

                            Intent moveToRequirements = new Intent(EditDocument.this, ViewServiceRequirements.class);
                            moveToRequirements.putExtra("serviceName", serviceName);
                            startActivity(moveToRequirements);
                        } else {
                            Toast.makeText(EditDocument.this, "There was a problem updating the description.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        chooseNewTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chooseFileType = checkRadioButtonChoice(chooseFileTypeRadioGroup);
                if (chooseFileType.equals("-1")) {
                    Toast.makeText(EditDocument.this, "Please select a file type.", Toast.LENGTH_SHORT).show();
                    return;
                }

                documentInfoReference.child("fileType").setValue(chooseFileType).addOnCompleteListener(EditDocument.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditDocument.this, "The file type has been updated.", Toast.LENGTH_SHORT).show();
                            finish();

                            Intent moveToRequirements = new Intent(EditDocument.this, ViewServiceRequirements.class);
                            moveToRequirements.putExtra("serviceName", serviceName);
                            startActivity(moveToRequirements);
                        } else {
                            Toast.makeText(EditDocument.this, "There was a problem updating the description.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

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