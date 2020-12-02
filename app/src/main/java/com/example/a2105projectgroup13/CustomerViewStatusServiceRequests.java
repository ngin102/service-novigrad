package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * This class allows a Customer to view all of the Service Requests he or she has submitted.
 * From here, the Customer can view the status of each Service Request.
 */
public class CustomerViewStatusServiceRequests extends AppCompatActivity {

    ListView statusServiceRequestList;
    ArrayList<String> statusServiceRequestArrayList = new ArrayList<String>();

    Button statusReturnToWelcomeScreenButton;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference serviceRequestsReference;

    String customerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_status_service_requests);

        initializeInstanceVariables();

        // update displayed data when any data is changed
        serviceRequestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusServiceRequestArrayList.clear();
                //Look at each Branch's service requests.
                for (DataSnapshot branch : snapshot.getChildren()) {
                    String branchUid = branch.getKey();
                    //If the Branch has children...
                    if (branch.hasChildren()){
                        //Look at each service request made to the Branch.
                        for (DataSnapshot serviceRequest : branch.getChildren()){
                            if (serviceRequest.hasChild("CustomerID")){
                                if (serviceRequest.child("CustomerID").getValue(String.class).equals(customerUid)){
                                    if (serviceRequest.hasChild("Status") && serviceRequest.hasChild("Service")){
                                        String requestNumber = serviceRequest.getKey();
                                        String status = serviceRequest.child("Status").getValue(String.class);
                                        String serviceName = serviceRequest.child("Service").getValue(String.class);

                                        //Use ServiceRequest constructor to display the service request with all of its details stored in Firebase.
                                        ServiceRequest currentRequest = new ServiceRequest(requestNumber, status, branchUid, serviceName, customerUid);
                                        statusServiceRequestArrayList.add(currentRequest.toString());
                                    }
                                }
                            }
                        }
                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerViewStatusServiceRequests.this, android.R.layout.simple_list_item_1, statusServiceRequestArrayList);
                statusServiceRequestList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerViewStatusServiceRequests.this, "ERROR.", Toast.LENGTH_LONG).show();
            }

        });

        statusReturnToWelcomeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToWelcomeScreen = new Intent(CustomerViewStatusServiceRequests.this, CustomerWelcomeActivity.class);
                startActivity(moveToWelcomeScreen);
            }
        });
    }

    /**
     * Initializes all of the instance variables.
     */
    private void initializeInstanceVariables() {
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        //initialize ListView
        statusServiceRequestList = findViewById(R.id.statusServiceRequestList);

        //initialize Button
        statusReturnToWelcomeScreenButton = (Button)findViewById(R.id.statusReturnToWelcomeScreenButton);

        //getting the customer id
        FirebaseUser customerAccount = firebaseAuth.getInstance().getCurrentUser();
        customerUid = customerAccount.getUid();

        serviceRequestsReference = firebaseDatabase.getReference("Service Requests");
    }
}