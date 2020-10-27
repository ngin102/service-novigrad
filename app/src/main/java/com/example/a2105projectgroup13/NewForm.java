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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewForm extends AppCompatActivity {
    private EditText editTextFormName;
    private LinearLayout fieldList;
    private Button addFieldButton;
    private Button addFormButton;
    private FirebaseDatabase firebaseDatabase;

    private ArrayList<String> fields;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_form);

        editTextFormName = findViewById(R.id.editTextFormName);
        fieldList = findViewById(R.id.fieldList);
        addFieldButton = findViewById(R.id.addFieldButton);
        addFormButton = findViewById(R.id.addFormButton);
        fields = new ArrayList<>();
        firebaseDatabase = firebaseDatabase.getInstance();

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

    private void addFieldToList(){
        LayoutInflater inflater = getLayoutInflater();
        final View field = inflater.inflate(R.layout.add_field, null);

        Button removeFieldButton = (Button) field.findViewById(R.id.removeFieldButton);

        removeFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                fieldList.removeView(field);
            }
        });

        fieldList.addView(field);
    }


    private boolean validateForEmptyFields(){
        for (int i = 0; i < fieldList.getChildCount(); i++){
            View selectedField = fieldList.getChildAt(i);
            EditText selectedFieldName = (EditText) selectedField.findViewById(R.id.editTextFieldName);
            String fieldName = selectedFieldName.getText().toString().trim();

            if (fieldName.isEmpty()) {
                Toast.makeText(NewForm.this, "Please name all fields or remove unnamed fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public void submitFormOnClick(View v) {
        String formName = editTextFormName.getText().toString().trim();

        if (formName.equals("")){
            Toast.makeText(NewForm.this, "Please enter a name for the Form.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validateForEmptyFields() == true && (! formName.equals("") )) {
            fields.clear();
            for (int i = 0; i < fieldList.getChildCount(); i++) {
                View selectedField = fieldList.getChildAt(i);
                EditText selectedFieldName = (EditText) selectedField.findViewById(R.id.editTextFieldName);
                String fieldName = selectedFieldName.getText().toString().trim();

                fields.add(fieldName);
            }

            final Form formToAddToService = new Form("form", formName);

            firebaseDatabase.getReference("Services").child("Service Name").child(formName).setValue(formToAddToService).addOnCompleteListener(NewForm.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        DatabaseReference fieldsInFirebase = firebaseDatabase.getReference("Services").child("Service Name").child("fields");
                        for (int i = 0; i < fields.size(); i++) {
                            String fieldToAdd = fields.get(i);
                            fieldsInFirebase.child(Integer.toString(i)).setValue(fieldToAdd);
                        }

                        Toast.makeText(NewForm.this, "Form added to service", Toast.LENGTH_SHORT).show();
                    } else {
                        //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                        Toast.makeText(NewForm.this, "There was a problem adding this Form to your service.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}