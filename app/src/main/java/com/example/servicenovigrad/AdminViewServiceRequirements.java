package com.example.servicenovigrad;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class displays the existing requirements of a service (forms and documents) to the admin.
 */
public class AdminViewServiceRequirements extends AppCompatActivity {
    // instance variables
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;

    private ListView requirementList;
    private ArrayList<String> requirementWithDetailsArrayList = new ArrayList<String>();
    private ArrayList<String> requirementWithoutDetailsArrayList = new ArrayList<String>();

    private Intent previousScreen;
    private String serviceName;

    private Form form;
    private Document document;

    private TextView requirementsListServiceName;
    private TextView requirementsText;

    private Button addFormButton;
    private Button addDocumentButton;
    private Button backToServiceListButton;

    // instance methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_requirements);

        requirementList = (ListView) findViewById(R.id.serviceRequirementList);

        previousScreen = getIntent();
        serviceName = previousScreen.getStringExtra("serviceName");

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Services").child(serviceName);

        form = new Form();
        document = new Document();

        requirementsListServiceName = (TextView) findViewById(R.id.requirementsListServiceName);
        requirementsListServiceName.setText(serviceName);

        requirementsText = (TextView) findViewById(R.id.requirementsText);


        addFormButton = (Button) findViewById(R.id.addFormRequirementButton);
        addDocumentButton = (Button) findViewById(R.id.addDocumentRequirementButton);

