package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BranchSearch extends AppCompatActivity {
    private DatabaseReference offeredServicesRef, userInfoRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private LinearLayout addressOption, hoursOption, serviceOption;
    private ListView branchList;
    private EditText citySearch, addressSearch, timeEditText, serviceEditText;
    private Button backButton, searchButton;

    private String day;

    private ArrayList<String> branchArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search);

        initializeInstanceVariables();

        //TODO: implement eventHandler for each search type
        offeredServicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchArrayList.clear();
                for (DataSnapshot branchOfferingService : snapshot.getChildren()) {
                    if (addressOption.isPressed()) { //Chooses the appropriate search option based off of which radioButton is pressed
                        searchByAddress(branchOfferingService.getKey());
                    } else if (hoursOption.isPressed()) {
                        searchByHours(branchOfferingService);
                    } else if (serviceOption.isPressed()) {
                        searchByService(serviceEditText.toString(), branchOfferingService);
                    }
                }

                //displays the branchArrayList onto the ListView
                ArrayAdapter arrayAdapter = new ArrayAdapter(BranchSearch.this, android.R.layout.simple_list_item_1, branchArrayList);
                branchList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchSearch.this, "ERROR. CANNOT ACCESS DATABASE.", Toast.LENGTH_LONG).show();
            }

        });

        //When a branch is pressed, the user is moved to their branch profile
        branchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String branchToView = branchArrayList.get(i);

                Intent moveToCustomerBranchProfile = new Intent(BranchSearch.this, CustomerBranchProfile.class);
                moveToCustomerBranchProfile.putExtra("branchID", branchToView);
                startActivity(moveToCustomerBranchProfile);
            }
        });


    }

    private void initializeInstanceVariables() {
        addressOption = (LinearLayout) findViewById(R.id.addressOption);
        hoursOption = (LinearLayout) findViewById(R.id.hoursOption);
        serviceOption = (LinearLayout) findViewById(R.id.serviceOption);

        firebaseDatabase = firebaseDatabase.getInstance();
        offeredServicesRef = firebaseDatabase.getReference("Offered Services");

        branchList = (ListView) findViewById(R.id.branchList);

        citySearch = (EditText) findViewById(R.id.citySearch);
        addressSearch = (EditText) findViewById(R.id.addressSearch);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        serviceEditText = (EditText) findViewById(R.id.serviceEditText);

        backButton = (Button) findViewById(R.id.backButton);
    }

    ///////Search functions

    /**
     * Is called when either the data changes in the database or is called by the search button in
     * the activity. It is always called within a loop, so it only looks at one
     *
     *
     */
    private void searchByAddress(String branchId) {
        final String city = citySearch.toString().toUpperCase().trim(); //sets the city and address to what is in the editTexts
        final String address = addressSearch.toString().toUpperCase().trim();

        userInfoRef = firebaseDatabase.getReference("User Info/" + branchId); //Gets the reference to the selected branch user's info

        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //reads a data snapshot
                if (snapshot.child("City").getValue().toString().toUpperCase().contains(city) && //Compares the city inputted to the branch city location stored
                        snapshot.child("Street Address").getValue().toString().toUpperCase().contains(address)) { //Compares the address inputted to the branch address stored
                    branchArrayList.add(snapshot.getValue().toString()); //Finally it adds the branchId to branchArrayList
                }
            }

            // display error if there is trouble accessing the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchSearch.this, "ERROR. CANNOT ACCESS DATABASE.", Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * searches
     */
    private void searchByHours(DataSnapshot snapshot) {
        final String time = timeEditText.toString().trim().replace(":","");
    }

    /**
     * searches
     */
    private void searchByService(String service, DataSnapshot snapshot) {

    }

    //////RadioButton onClick functions

    /**
     * Is called when a radioButton is clicked.
     * The selected search option will be the only visible usable input for the user
     */
    private void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.addressOption://searchByAddress is available for the user's search
                if (checked)
                    addressOption.setVisibility(View.VISIBLE);
                hoursOption.setVisibility(View.GONE);
                serviceOption.setVisibility(View.GONE);
                break;
            case R.id.hoursOption://searchByHours is available for the user's search
                if (checked)
                    addressOption.setVisibility(View.GONE);
                hoursOption.setVisibility(View.VISIBLE);
                serviceOption.setVisibility(View.GONE);

                break;
            case R.id.serviceOption://searchByService is available for the user's search
                if (checked)
                    addressOption.setVisibility(View.GONE);
                hoursOption.setVisibility(View.GONE);
                serviceOption.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Sets the day when a day radioButton is pressed. The day information is required for searchByHours(String time)
     */
    private void setDayOnClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.monButton://searchByAddress is available for the user's search
                if (checked)
                    day = "Monday";
                break;
            case R.id.tuesButton://searchByHours is available for the user's search
                if (checked)
                    day = "Tuesday";
                break;
            case R.id.wedButton://searchByService is available for the user's search
                if (checked)
                    day = "Wednesday";
                break;
            case R.id.thurButton://searchByAddress is available for the user's search
                if (checked)
                    day = "Thursday";
                break;
            case R.id.friButton://searchByHours is available for the user's search
                if (checked)
                    day = "Friday";
                break;
            case R.id.satButton://searchByService is available for the user's search
                if (checked)
                    day = "Saturday";
                break;
            case R.id.sunButton://searchByService is available for the user's search
                if (checked)
                    day = "Sunday";
                break;
        }
    }

    ///////Button onClick functions

    private void onSearchButtonClicked(View view) {
        offeredServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchArrayList.clear();
                for (DataSnapshot branchOfferingService : snapshot.getChildren()) {
                    if (addressOption.isPressed()) { //Chooses the appropriate search option based off of which radioButton is pressed
                        searchByAddress(branchOfferingService.getKey());
                    } else if (hoursOption.isPressed()) {
                        searchByHours(branchOfferingService);
                    } else if (serviceOption.isPressed()) {
                        searchByService(serviceEditText.toString(), branchOfferingService);
                    }
                }

                //displays the branchArrayList onto the ListView
                ArrayAdapter arrayAdapter = new ArrayAdapter(BranchSearch.this, android.R.layout.simple_list_item_1, branchArrayList);
                branchList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchSearch.this, "ERROR. CANNOT ACCESS DATABASE.", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void onBackButtonClicked(View view) {
        Intent moveToCustomerWelcomeActivity = new Intent(BranchSearch.this, CustomerWelcomeActivity.class);
        startActivity(moveToCustomerWelcomeActivity);
    }

}