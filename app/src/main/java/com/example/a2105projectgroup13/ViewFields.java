package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class allows the Admin to view and edit the fields (forms) of a service.
 */

public class ViewFields extends AppCompatActivity {
    // instance variables
    private DatabaseReference requirementInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference fieldsInDatabase;

    private Intent requirementScreen;
    private String serviceName;
    private String requirementName;

    private ListView fieldList;
    private TextView requirementNameOnScreen;
    private ArrayList<String> fieldArrayList = new ArrayList<String>();

    private ArrayList<String> fieldKeyArrayList = new ArrayList<String>();

    private Button backToServiceListButton;
    private Button backToRequirementListButton;
    private Button addFieldButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fields);

        fieldList = (ListView) findViewById(R.id.fieldScrollingList);

        requirementScreen = getIntent();
        serviceName = requirementScreen.getStringExtra("selectedServiceName");
        requirementName = requirementScreen.getStringExtra("requirementName");

        firebaseDatabase = firebaseDatabase.getInstance();
        requirementInDatabase = firebaseDatabase.getReference("Services").child(serviceName).child(requirementName);
        fieldsInDatabase = requirementInDatabase.child("fields");

        requirementNameOnScreen = (TextView) findViewById(R.id.changeableRequirementName);
        requirementNameOnScreen.setText(requirementName);

        backToServiceListButton = (Button) findViewById(R.id.backToServicesButtonOnFields);
        backToServiceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToServices = new Intent(ViewFields.this, ServiceList.class);
                startActivity(moveToServices);
            }
        });

        backToRequirementListButton = (Button) findViewById(R.id.backToRequirementListButton);
        backToRequirementListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToRequirements = new Intent(ViewFields.this, ViewServiceRequirements.class);
                moveToRequirements.putExtra("serviceName", serviceName);
                startActivity(moveToRequirements);
            }
        });

        addFieldButton = (Button) findViewById(R.id.addFieldButtonOnFields);
        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToFields = new Intent(ViewFields.this, AddField.class);
                moveToFields.putExtra("serviceName", serviceName);
                moveToFields.putExtra("requirementName", requirementName);
                startActivity(moveToFields);
            }
        });

        fieldsInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fieldArrayList.clear();
                fieldKeyArrayList.clear();
                for (DataSnapshot field : snapshot.getChildren() ) {
                    String fieldValue = field.getValue(String.class);
                    fieldArrayList.add(fieldValue);
                    fieldKeyArrayList.add(field.getKey());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(ViewFields.this, android.R.layout.simple_list_item_1, fieldArrayList);
                fieldList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFields.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        //Adapted from Lab 5
        fieldList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fieldToChange = fieldKeyArrayList.get(i);
                showChangeDialog(fieldToChange);
                return true;
            }
        });


    }

    /**
     Updates the value of a field in the database for an associated reference.
     */
    private void changeValue(final DatabaseReference key, final String newValue) {
        //Moving reference in Firebase.
        final DatabaseReference fieldReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child(requirementName).child("fields");

        fieldReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot field : snapshot.getChildren() ) {
                    String fieldValue = field.getValue(String.class);

                    if (fieldValue.toLowerCase().equals(newValue.toLowerCase())){
                        Toast.makeText(ViewFields.this, "There is already a field with this name. Please choose a new field name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                key.setValue(newValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFields.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });
    }


    //Adapted from deleteProduct() method from Lab 5
    private boolean deleteField(final String specificFieldKey){

        final DatabaseReference fieldReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child(requirementName).child("fields").child(specificFieldKey);

        final DatabaseReference allFieldsReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child(requirementName).child("fields");
        allFieldsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 1){
                    Toast.makeText(ViewFields.this, "Can not delete field. You must have at least one field in your Form.", Toast.LENGTH_LONG).show();
                    return;
                }

                else {
                    fieldReference.removeValue();

                    allFieldsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int count = 0;

                            for (DataSnapshot field : snapshot.getChildren() ) {
                                String fieldValue = field.getValue(String.class);

                                DatabaseReference specificFieldReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child(requirementName).child("fields").child(field.getKey());
                                specificFieldReference.removeValue();
                                allFieldsReference.child(Integer.toString(count)).setValue(fieldValue);
                                count++;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ViewFields.this, "ERROR.", Toast.LENGTH_LONG).show();
                        }
                    });

                    Toast.makeText(ViewFields.this, "Field deleted.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFields.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        return true;
    }



    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showChangeDialog(final String specificField){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_dialog_field, null);
        dialogBuilder.setView(dialogView);

        final Button deleteFieldButton = (Button) dialogView.findViewById(R.id.deleteFieldButton);
        final Button editFieldNameButton = (Button) dialogView.findViewById(R.id.editFieldNameButton);
        final EditText editTextFieldNameOnList = (EditText) dialogView.findViewById(R.id.editTextFieldNameOnList);

        final AlertDialog alert = dialogBuilder.create();
        alert.show();

        deleteFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteField(specificField);
                alert.dismiss();
            }
        });

        editFieldNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String newValue = editTextFieldNameOnList.getText().toString().trim();
                if (newValue.equals("")){
                    Toast.makeText(ViewFields.this, "Please enter a field name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String validatedNewValue = ValidateString.validateServiceName(newValue);
                if (validatedNewValue.equals("-1")) {
                    Toast.makeText(ViewFields.this, "Invalid Field name. Make sure your Field name is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    newValue = validatedNewValue;
                }

                DatabaseReference fieldReference = firebaseDatabase.getReference("Services").child(serviceName).child(requirementName).child("fields").child(specificField);

                changeValue(fieldReference, newValue);
                alert.dismiss();
            }
        });
    }
}