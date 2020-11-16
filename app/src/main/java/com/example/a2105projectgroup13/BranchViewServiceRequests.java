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

public class BranchViewServiceRequests extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String uid;

    private ListView serviceList;
    private ArrayList<String> serviceArrayList = new ArrayList<String>();

    private Button returnToBranchWelcomeScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_service_requests);

        getUid(); // sets the variable uid to the branch ID

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Service Requests").child(uid);
        serviceList = (ListView) findViewById(R.id.serviceRequestListBranch);

        returnToBranchWelcomeScreenButton = (Button) findViewById(R.id.returnToBranchWelcomeScreenButton2);

        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren()) {
                    final String key = service.getKey();
                    serviceArrayList.add(key);
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(BranchViewServiceRequests.this, android.R.layout.simple_list_item_1, serviceArrayList);
                serviceList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchViewServiceRequests.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceToViewRequirements = serviceArrayList.get(i);
                Intent moveToView = new Intent(BranchViewServiceRequests.this, ActivityServiceRequest.class);
                moveToView.putExtra("serviceName", serviceToViewRequirements);
                startActivity(moveToView);
            }
        });


        returnToBranchWelcomeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(BranchViewServiceRequests.this, BranchWelcomeActivity.class);
                startActivity(move);
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