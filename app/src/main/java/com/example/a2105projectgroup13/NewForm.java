package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
/**
 This class allows the Admin to add a new form to the database.
 */

public class NewForm extends AppCompatActivity {
    private EditText editTextFormName;
    private LinearLayout fieldList;
    private Button addFieldButton;
    private Button addFormButton;
    private FirebaseDatabase firebaseDatabase;

    private String serviceName;
    private Intent previousScreen;

    private TextView serviceNameOnscreen;

    private ArrayList<String> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_form);

        initializeInstanceVariables();

        serviceNameOnscreen.setText(serviceName);


        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                addFieldToList();
            }
        });

        addFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                submitFormOnClick(view);
            }
        });
    }

    /**
     Helper method for initializing local variables.
     */
    private void initializeInstanceVariables() {
        editTextFormName = findViewById(R.id.editTextFormName);
        fieldList = findViewById(R.id.fieldList);
        addFieldButton = findViewById(R.id.addFieldButton);
        addFormButton = findViewById(R.id.addFormButton);
        fields = new ArrayList<>();
        firebaseDatabase = firebaseDatabase.getInstance();

        previousScreen = getIntent();
        serviceName = previousScreen.getStringExtra("serviceName");

        serviceNameOnscreen = (TextView) findViewById(R.id.serviceNameOnScreen2);
    }

    /**
     Method which adds a new form field for the Admin to fill out.
     */
    private void addFieldToList(){
        LayoutInflater inflater = getLayoutInflater();
        final View field = inflater.inflate(R.layout.add_field_in_newform, null);

        Button removeFieldButton = (Button) field.findViewById(R.id.removeFieldButton);

        removeFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                fieldList.removeView(field);
            }
        });

        fieldList.addView(field);
    }

    /**
     Method which checks that all fields are filled by the admin.
     Returns true if the fields are filled appropriately, or false if they are not.
     */
    private boolean validateForEmptyFields(){
        for (int i = 0; i < fieldList.getChildCount(); i++){
            View selectedField = fieldList.getChildAt(i);
            EditText selectedFieldName = (EditText) selectedField.findViewById(R.id.editTextFieldNameInNewForm);
            String fieldName = selectedFieldName.getText().toString().trim();

            // display an error if a field is empty
            if (fieldName.isEmpty()) {
                Toast.makeText(NewForm.this, "Please name all fields or remove unnamed fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    /**
     Method that verifies there are no duplicate fields.
     Returns true if there are no duplicate fields, or false if there are duplicate fields.
     */
    private boolean validateForDuplicateFields(){
        for (int i = 0; i < fieldList.getChildCount(); i++){
            for (int j = i + 1; j < fieldList.getChildCount(); j++){
                View fieldOne = fieldList.getChildAt(i);
                EditText fieldNameOne = (EditText) fieldOne.findViewById(R.id.editTextFieldNameInNewForm);
                String stringFieldNameOne = fieldNameOne.getText().toString().toLowerCase().trim();

                View fieldTwo = fieldList.getChildAt(j);
                EditText fieldNameTwo = (EditText) fieldTwo.findViewById(R.id.editTextFieldNameInNewForm);
                String stringFieldNameTwo = fieldNameTwo.getText().toString().toLowerCase().trim();

                // display an error if there is a duplicate field
                if (stringFieldNameOne.equals(stringFieldNameTwo)) {
                    Toast.makeText(NewForm.this, "Please remove duplicate fields.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    /**
     Method for uploading the new form to the database under the appropriate service.
     */
    public void submitFormOnClick(View v) {
        String formName = editTextFormName.getText().toString().trim();

        // display and error if the form's name is empty
        if (formName.equals("")){
            Toast.makeText(NewForm.this, "Please enter a name for the Form.", Toast.LENGTH_SHORT).show();
            return;
        }
        // display and error if the form's name is invalid
        String validatedFormName = ValidateString.validateServiceName(formName);
        if (validatedFormName.equals("-1")) {
            Toast.makeText(NewForm.this, "Invalid Form name. Make sure your Form name begins with a letter and is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            formName = validatedFormName;
        }

        // display and error if the form has no fields
        if (fieldList.getChildCount() == 0){
            Toast.makeText(NewForm.this, "Please specify the fields that will be in the Form.", Toast.LENGTH_SHORT).show();
            return;
        }


        if ( validateForEmptyFields() == true &&  validateForDuplicateFields() == true && (! formName.equals("") ) && (fieldList.getChildCount() != 0)) {
            fields.clear();
            for (int i = 0; i < fieldList.getChildCount(); i++) {
                View selectedField = fieldList.getChildAt(i);
                EditText selectedFieldName = (EditText) selectedField.findViewById(R.id.editTextFieldNameInNewForm);
                String fieldName = selectedFieldName.getText().toString().trim();

                String validatedFieldName = ValidateString.validateServiceName(fieldName);
                if (validatedFieldName.equals("-1")) {
                    Toast.makeText(NewForm.this, "Invalid Field name. Make sure your Field name begins with a letter and is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    fieldName = validatedFieldName;
                }

                fields.add(fieldName);
            }

            String[] formFields = new String[fields.size()];
            for (int i = 0; i < fields.size(); i++){
                formFields[i] = fields.get(i);
            }


            Admin admin = new Admin("Admin", "Admin", "Admin Account");
            final Form formToAddToService = admin.createForm("form", formName);

            firebaseDatabase.getReference("Services").child(serviceName).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String servicePrice = dataSnapshot.getValue(String.class);
                    Service selectedService = new Service(serviceName, servicePrice);
                    formToAddToService.setService(selectedService);
                }

                // display an error if there is a problem finding the service in the database
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(NewForm.this, "There was a problem creating this Form.", Toast.LENGTH_SHORT).show();
                }
            });


            final String newFormName = formName;
            firebaseDatabase.getReference("Services").child(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot requirement : snapshot.getChildren()) {
                        String storedRequirementName = requirement.getKey();

                        if (storedRequirementName.toLowerCase().equals(newFormName.toLowerCase())) {
                            Toast.makeText(NewForm.this, "There is already a requirement with this name. Please choose a new Form name.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }


                    firebaseDatabase.getReference("Services").child(serviceName).child(newFormName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(NewForm.this, "A requirement already exists under this name. Please use a different name for your Form.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                firebaseDatabase.getReference("Services").child(serviceName).child(formToAddToService.getName()).setValue(formToAddToService).addOnCompleteListener(NewForm.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference fieldsInFirebase = firebaseDatabase.getReference("Services").child(serviceName).child(formToAddToService.getName()).child("fields");
                                            for (int i = 0; i < fields.size(); i++) {
                                                String fieldToAdd = fields.get(i);
                                                fieldsInFirebase.child(Integer.toString(i)).setValue(fieldToAdd);
                                            }

                                            Toast.makeText(NewForm.this, "Form added to service", Toast.LENGTH_SHORT).show();
                                            finish();

                                            Intent moveToAdd = new Intent(NewForm.this, ViewServiceRequirements.class);
                                            moveToAdd.putExtra("serviceName", serviceName);
                                            startActivity(moveToAdd);
                                        } else {
                                            //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                                            Toast.makeText(NewForm.this, "There was a problem adding this Form to your service.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        // display an error if there was a problem saving the form to the database
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(NewForm.this, "There was a problem creating this Form.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(NewForm.this, "ERROR.", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}