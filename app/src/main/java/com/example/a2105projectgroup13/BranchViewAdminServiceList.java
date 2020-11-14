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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

/**
 * This class displays a list of services created by the Admin to a Branch
 */
public class BranchViewAdminServiceList extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String uid;

    private ListView serviceList;
    private ArrayList<String> serviceArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_admin_service_list);

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Services");
        serviceList = (ListView) findViewById(R.id.adminServiceListBranch);


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
                            Toast.makeText(BranchViewAdminServiceList.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });

                    serviceArrayList.add(key);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(BranchViewAdminServiceList.this, android.R.layout.simple_list_item_1, serviceArrayList);
                serviceList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchViewAdminServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        //Adapted from Lab 5
        serviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToAdd = serviceArrayList.get(i);
                showAddDialog(serviceToAdd);
                return true;
            }
        });

        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToViewRequirements = serviceArrayList.get(i);
                Intent moveToView = new Intent(BranchViewAdminServiceList.this, ViewServiceRequirements.class);
                moveToView.putExtra("serviceName", serviceToViewRequirements);
                startActivity(moveToView);
            }
        });
    }

    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showAddDialog(final String serviceName){
        getUid();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button offerServiceButton = (Button) dialogView.findViewById(R.id.offerServiceButton);


        dialogBuilder.setTitle(serviceName);
        final AlertDialog alert = dialogBuilder.create();
        alert.show();

        final Service linkedService = new Service(serviceName, "Not needed");

        offerServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                firebaseDatabase.getReference("Offered Services").child(uid).child(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(BranchViewAdminServiceList.this, "You are already offering this Service!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            firebaseDatabase.getReference("Offered Services").child(uid).child(serviceName).setValue(linkedService).addOnCompleteListener(BranchViewAdminServiceList.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Since the user's information has been successfully stored in Firebase Database, the registration process is completed!
                                        Toast.makeText(BranchViewAdminServiceList.this, "Service is now being offered to Customers", Toast.LENGTH_SHORT).show();

                                    } else {
                                        //If the user's information was not successfully stored in Firebase Database, give the user this message prompt.
                                        Toast.makeText(BranchViewAdminServiceList.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(BranchViewAdminServiceList.this, "There was a problem offering this Service.", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.dismiss();
            }
        });

    }

    /**
     * Gets (but does not return) the unique user uId of the user who
     * is currently logged into the app via Firebase Authentication.
     */
    private void getUid(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }
}