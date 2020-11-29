package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerApplyServiceRequests extends AppCompatActivity {
    //instance variables
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    ArrayList<String> documentsArrayList = new ArrayList<String>();

    ArrayList<String> displayDocumentsArrayList = new ArrayList<String>();

    Intent previousScreen;
    String branchId;
    String customerId;
    String serviceName;

    //ListView formFilledFieldsListView
    ListView documentsListView;

    TextView changeableCustomerIDTextView;
    TextView changeableServiceNameTextView;
    TextView changeableBranchIDTextView;;
    TextView changeableServicePriceTextView;
    TextView changeableServiceRequestTextView;

    DatabaseReference serviceReference;

    Form form;
    Document document;

    LinearLayout formsAndFieldsInputList;

    Button applyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_apply_service_requests);

        initializeInstanceVariables();
        setPrice();
        setRequestNumber();

        //Forms/Fields List

        serviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requirement : snapshot.getChildren()) {
                    if (requirement.hasChild("fields")) {
                        String currentFormName = requirement.getKey();

                        for (DataSnapshot fields : requirement.child("fields").getChildren()){
                            String currentFieldName = fields.getValue(String.class);

                            LayoutInflater inflater = getLayoutInflater();
                            final View field = inflater.inflate(R.layout.fill_out_field, null);
                            TextView formName = (TextView) field.findViewById(R.id.applyFormName);
                            formName.setText(currentFormName + ":");
                            TextView fieldName = (TextView) field.findViewById(R.id.applyFieldName);
                            fieldName.setText(currentFieldName + ":");

                            formsAndFieldsInputList.addView(field);
                        }
                    }
                }
            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerApplyServiceRequests.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        //Documents List
        serviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                documentsArrayList.clear();
                displayDocumentsArrayList.clear();

                for (DataSnapshot requirement : snapshot.getChildren()) {
                    if (! requirement.hasChild("fields") && ! requirement.getKey().equals("price")) {
                        document = requirement.getValue(Document.class);
                        documentsArrayList.add(requirement.getKey());
                        displayDocumentsArrayList.add(document.toString());
                    }
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerApplyServiceRequests.this, android.R.layout.simple_list_item_1, documentsArrayList);
                documentsListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerApplyServiceRequests.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                applyOnClick(view);
            }
        });
    }

    /**
     * Initializes all of the instance variables.
     */
    private void initializeInstanceVariables() {
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        //initialize ListViews
       // formFilledFieldsListView = (ListView)findViewById(R.id.applyFormFieldsListView);
        documentsListView = (ListView)findViewById(R.id.applyDocumentsListView);

        //initialize TextViews
        changeableCustomerIDTextView = (TextView)findViewById(R.id.applyChangeableCustomerIDTextView);
        changeableServiceNameTextView = (TextView)findViewById(R.id.applyChangeableServiceNameTextView);;
        changeableBranchIDTextView = (TextView)findViewById(R.id.applyChangeableBranchIDTextView);
        changeableServicePriceTextView = (TextView)findViewById(R.id.applyChangeableServicePriceTextView);
        changeableServiceRequestTextView = (TextView)findViewById(R.id.applyChangeableServiceRequestTextView);

        //getting the customer id
        FirebaseUser customerAccount = firebaseAuth.getInstance().getCurrentUser();
        customerId = customerAccount.getUid();
        changeableCustomerIDTextView.setText(customerId);

        //get intent
        previousScreen = getIntent();
        branchId = previousScreen.getStringExtra("branchID");
        changeableBranchIDTextView.setText(branchId);

        serviceName = previousScreen.getStringExtra("serviceName");
        changeableServiceNameTextView.setText(serviceName);

        serviceReference = firebaseDatabase.getReference("Services").child(serviceName);

        formsAndFieldsInputList = findViewById(R.id.formsAndFieldsInputList);

        applyButton = findViewById(R.id.applyButton);
    }

    /**
     * Displays the details of the service request in the TextViews on the activity screen.
     */
    private void setPrice(){
        DatabaseReference priceReference = firebaseDatabase.getReference("Services").child(serviceName).child("price");

        priceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                changeableServicePriceTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerApplyServiceRequests.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    private void setRequestNumber(){
        DatabaseReference requestsReference = firebaseDatabase.getReference("Service Requests").child(branchId);

        requestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int value = (int) dataSnapshot.getChildrenCount() + 1;
                changeableServiceRequestTextView.setText("Request " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerApplyServiceRequests.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    private String getRequestNumber(){
        return changeableServiceRequestTextView.getText().toString().trim();
    }

    private String getPrice(){
        return changeableServicePriceTextView.getText().toString().trim();
    }

    /**
     Method which checks that all fields are filled by the Customer.
     Returns true if the fields are filled appropriately, or false if they are not.
     */
    private boolean validateForEmptyFields(){
        for (int i = 0; i < formsAndFieldsInputList.getChildCount(); i++){
            View selectedField = formsAndFieldsInputList.getChildAt(i);
            EditText userInput = (EditText) selectedField.findViewById(R.id.fieldInputEditText);
            String input = userInput.getText().toString().trim();

            // display an error if a field is empty
            if (input.isEmpty()) {
                Toast.makeText(CustomerApplyServiceRequests.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    /**
     Method for uploading the Customer's inputs for the fields.
     */
    private void applyOnClick(View v) {
        String requestNumber = getRequestNumber();
        String price = getPrice();

        if (validateForEmptyFields() == true){
            for (int i = 0; i < formsAndFieldsInputList.getChildCount(); i++) {
                View selectedField = formsAndFieldsInputList.getChildAt(i);
                EditText userInput = (EditText) selectedField.findViewById(R.id.fieldInputEditText);
                String input = userInput.getText().toString().trim();

                TextView formNameTextView = (TextView) selectedField.findViewById(R.id.applyFormName);
                String formName = formNameTextView.getText().toString().trim();

                TextView fieldNameTextView = (TextView) selectedField.findViewById(R.id.applyFieldName);
                String fieldName = fieldNameTextView.getText().toString().trim();
                fieldName = fieldName.substring(0, fieldName.length()-1);

                firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Fields").child(formName + " " + fieldName).setValue(input);
            }

            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Price").setValue(price);
            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Service").setValue(serviceName);
            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("CustomerID").setValue(customerId);
            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Status").setValue("Pending");
            Toast.makeText(CustomerApplyServiceRequests.this, "Application sent!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}