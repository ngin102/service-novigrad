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
 * This class displays a list of services to the Admin.
 */
public class AdminViewServiceList extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;

    private ListView serviceList;
    private ArrayList<String> serviceArrayList = new ArrayList<String>();

    private Button returnToManageServicesButton;
    private Button addServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Services");
        serviceList = (ListView) findViewById(R.id.serviceList);

        returnToManageServicesButton = (Button) findViewById(R.id.returnToManageServicesButton);

        addServiceButton = (Button) findViewById(R.id.addServiceButton);


        returnToManageServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AdminViewServiceList.this, AdminWelcomeActivity.class));
            }
        });

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AdminViewServiceList.this, AdminNewService.class));
            }
        });


        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren() ) {

                    final String key = service.getKey();

                    serviceInDatabase.child(key).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String price = dataSnapshot.getValue(String.class);
                            Service newService = new Service(key, price);
                            Admin admin = new Admin("Admin", "Admin", "Admin Account");
                            admin.addToServices(newService);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminViewServiceList.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });

                    serviceArrayList.add(key);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(AdminViewServiceList.this, android.R.layout.simple_list_item_1, serviceArrayList);
                serviceList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
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
                Intent moveToView = new Intent(AdminViewServiceList.this, AdminViewServiceRequirements.class);
                moveToView.putExtra("serviceName", serviceToViewRequirements);
                startActivity(moveToView);
            }
        });
    }


    private void changeKey(final DatabaseReference currentKey, final DatabaseReference newKey) {
        //Moving reference in Firebase.
        final String newKeyChecker = newKey.getKey();
        final DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services");


        firebaseDatabase.getReference("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot service : snapshot.getChildren()) {
                    String storedServiceName = service.getKey();

                    if (storedServiceName.toLowerCase().equals(newKeyChecker.toLowerCase())) {
                        Toast.makeText(AdminViewServiceList.this, "There is already a Service with this name. Please choose a new Service name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }


                serviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(newKeyChecker)) {
                            Toast.makeText(AdminViewServiceList.this, "There already exists a Service by this name. Choose another Service name.", Toast.LENGTH_LONG).show();
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
                                                Toast.makeText(AdminViewServiceList.this, "Changed Service name.", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            deleteService(currentKey.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_SHORT).show();
                    }
                });




                //Changing in Branches
                final String currentKeyChecker = currentKey.getKey();
                FirebaseDatabase.getInstance().getReference("Offered Services").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot uid : snapshot.getChildren()) {
                            String uidKey = uid.getKey();

                            if (uid.hasChild(currentKeyChecker)) {
                                Service editedService = new Service(newKeyChecker, "Not needed");
                                FirebaseDatabase.getInstance().getReference("Offered Services").child(uidKey).child(newKeyChecker).setValue(editedService);
                                FirebaseDatabase.getInstance().getReference("Offered Services").child(uidKey).child(currentKeyChecker).removeValue();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
                    }
                });

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });
    }


    //Adapted from deleteProduct() method from Lab 5
    private boolean deleteService(final String serviceName){
        //Getting the specified service reference
        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName);
        serviceReference.removeValue();
        Toast.makeText(AdminViewServiceList.this, "Service deleted.", Toast.LENGTH_LONG).show();

        FirebaseDatabase.getInstance().getReference("Offered Services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot uid : snapshot.getChildren()) {
                    String key = uid.getKey();

                    if (uid.hasChild(serviceName)) {
                        DatabaseReference uidRemoveService = FirebaseDatabase.getInstance().getReference("Offered Services").child(key).child(serviceName);
                        uidRemoveService.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


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
                    Toast.makeText(AdminViewServiceList.this, "Please enter a Service name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String validatedNewKey = ValidateString.validateServiceName(newKey);
                if (validatedNewKey.equals("-1")) {
                    Toast.makeText(AdminViewServiceList.this, "Invalid Service name. Make sure your Service name begins with a letter and is only alphanumeric. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    newKey = validatedNewKey;
                }

                DatabaseReference currentRequirementName = firebaseDatabase.getReference("Services").child(serviceName);
                DatabaseReference newRequirementName = firebaseDatabase.getReference("Services").child(newKey);

                changeKey(currentRequirementName, newRequirementName);
                alert.dismiss();
            }
        });
    }



}