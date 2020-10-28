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
import com.google.firebase.database.FirebaseDatabase;


public class NewDocument extends AppCompatActivity {
    private EditText editTextDocumentName;
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

        serviceNameOnScreen.setText(serviceName);

        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                submitDocumentOnClick(view);
            }
        });
    }

    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.
        editTextDocumentName= findViewById(R.id.editTextDocumentName);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        choosePDFButton = findViewById(R.id.choosePDFButton);
        addDocumentButton = findViewById(R.id.addDocumentButton);
        chooseFileTypeRadioGroup = findViewById(R.id.chooseFileTypeRadioGroup);
        firebaseDatabase = firebaseDatabase.getInstance();

        previousScreen = getIntent();
        serviceName = previousScreen.getStringExtra("serviceName");

        serviceNameOnScreen = findViewById(R.id.serviceNameOnScreen3);
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

    public void submitDocumentOnClick(View v) {
        final String documentName = editTextDocumentName.getText().toString().trim();

        if (documentName.equals("")){
            Toast.makeText(NewDocument.this, "Please enter a name for the Document.", Toast.LENGTH_SHORT).show();
            return;
        }

        String chooseFileType = checkRadioButtonChoice(chooseFileTypeRadioGroup);
        if (chooseFileType.equals("-1")){
            Toast.makeText(NewDocument.this, "Please select a file type.", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( ( ! chooseFileType.equals("-1") ) && (! documentName.equals("") )) {
            final Document documentToAddToService = new Document("document", documentName, chooseFileType);

            firebaseDatabase.getReference("Services").child(serviceName).child(documentName).setValue(documentToAddToService).addOnCompleteListener(NewDocument.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(NewDocument.this, "Document added to service", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(NewDocument.this, ManageServices.class));
                    } else {
                        //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                        Toast.makeText(NewDocument.this, "There was a problem adding this Document to your service.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}