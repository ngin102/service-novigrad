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

import static android.view.View.INVISIBLE;

public class ViewServiceRequirements extends AppCompatActivity {
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

    private Button addFormButton;
    private Button addDocumentButton;
    private Button backToServiceListButton;

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


        addFormButton = (Button) findViewById(R.id.addFormRequirementButton);
        addDocumentButton = (Button) findViewById(R.id.addDocumentRequirementButton);

        addFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToForms = new Intent(ViewServiceRequirements.this, NewForm.class);
                moveToForms.putExtra("serviceName", serviceName);
                startActivity(moveToForms);
            }
        });

        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToDocuments = new Intent(ViewServiceRequirements.this, NewDocument.class);
                moveToDocuments.putExtra("serviceName2", serviceName);
                startActivity(moveToDocuments);
            }
        });

        backToServiceListButton = (Button) findViewById(R.id.backToServicesButton);

        backToServiceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToServices = new Intent(ViewServiceRequirements.this, ServiceList.class);
                startActivity(moveToServices);
            }
        });


        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementWithDetailsArrayList.clear();
                requirementWithoutDetailsArrayList.clear();
                for (DataSnapshot requirement : snapshot.getChildren()) {
                    if (requirement.hasChild("fields")) {
                        form = requirement.getValue(Form.class);

                        requirementWithDetailsArrayList.add(form.getName() + ": " + "\n" + "      type: " + form.getType() +
                                "\n" + "      TAP TO VIEW FIELDS");
                        requirementWithoutDetailsArrayList.add(requirement.getKey());

                    } else if (requirement.getKey().equals("price")) {
                        String cost = requirement.getValue(String.class);
                        requirementWithDetailsArrayList.add("price: $" + cost);
                        requirementWithoutDetailsArrayList.add(requirement.getKey());
                    } else {
                        document = requirement.getValue(Document.class);
                        requirementWithDetailsArrayList.add(document.getName() + ": " + "\n" + "      type: " + document.getType() +
                                "\n" + "      fileType: " + document.getFileType());
                        requirementWithoutDetailsArrayList.add(requirement.getKey());

                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(ViewServiceRequirements.this, android.R.layout.simple_list_item_1, requirementWithDetailsArrayList);
                requirementList.setAdapter(arrayAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewServiceRequirements.this, "ERROR.", Toast.LENGTH_LONG).show();
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
                String requirementToViewFields = requirementWithoutDetailsArrayList.get(i);
                moveToViewFields(requirementToViewFields);

            }
        });
    }

    private void moveToViewFields(final String requirementToViewFields){
        DatabaseReference requirementReference = serviceInDatabase.child(requirementToViewFields);

        requirementReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("fields")) {
                    Intent moveToView = new Intent(ViewServiceRequirements.this, ViewFields.class);
                    moveToView.putExtra("selectedServiceName", serviceName);
                    moveToView.putExtra("requirementName", requirementToViewFields);
                    startActivity(moveToView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewServiceRequirements.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean updatePrice(String price){
        //Getting the specified service reference
        DatabaseReference priceReference = serviceInDatabase.child("price");

        priceReference.setValue(price).addOnCompleteListener(ViewServiceRequirements.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Since the user's information has been successfully stored in Firebase Database, the registration process is completed!
                    Toast.makeText(ViewServiceRequirements.this, "Price updated!", Toast.LENGTH_SHORT).show();
                } else {
                    //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                    Toast.makeText(ViewServiceRequirements.this, "There was a problem updating the price. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    //Adapted from deleteProduct() method from Lab 5
    private boolean deleteRequirement(String requirement){
        //Getting the specified service reference
        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child(requirement);
        serviceReference.removeValue();
        Toast.makeText(ViewServiceRequirements.this, "Requirement deleted.", Toast.LENGTH_LONG).show();
        return true;
    }




    private void changeKey(final DatabaseReference currentKey, final DatabaseReference newKey) {
        //Moving reference in Firebase.
        final String newKeyChecker = newKey.getKey();
        final DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName);

        serviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.hasChild(newKeyChecker)){
                   Toast.makeText(ViewServiceRequirements.this, "There already exists a requirement by this name. Choose another requirement name.", Toast.LENGTH_LONG).show();
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
                                       Toast.makeText(ViewServiceRequirements.this, "Changed requirement name.", Toast.LENGTH_LONG).show();

                                   } else {
                                       Toast.makeText(ViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(ViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                       }
                   });

                   newKey.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           newKey.child("name").setValue(newKey.getKey());
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(ViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
                       }
                   });

                   deleteRequirement(currentKey.getKey());
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewServiceRequirements.this, "ERROR.", Toast.LENGTH_SHORT).show();
            }
        });
    }





    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showChangeDialog(final String requirement){

        if (requirement.equals("price")){
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
                public void onClick(View view){
                    String newPrice = editTextNewPrice.getText().toString().trim();
                    if (newPrice.equals("")){
                        Toast.makeText(ViewServiceRequirements.this, "Please enter a price.", Toast.LENGTH_SHORT).show();
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
            final View dialogView = inflater.inflate(R.layout.change_dialog_requirement, null);
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
                        Toast.makeText(ViewServiceRequirements.this, "Please enter a requirement name.", Toast.LENGTH_SHORT).show();
                        return;
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