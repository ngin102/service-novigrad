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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceList extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;

    private ListView serviceList;
    private ArrayList<String> serviceArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Services");
        serviceList = (ListView) findViewById(R.id.serviceList);

        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren() ) {
                    String key = service.getKey();
                    serviceArrayList.add(key);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(ServiceList.this, android.R.layout.simple_list_item_1, serviceArrayList);
                serviceList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        //Adapted from Lab 5
        serviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToChange = serviceArrayList.get(i);
                showChangeDialog(serviceToChange);
                return true;
            }
        });
    }

    //Adapted from deleteProduct() method from Lab 5
    private boolean deleteService(String serviceName){
        //Getting the specified service reference
        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName);
        serviceReference.removeValue();
        Toast.makeText(ServiceList.this, "Service deleted.", Toast.LENGTH_LONG).show();
        return true;
    }

    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showChangeDialog(final String serviceName){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_dialog_service, null);
        dialogBuilder.setView(dialogView);

        final Button deleteServiceButton = (Button) dialogView.findViewById(R.id.deleteServiceButton);
        final Button viewRequirementsButton = (Button) dialogView.findViewById(R.id.viewRequirementsButton);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog alert = dialogBuilder.create();
        alert.show();

        deleteServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteService(serviceName);
                alert.dismiss();
            }
        });

        viewRequirementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToView = new Intent(ServiceList.this, ViewServiceRequirements.class);
                moveToView.putExtra("serviceName", serviceName);
                startActivity(moveToView);
            }
        });
    }



}