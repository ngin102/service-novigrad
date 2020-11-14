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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class displays a list of services created by the Admin to a Branch
 */
public class BranchViewOfferedServiceList extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String uid;

    private ListView serviceList;
    private ArrayList<String> serviceArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_offered_service_list);

        getUid();

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Offered Services").child(uid);
        serviceList = (ListView) findViewById(R.id.offeredServiceListBranch);


        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren()) {
                    final String key = service.getKey();
                    serviceArrayList.add(key);

                    ArrayAdapter arrayAdapter = new ArrayAdapter(BranchViewOfferedServiceList.this, android.R.layout.simple_list_item_1, serviceArrayList);
                    serviceList.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchViewOfferedServiceList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        //Adapted from Lab 5
        serviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToRemove = serviceArrayList.get(i);
                showRemoveDialog(serviceToRemove);
                return true;
            }
        });

        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToViewRequirements = serviceArrayList.get(i);
                Intent moveToView = new Intent(BranchViewOfferedServiceList.this, ViewServiceRequirements.class);
                moveToView.putExtra("serviceName", serviceToViewRequirements);
                startActivity(moveToView);
            }
        });
    }

    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showRemoveDialog(final String serviceName){
        getUid();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remove_offeredservice_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button stopOfferingButton = (Button) dialogView.findViewById(R.id.stopOfferingButton);


        dialogBuilder.setTitle(serviceName);
        final AlertDialog alert = dialogBuilder.create();
        alert.show();

        stopOfferingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                stopOfferingService(serviceName);
                Toast.makeText(BranchViewOfferedServiceList.this, "You have stopped offering this Service.", Toast.LENGTH_LONG).show();

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


    //Adapted from deleteProduct() method from Lab 5
    private boolean stopOfferingService(String serviceName){
        DatabaseReference serviceReference = firebaseDatabase.getReference("Offered Services").child(uid).child(serviceName);
        serviceReference.removeValue();
        return true;
    }
}