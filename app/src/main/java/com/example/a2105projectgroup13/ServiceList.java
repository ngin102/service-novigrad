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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToViewRequirements = serviceArrayList.get(i);
                Intent moveToView = new Intent(ServiceList.this, ViewServiceRequirements.class);
                moveToView.putExtra("serviceName", serviceToViewRequirements);
                startActivity(moveToView);
            }
        });
    }


    private void changeKey(final DatabaseReference currentKey, final DatabaseReference newKey) {
        //Moving reference in Firebase.
        final String newKeyChecker = newKey.getKey();
        final DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services");

        serviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(newKeyChecker)){
                    Toast.makeText(ServiceList.this, "There already exists a Service by this name. Choose another Service name.", Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(ServiceList.this, "Changed Service name.", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(ServiceList.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ServiceList.this, "ERROR.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                    deleteService(currentKey.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServiceList.this, "ERROR.", Toast.LENGTH_SHORT).show();
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
        final Button editServiceNameButton = (Button) dialogView.findViewById(R.id.editServiceNameButton);
        final EditText editTextServiceNameOnList = (EditText) dialogView.findViewById(R.id.editTextServiceNameOnList);


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

        editServiceNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String newKey = editTextServiceNameOnList.getText().toString().trim();
                if (newKey.equals("")){
                    Toast.makeText(ServiceList.this, "Please enter a Service name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference currentRequirementName = firebaseDatabase.getReference("Services").child(serviceName);
                DatabaseReference newRequirementName = firebaseDatabase.getReference("Services").child(newKey);

                changeKey(currentRequirementName, newRequirementName);
                alert.dismiss();
            }
        });
    }



}