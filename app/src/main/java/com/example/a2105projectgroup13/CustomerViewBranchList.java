package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerViewBranchList extends AppCompatActivity {
    // instance variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersInDatabase;

    private ListView branchList;
    private ArrayList<String> branchArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_branch_list);

        initializeInstanceVariables();

        // update displayed data when any data is changed
        usersInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchArrayList.clear();
                for (DataSnapshot user : snapshot.getChildren()) {
                    if (user.child("accountType").getValue(String.class).equals("Branch Account")){
                        branchArrayList.add(user.getKey());
                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerViewBranchList.this, android.R.layout.simple_list_item_1, branchArrayList);
                branchList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerViewBranchList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }

        });

        branchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String branchToView = branchArrayList.get(i);

                Intent moveToCustomerBranchProfile = new Intent(CustomerViewBranchList.this, CustomerBranchProfile.class);
                moveToCustomerBranchProfile.putExtra("branchID", branchToView);
                startActivity(moveToCustomerBranchProfile);
            }
        });
    }

    /**
     * Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.

        firebaseDatabase = firebaseDatabase.getInstance();
        usersInDatabase = firebaseDatabase.getReference("Users");

        branchList = (ListView) findViewById(R.id.branchList);

    }

}