        // launch NewForm
        addFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToForms = new Intent(AdminViewServiceRequirements.this, AdminNewForm.class);
                moveToForms.putExtra("serviceName", serviceName);
                startActivity(moveToForms);
            }
        });

        // launch NewDocument
        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToDocuments = new Intent(AdminViewServiceRequirements.this, AdminNewDocument.class);
                moveToDocuments.putExtra("serviceName2", serviceName);
                startActivity(moveToDocuments);
            }
        });

        backToServiceListButton = (Button) findViewById(R.id.backToServicesButton);

        // launch ServiceList
        backToServiceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToServices = new Intent(AdminViewServiceRequirements.this, AdminViewServiceList.class);
                startActivity(moveToServices);
            }
        });

        // update displayed data when any data is changed
        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementWithDetailsArrayList.clear();
                requirementWithoutDetailsArrayList.clear();

                String cost = ""; 
                for (DataSnapshot requirement: snapshot.getChildren()){
                    if (requirement.getKey().equals("price")) {
                        cost = requirement.getValue(String.class);
                        requirementWithDetailsArrayList.add("Price: $" + cost);
                        requirementWithoutDetailsArrayList.add(requirement.getKey());
                    }
                }
                
                Service selectedService = new Service(serviceName, cost);

                for (DataSnapshot requirement : snapshot.getChildren()) {
                    if (requirement.hasChild("fields")) {
                        form = requirement.getValue(Form.class);

                        requirementWithDetailsArrayList.add("Form: " + form.toString());
                        requirementWithoutDetailsArrayList.add(requirement.getKey());

                    } else if (requirement.getKey().equals("price")) {
                        ;
                    } else {
                        document = requirement.getValue(Document.class);
                        requirementWithDetailsArrayList.add("Document: " + document.toString());
                        requirementWithoutDetailsArrayList.add(requirement.getKey());
                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(AdminViewServiceRequirements.this, android.R.layout.simple_list_item_1, requirementWithDetailsArrayList);
                requirementList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewServiceRequirements.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        //Adapted from Lab 5
        requirementList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String requirementToChange = requirementWithoutDetailsArrayList.get(i);
                showChangeDialog(requirementToChange);
                return true;
            }
        });

        //Only for fields
        requirementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String requirementToView = requirementWithoutDetailsArrayList.get(i);

                String checkIfFormOrDocument = requirementWithDetailsArrayList.get(i);

                if (checkIfFormOrDocument.indexOf(":") == 4){
                    moveToFields(requirementToView);
                }
                if (checkIfFormOrDocument.indexOf(":") == 8){
                    moveToDocumentDetails(requirementToView);
                }

            }
        });
    }

    /**
     * Helper method for launching ViewFields to edit form requirements.
     * Returns nothing.
     */
    private void moveToFields(final String requirementToViewFields){
        Intent moveToView = new Intent(AdminViewServiceRequirements.this, AdminViewFields.class);
        moveToView.putExtra("selectedServiceName", serviceName);
        moveToView.putExtra("requirementName", requirementToViewFields);
        startActivity(moveToView);
    }

    /**
     * Helper method for launching EditDocument to edit a document requirement.
     * Returns nothing.
     */
    private void moveToDocumentDetails(String requirementToViewDetails){
        Intent moveToView2 = new Intent(AdminViewServiceRequirements.this, AdminEditDocument.class);
        moveToView2.putExtra("selectedServiceName", serviceName);
        moveToView2.putExtra("requirementName", requirementToViewDetails);
        startActivity(moveToView2);
    }


    /**
     * Method for updating the price of the requirement.
     * If the task is succesful, returns true and display a success message to the user.
     * Otherwise, returns false and displays an error message to the user.
     */
    private boolean updatePrice(String price){
        //Getting the specified service reference
        DatabaseReference priceReference = serviceInDatabase.child("price");

        //validate the price
        String validatedPrice = ValidateString.validatePrice(price);
        if (validatedPrice == "-1") {
            Toast.makeText(AdminViewServiceRequirements.this, "Please enter a valid form of price", Toast.LENGTH_SHORT).show();
            return false;
        }

        // update the price
        priceReference.setValue(ValidateString.validatePrice(price)).addOnCompleteListener(AdminViewServiceRequirements.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminViewServiceRequirements.this, "Price updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminViewServiceRequirements.this, "There was a problem updating the price. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    //
    /**
     * Adapted from deleteProduct() method from Lab 5
     */
    private boolean deleteRequirement(String requirement){
        //Getting the specified service reference
        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child(requirement);
        serviceReference.removeValue();
        Toast.makeText(AdminViewServiceRequirements.this, "Requirement deleted.", Toast.LENGTH_LONG).show();
        return true;
    }




    private void changeKey(final DatabaseReference currentKey, final DatabaseReference newKey) {
        //Moving reference in Firebase.
        final String newKeyChecker = newKey.getKey();
        final DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName);

        firebaseDatabase.getReference("Services").child(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requirement : snapshot.getChildren()) {
                    String storedRequirementName = requirement.getKey();

                    if (storedRequirementName.toLowerCase().equals(newKeyChecker.toLowerCase())) {
                        Toast.makeText(AdminViewServiceRequirements.this, "There is already a Form with this name. Please choose a new Form name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                serviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(newKeyChecker)) {
                            Toast.makeText(AdminViewServiceRequirements.this, "There already exists a requirement by this name. Choose another requirement name.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            //Moving reference in Firebase.
                            currentKey.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    newKey.setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                Toast.makeText(AdminViewServiceRequirements.this, "Changed requirement name.", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(AdminViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AdminViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            newKey.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    newKey.child("name").setValue(newKey.getKey());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AdminViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            deleteRequirement(currentKey.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewServiceRequirements.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });
    }


    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showChangeDialog(final String requirement) {

        if (requirement.equals("price")) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.change_dialog_requirement_price, null);
            dialogBuilder.setView(dialogView);

            final EditText editTextNewPrice = (EditText) dialogView.findViewById(R.id.editTextNewPrice);
            final Button updatePriceButton = (Button) dialogView.findViewById(R.id.updatePriceButton);

            dialogBuilder.setTitle(requirement);
            final AlertDialog alert = dialogBuilder.create();
            alert.show();

            updatePriceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newPrice = editTextNewPrice.getText().toString().trim();
                    if (newPrice.equals("")) {
                        Toast.makeText(AdminViewServiceRequirements.this, "Please enter a price.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updatePrice(newPrice);
                    alert.dismiss();
                }
            });
        }


        else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.change_dialog_form, null);
            dialogBuilder.setView(dialogView);


            final Button deleteRequirementButton = (Button) dialogView.findViewById(R.id.deleteRequirementButton);
            final Button updateRequirementButton = (Button) dialogView.findViewById(R.id.updateRequirementNameButton);
            final EditText editTextNewRequirementName = (EditText) dialogView.findViewById(R.id.editTextNewRequirementName);

            dialogBuilder.setTitle(requirement);
            final AlertDialog alert = dialogBuilder.create();
            alert.show();

            deleteRequirementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    deleteRequirement(requirement);
                    alert.dismiss();
                }
            });

            updateRequirementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    String newKey = editTextNewRequirementName.getText().toString().trim();
                    if (newKey.equals("")){
                        Toast.makeText(AdminViewServiceRequirements.this, "Please enter a Form name.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String validatedNewKey = ValidateString.validateServiceName(newKey);
                    if (validatedNewKey.equals("-1")) {
                        Toast.makeText(AdminViewServiceRequirements.this, "Invalid Form name. Make sure your Form name begins with a letter and is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        newKey = validatedNewKey;
                    }

                    DatabaseReference currentRequirementName = firebaseDatabase.getReference("Services").child(serviceName).child(requirement);
                    DatabaseReference newRequirementName = firebaseDatabase.getReference("Services").child(serviceName).child(newKey);

                    changeKey(currentRequirementName, newRequirementName);
                    alert.dismiss();
                }
            });
        }
    }
}