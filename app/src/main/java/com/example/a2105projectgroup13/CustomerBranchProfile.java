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

public class CustomerBranchProfile extends AppCompatActivity {
    TextView mondayWorkingHoursTextView;
    TextView tuesdayWorkingHoursTextView;
    TextView wednesdayWorkingHoursTextView;
    TextView thursdayWorkingHoursTextView;
    TextView fridayWorkingHoursTextView;
    TextView saturdayWorkingHoursTextView;
    TextView sundayWorkingHoursTextView;

    TextView showPhoneNumberTextView;
    TextView showAddressTextView;
    TextView showCityTextView;
    TextView showPostalCodeTextView;

    TextView uidBranchTextView;

    Button backToBranchListButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference offeredServicesReference;

    String branchUid;
    Intent previousScreen;

    ListView offeredServicesList;
    ArrayList<String> offeredServicesArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_branch_profile);

        initializeInstanceVariables();
        getProfileInfo();

        offeredServicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offeredServicesArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren()) {
                    final String key = service.getKey();
                    offeredServicesArrayList.add(key);
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerBranchProfile.this, android.R.layout.simple_list_item_1, offeredServicesArrayList);
                offeredServicesList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        backToBranchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent move = new Intent(CustomerBranchProfile.this, CustomerViewBranchList.class);
                startActivity(move);
            }
        });

        offeredServicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String viewOfferedService = offeredServicesArrayList.get(i);

                Intent moveToApply = new Intent(CustomerBranchProfile.this, CustomerApplyServiceRequests.class);
                moveToApply.putExtra("branchID", branchUid);
                moveToApply.putExtra("serviceName", viewOfferedService);
                startActivity(moveToApply);
            }
        });
    }

    /**
     * Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.

        //Display Profile Info
        showPhoneNumberTextView = findViewById(R.id.bPShowPhoneTextView);
        showAddressTextView = findViewById(R.id.bPShowStreetAddressTextView);
        showCityTextView = findViewById(R.id.bPShowCityTextView);;
        showPostalCodeTextView = findViewById(R.id.bPShowPostalCodeTextView);;

        //Display working hours
        mondayWorkingHoursTextView = findViewById(R.id.bPMondayWHTextView);
        tuesdayWorkingHoursTextView = findViewById(R.id.bPTuesdayWHTextView);
        wednesdayWorkingHoursTextView = findViewById(R.id.bPWednesdayWHTextView);
        thursdayWorkingHoursTextView = findViewById(R.id.bPThursdayWHTextView);
        fridayWorkingHoursTextView = findViewById(R.id.bPFridayWHTextView);
        saturdayWorkingHoursTextView = findViewById(R.id.bPSaturdayWHTextView);
        sundayWorkingHoursTextView = findViewById(R.id.bPSundayWHTextView);

        //Display BranchUid
        uidBranchTextView = findViewById(R.id.bPUidShowTextView);

        //Return to Branch List
        backToBranchListButton = findViewById(R.id.backToBranchListButton1);


        firebaseDatabase = firebaseDatabase.getInstance();

        previousScreen = getIntent();
        branchUid = previousScreen.getStringExtra("branchID");

        offeredServicesList = findViewById(R.id.bPOfferedServicesList);
        offeredServicesReference = firebaseDatabase.getReference("Offered Services").child(branchUid);
    }

    /**
     * Displays the profile information and Working Hours of the Branch in the TextViews on the activity screen.
     */
    private void getProfileInfo(){
        uidBranchTextView.setText(branchUid);

        DatabaseReference streetAddressReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Street Address");
        DatabaseReference cityReference = firebaseDatabase.getReference("User Info").child(branchUid).child("City");
        DatabaseReference postalCodeReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Postal Code");
        DatabaseReference phoneNumberReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Phone Number");

        streetAddressReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showAddressTextView.setText("No street address has been set for this Branch.");

                } else {
                    showAddressTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        cityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showCityTextView.setText("No city has been set for this Branch.");
                } else {
                    showCityTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        postalCodeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showPostalCodeTextView.setText("No postal code has been set for this Branch.");
                } else {
                    showPostalCodeTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        //Use dataSnapshot to set a TextView to display the user's account type.
        phoneNumberReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                if (value == null){
                    showPhoneNumberTextView.setText("No phone number has been set for this Branch.");
                } else {
                    showPhoneNumberTextView.setText(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        DatabaseReference mondayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Monday");
        DatabaseReference tuesdayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Tuesday");
        DatabaseReference wednesdayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Wednesday");
        DatabaseReference thursdayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Thursday");
        DatabaseReference fridayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Friday");
        DatabaseReference saturdayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Saturday");
        DatabaseReference sundayHoursReference = firebaseDatabase.getReference("User Info").child(branchUid).child("Working Hours").child("Sunday");

        mondayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    mondayWorkingHoursTextView.setText("Monday: CLOSED");
                } else {
                    mondayWorkingHoursTextView.setText("Monday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        tuesdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    tuesdayWorkingHoursTextView.setText("Tuesday: CLOSED");
                } else {
                    tuesdayWorkingHoursTextView.setText("Tuesday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        wednesdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    wednesdayWorkingHoursTextView.setText("Wednesday: CLOSED");
                } else {
                    wednesdayWorkingHoursTextView.setText("Wednesday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        thursdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    thursdayWorkingHoursTextView.setText("Thursday: CLOSED");
                } else {
                    thursdayWorkingHoursTextView.setText("Thursday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        fridayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    fridayWorkingHoursTextView.setText("Friday: CLOSED");
                } else {
                    fridayWorkingHoursTextView.setText("Friday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        saturdayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    saturdayWorkingHoursTextView.setText("Saturday: CLOSED");
                } else {
                    saturdayWorkingHoursTextView.setText("Saturday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        sundayHoursReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openTime = dataSnapshot.child("openTime").getValue(String.class);
                String closeTime = dataSnapshot.child("closeTime").getValue(String.class);

                if ( (openTime == null && closeTime == null) || (openTime == "CLOSED" && closeTime == "CLOSED")) {
                    sundayWorkingHoursTextView.setText("Sunday: CLOSED");
                } else {
                    sundayWorkingHoursTextView.setText("Sunday: " + openTime + " - " + closeTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CustomerBranchProfile.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }
}