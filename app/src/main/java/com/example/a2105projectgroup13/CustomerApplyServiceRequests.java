package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * This class allows a Customer to submit a Service Request by filling out all required fields
 * and uploading all required Documents.
 */
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


    TextView changeableCustomerIDTextView;
    TextView changeableServiceNameTextView;
    TextView changeableBranchIDTextView;;
    TextView changeableServicePriceTextView;
    TextView changeableServiceRequestTextView;

    DatabaseReference serviceReference;

    LinearLayout formsAndFieldsInputList;
    LinearLayout documentsList;

    Button applyButton;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Uri imageUri;

    String filename;

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

                            //If the field name is "Street Address", add the view made specifically for street address input.
                            if (currentFieldName.toLowerCase().contains("street address") || currentFieldName.toLowerCase().contains("streetaddress")){
                                LayoutInflater inflater = getLayoutInflater();
                                final View field = inflater.inflate(R.layout.fill_out_field_street_address, null);

                                TextView formName1 = (TextView) field.findViewById(R.id.applyFormName);
                                formName1.setText(currentFormName + ":");

                                TextView fieldName1 = (TextView) field.findViewById(R.id.applyFieldName);
                                fieldName1.setText(currentFieldName + ":");

                                formsAndFieldsInputList.addView(field);
                            }

                            //If the field name is not "Street Address', add the normal view for field input.
                            else {
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
                        final String requirementName = requirement.getKey();

                        final Document selectedDocument = requirement.getValue(Document.class);

                        LayoutInflater inflater = getLayoutInflater();
                        final View uploadingDocument = inflater.inflate(R.layout.upload_document, null);

                        TextView uploadDocumentName = (TextView) uploadingDocument.findViewById(R.id.uploadDocumentName);
                        uploadDocumentName.setText(requirementName);

                        TextView uploadStatus = (TextView) uploadingDocument.findViewById(R.id.uploadStatus);
                        uploadStatus.setText("UNUPLOADED");

                        TextView uploadDocumentInfo = (TextView) uploadingDocument.findViewById(R.id.uploadDocumentInfo);
                        uploadDocumentInfo.setText("File type: " + selectedDocument.getFileType() + "\n" + "Description: " + selectedDocument.getDescription());

                        Button uploadButton = (Button) uploadingDocument.findViewById(R.id.uploadButton);

                        uploadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                            filename = requirementName;
                            Toast.makeText(CustomerApplyServiceRequests.this, "Upload a file for " + filename + ".", Toast.LENGTH_LONG).show();
                            openGallery();
                            }
                        });

                        documentsList.addView(uploadingDocument);
                    }
                }

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
        documentsList = findViewById(R.id.applyDocumentsList);

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

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
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


    /**
     * Set the request number of this Service Request.
     */
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


    /**
     * Get the request number of this Service Request.
     */
    private String getRequestNumber(){
        return changeableServiceRequestTextView.getText().toString().trim();
    }


    /**
     * Get the price of this Service.
     */
    private String getPrice(){
        return changeableServicePriceTextView.getText().toString().trim();
    }


    /**
     * Method that checks that all fields are filled by the Customer.
     * Returns true if the fields are filled appropriately, or false if they are not.
     */
    private boolean validateForEmptyFields(){
        for (int i = 0; i < formsAndFieldsInputList.getChildCount(); i++){
            View selectedField = formsAndFieldsInputList.getChildAt(i);

            TextView fieldName = (TextView) selectedField.findViewById(R.id.applyFieldName);
            String fieldNameString = fieldName.getText().toString().trim();

            if (fieldNameString.toLowerCase().contains("street address") || fieldNameString.toLowerCase().contains("streetaddress") ){
                EditText userStreetNumberInput = (EditText) selectedField.findViewById(R.id.fieldInputEditTextStreetNumber);
                String streetNumber = userStreetNumberInput.getText().toString().trim();

                EditText userStreetAddressInput = (EditText) selectedField.findViewById(R.id.fieldInputEditTextStreetAddress);
                String streetAddress = userStreetAddressInput.getText().toString().trim();

                if (streetNumber.isEmpty()) {
                    Toast.makeText(CustomerApplyServiceRequests.this, "Please fill out a street number.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (streetAddress.isEmpty()) {
                    Toast.makeText(CustomerApplyServiceRequests.this, "Please fill out a street address.", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }

            else {
                EditText userInput = (EditText) selectedField.findViewById(R.id.fieldInputEditText);
                String input = userInput.getText().toString().trim();

                // display an error if a field is empty
                if (input.isEmpty()) {
                    Toast.makeText(CustomerApplyServiceRequests.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Method that checks that all fields are filled by the Customer.
     * Returns true if the fields are filled appropriately, or false if they are not.
     */
    private boolean validateForUnuploadedDocuments(){
        for (int i = 0; i < documentsList.getChildCount(); i++){
            View document = documentsList.getChildAt(i);

            TextView uploadDocumentName = (TextView) document.findViewById(R.id.uploadDocumentName);
            String documentName = uploadDocumentName.getText().toString();

            TextView uploadDocumentStatus = (TextView) document.findViewById(R.id.uploadStatus);
            String uploadStatus = uploadDocumentStatus.getText().toString();

            if (uploadStatus.equals("UNUPLOADED")){
                Toast.makeText(CustomerApplyServiceRequests.this, "Please upload the " + documentName + "!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


    /**
     * Method to upload a photo.
     */
    private void uploadDocument(final String filename) {
        final String requestNumber = getRequestNumber();
        StorageReference referenceToDocument = firebaseStorage.getReference().child(branchId + "/" + requestNumber + "/" + filename);

        referenceToDocument.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int j = 0;
                        boolean found = false;
                        while (j < documentsList.getChildCount() && found == false){
                            View uploadingDocument = documentsList.getChildAt(j);

                            TextView uploadDocumentName = (TextView) uploadingDocument.findViewById(R.id.uploadDocumentName);
                            String documentInfo = uploadDocumentName.getText().toString();

                            if (documentInfo.equals(filename)){
                                TextView uploadStatus = (TextView) uploadingDocument.findViewById(R.id.uploadStatus);
                                uploadStatus.setText("UPLOADED");
                                found = true;
                                Toast.makeText(CustomerApplyServiceRequests.this, "Successfully uploaded " + filename + "!", Toast.LENGTH_SHORT).show();
                            }

                            j++;

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(CustomerApplyServiceRequests.this, "Error uploading. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * Method to open Android Gallery.
     */
    private void openGallery() {
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(openGallery, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadDocument(filename);
        }
    }


    /**
     * Method for uploading the Customer's inputs for the fields.
     */
    private void applyOnClick(View v) {
        final String requestNumber = getRequestNumber();
        final String price = getPrice();

        //Validating fields in Driver's License, Health Card and Photo ID
            //Fields used in these Forms:
                //We have split up the field "Address" into three different fields:
                    //* Street Address
                    //* City
                    //* Postal Code

                //* First name and Last name
                //* Date of Birth
                //* License Type (G1, G2 or G)


        if (validateForEmptyFields() == true && validateForUnuploadedDocuments() == true) {
            //Store field inputs in Firebase
            for (int i = 0; i < formsAndFieldsInputList.getChildCount(); i++) {
                View selectedField = formsAndFieldsInputList.getChildAt(i);

                TextView formNameTextView = (TextView) selectedField.findViewById(R.id.applyFormName);
                String formName = formNameTextView.getText().toString().trim();


                TextView fieldNameTextView = (TextView) selectedField.findViewById(R.id.applyFieldName);
                String fieldName = fieldNameTextView.getText().toString().trim();


                //If the field is "Street Address," validate the Street Address.
                if (fieldName.toLowerCase().contains("street address") || fieldName.toLowerCase().contains("streetaddress")) {
                    EditText userInputStreetNumber = (EditText) selectedField.findViewById(R.id.fieldInputEditTextStreetNumber);
                    String input1 = userInputStreetNumber.getText().toString().trim();

                    EditText userInputStreetAddress = (EditText) selectedField.findViewById(R.id.fieldInputEditTextStreetAddress);
                    String input2 = userInputStreetAddress.getText().toString().trim();

                    fieldName = fieldName.substring(0, fieldName.length() - 1);


                    if (input2.startsWith("-")){
                        Toast.makeText(CustomerApplyServiceRequests.this, "You can not start a street address with a hyphen.", Toast.LENGTH_SHORT).show();
                        firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                        return;
                    }
                    else if (input2.endsWith("-")) {
                        Toast.makeText(CustomerApplyServiceRequests.this, "You can not end a street address with a hyphen.", Toast.LENGTH_SHORT).show();
                        firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                        return;
                    }
                    else if (input2.equals("-")){
                        Toast.makeText(CustomerApplyServiceRequests.this, "' - ' is not a valid street address.", Toast.LENGTH_SHORT).show();
                        firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                        return;
                    }
                    else {
                        String validateStreetAddress = ValidateString.validateAddressOrCity(input2);
                        if (validateStreetAddress.equals("-1")) {
                            Toast.makeText(CustomerApplyServiceRequests.this, "Street addresses can only be alphabetic; they can also include hyphens. Please enter a valid street address.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;
                        } else {
                            input2 = validateStreetAddress;
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Fields").child(formName + " " + fieldName).setValue(input1 + " " + input2);
                        }
                    }
                }

                //If the field is not "Street Address"...
                else {
                    EditText userInput = (EditText) selectedField.findViewById(R.id.fieldInputEditText);
                    String input = userInput.getText().toString().trim();

                    fieldName = fieldName.substring(0, fieldName.length() - 1);

                    //Validating for First name and Last name (and their variations, including 'Name')
                    if (fieldName.toLowerCase().contains("first name") || fieldName.toLowerCase().contains("firstname")
                            || fieldName.toLowerCase().contains("last name") || fieldName.toLowerCase().contains("lastname")
                            || fieldName.toLowerCase().contains("name")) {

                        String validatedInput = ValidateString.validateName(input);
                        if (validatedInput.equals("-1")) {
                            Toast.makeText(CustomerApplyServiceRequests.this, "Invalid name. Names must be alphanumeric and must at least be two characters long.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;

                        } else {
                            input = validatedInput;
                        }
                    }

                    //Validating for Date of Birth
                    if (fieldName.toLowerCase().contains("date of birth") || fieldName.toLowerCase().contains("dateofbirth")
                            || fieldName.toLowerCase().contains("day of birth") || fieldName.toLowerCase().contains("dayofbirth")
                            || fieldName.toLowerCase().contains("birthday") || fieldName.toLowerCase().contains("birthdate")
                            || fieldName.toLowerCase().contains("birth date")) {

                        String validatedInput = ValidateString.validateDateOfBirth(input);
                        if (validatedInput.equals("-1")) {
                            Toast.makeText(CustomerApplyServiceRequests.this, "Invalid date of birth. Use the format mm/dd/yyyy, where mm is any two digit integer between 00 and 12, dd is any two digit integer between 01 and 31 and yyyy is any four digit integer.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;

                        } else {
                            input = validatedInput;
                        }
                    }


                    //Validating for License Type (G1, G2 or G)
                    if (fieldName.toLowerCase().contains("license type") || fieldName.toLowerCase().contains("license type (g1, g2 or g)") || fieldName.toLowerCase().contains("licensetype")) {
                        if (!input.toLowerCase().equals("g") && !input.toLowerCase().equals("g1") && !input.toLowerCase().equals("g2")) {
                            Toast.makeText(CustomerApplyServiceRequests.this, "Invalid license type. A license type must be g1, g2 or g.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;
                        }

                        else {
                            input = input.toUpperCase();
                        }
                    }


                    //Validating for City name
                    if (fieldName.toLowerCase().equals("city")){
                        if (input.startsWith("-")){
                            Toast.makeText(CustomerApplyServiceRequests.this, "You can not start a city name with a hyphen.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;
                        }
                        else if (input.equals("-")){
                            Toast.makeText(CustomerApplyServiceRequests.this, "' - ' is not a city name.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;
                        }

                        else if (input.endsWith("-")){
                            Toast.makeText(CustomerApplyServiceRequests.this, "A city name that ends with ' - ' is not valid.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;
                        }
                        else {
                            String validatedInput = ValidateString.validateAddressOrCity(input);
                            if (validatedInput.equals("-1")) {
                                Toast.makeText(CustomerApplyServiceRequests.this, "City names can only be alphabetic; they can also include hyphens. Please enter a valid city name.", Toast.LENGTH_SHORT).show();
                                firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                                return;

                            } else {
                                input = validatedInput;
                            }
                        }
                    }


                    //Validating for Postal Code
                    if (fieldName.toLowerCase().contains("postal code") || fieldName.toLowerCase().contains("postalcode")){
                        if (input.length() != 6){
                            Toast.makeText(CustomerApplyServiceRequests.this, "Postal codes are 6 characters long. Please enter a valid postal code.", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                            return;
                        }
                        else {
                            String validatedInput = ValidateString.validatePostalCode(input);
                            if (validatedInput.equals("-1")) {
                                Toast.makeText(CustomerApplyServiceRequests.this, "Postal codes are 6 characters long. Postal codes follow the format 'A0A0A0', where A is any letter and 0 is any number. No spaces. Please enter a valid postal code.", Toast.LENGTH_SHORT).show();
                                firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).removeValue();
                                return;

                            } else {
                                input = validatedInput;
                            }
                        }
                    }

                    //Store field input in Firebase.
                    firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Fields").child(formName + " " + fieldName).setValue(input);

                }
            }

            //Store uploaded file names in Firebase
            for (int j = 0; j < documentsList.getChildCount(); j++){
                View document = documentsList.getChildAt(j);

                TextView uploadDocumentName = (TextView) document.findViewById(R.id.uploadDocumentName);
                String documentName = uploadDocumentName.getText().toString();

                firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Documents").child(documentName).setValue(documentName);
            }

            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Price").setValue(price);
            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Service").setValue(serviceName);
            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("CustomerID").setValue(customerId);

            firebaseDatabase.getReference("Service Requests").child(branchId).child(requestNumber).child("Status").setValue("Pending");
            Toast.makeText(CustomerApplyServiceRequests.this, "Request application sent!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}