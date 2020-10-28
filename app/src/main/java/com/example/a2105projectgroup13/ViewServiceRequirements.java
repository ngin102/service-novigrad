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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

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


        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementWithDetailsArrayList.clear();
                for (DataSnapshot requirement : snapshot.getChildren()) {
                    if (requirement.hasChild("fields")) {
                        form = requirement.getValue(Form.class);

                        requirementWithDetailsArrayList.add(form.getFormName() + ": " + "\n" + "      type: " + form.getType() +
                                "\n" + "      TAP TO VIEW FIELDS");
                        requirementWithoutDetailsArrayList.add(requirement.getKey());

                    } else if (requirement.getKey().equals("price")) {
                        String cost = requirement.getValue(String.class);
                        requirementWithDetailsArrayList.add("price: $" + cost);
                        requirementWithoutDetailsArrayList.add(requirement.getKey());
                    } else {
                        document = requirement.getValue(Document.class);
                        requirementWithDetailsArrayList.add(document.getDocumentName() + ": " + "\n" + "      type: " + document.getType() +
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
                Toast.makeText(ViewServiceRequirements.this, requirementToChange, Toast.LENGTH_LONG).show();
                showChangeDialog(requirementToChange);
                return true;
            }
        });
    }

    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showChangeDialog(final String requirement){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_dialog_service, null);
        dialogBuilder.setView(dialogView);

        final String requirementName = requirement;

        final Button deleteServiceButton = (Button) dialogView.findViewById(R.id.deleteServiceButton);
        final Button viewRequirementsButton = (Button) dialogView.findViewById(R.id.viewRequirementsButton);

        dialogBuilder.setTitle(requirement);
        final AlertDialog alert = dialogBuilder.create();
        alert.show();

        viewRequirementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToView = new Intent(ViewServiceRequirements.this, ViewFields.class);
                moveToView.putExtra("selectedServiceName", serviceName);
                moveToView.putExtra("requirementName", requirementName);
                startActivity(moveToView);
            }
        });
    }
}