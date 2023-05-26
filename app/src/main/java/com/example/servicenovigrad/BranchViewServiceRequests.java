package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
 * This class allows a Branch to view the Service Requests it has received from Customers.
 */
public class BranchViewServiceRequests extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String uid;

    private ListView serviceRequestList;
    private ArrayList<String> serviceRequestArrayList = new ArrayList<String>();
    private ArrayList<String> serviceRequestWithStatusArrayList = new ArrayList<String>();

    private Button returnToBranchWelcomeScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_service_requests);

        getUid(); // sets the variable uid to the branch ID

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Service Requests").child(uid);
        serviceRequestList = (ListView) findViewById(R.id.serviceRequestListBranch);

        returnToBranchWelcomeScreenButton = (Button) findViewById(R.id.returnToBranchWelcomeScreenButton2);

        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceRequestArrayList.clear();
                serviceRequestWithStatusArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren()) {
                    final String key = service.getKey();
                    serviceRequestArrayList.add(key);

                    serviceInDatabase.child(key).child("Status").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String status = dataSnapshot.getValue(String.class);
                            serviceRequestWithStatusArrayList.add(key + ": " + status);

                            ArrayAdapter arrayAdapter = new ArrayAdapter(BranchViewServiceRequests.this, android.R.layout.simple_list_item_1, serviceRequestWithStatusArrayList);
                            serviceRequestList.setAdapter(arrayAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(BranchViewServiceRequests.this, "ERROR", Toast.LENGTH_SHORT).show();;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchViewServiceRequests.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        serviceRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String requestToView = serviceRequestArrayList.get(i);
                Intent moveToNextScreen = new Intent(BranchViewServiceRequests.this, BranchApproveDenyServiceRequests.class);
                moveToNextScreen.putExtra("requestKey", requestToView);
                startActivity(moveToNextScreen);
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