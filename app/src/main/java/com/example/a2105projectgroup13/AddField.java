package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 This class allows the admin to add fields to Forms (i.e. required information that is entered
 by the user as a String).
 */

public class AddField extends AppCompatActivity {

    Button addFieldButton;
    Button cancelButton;
    EditText editTextFieldName;
    String newField;

    private Intent requirementScreen;
    private String serviceName;
    private String requirementName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference allFieldsReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        addFieldButton = (Button) findViewById(R.id.addFieldButtonOnScreen);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        editTextFieldName = (EditText) findViewById(R.id.editTextFieldName);

        requirementScreen = getIntent();
        serviceName = requirementScreen.getStringExtra("serviceName");
        requirementName = requirementScreen.getStringExtra("requirementName");

        firebaseDatabase = firebaseDatabase.getInstance();
        allFieldsReference = firebaseDatabase.getReference("Services").child(serviceName).child(requirementName).child("fields");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToRequirements1 = new Intent(AddField.this, ViewFields.class);
                moveToRequirements1.putExtra("selectedServiceName", serviceName);
                moveToRequirements1.putExtra("requirementName", requirementName);
                startActivity(moveToRequirements1);
            }
        });

        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                newField = editTextFieldName.getText().toString().trim();

                if (newField.equals("")){
                    Toast.makeText(AddField.this, "Please enter a field name.", Toast.LENGTH_LONG).show();
                    return;
                }

                String validatedNewField = ValidateString.validateServiceName(newField);
                if (validatedNewField.equals("-1")) {
                    Toast.makeText(AddField.this, "Invalid Field name. Make sure your Field name is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    newField = validatedNewField;
                }

                allFieldsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int childrenCount = (int) snapshot.getChildrenCount();

                        for (DataSnapshot field : snapshot.getChildren()) {
                            String fieldValue = field.getValue(String.class);
                            if (fieldValue.equals(newField)) {
                                Toast.makeText(AddField.this, "There is already a field with this name. Please choose a different field name.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        allFieldsReference.child(String.valueOf(childrenCount)).setValue(newField).addOnCompleteListener(AddField.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddField.this, "Field added to Form.", Toast.LENGTH_SHORT).show();
                                    finish();

                                    Intent moveToRequirements = new Intent(AddField.this, ViewFields.class);
                                    moveToRequirements.putExtra("selectedServiceName", serviceName);
                                    moveToRequirements.putExtra("requirementName", requirementName);
                                    startActivity(moveToRequirements);
                                } else {
                                    //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                                    Toast.makeText(AddField.this, "There was a problem adding this Field to your Form.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                     //Notifies the user that there was a database error.
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddField.this, "ERROR.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}