package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class allows a Customer to search for a Branch by address, by the services the Branch offers, or by the Branch's working hours.
 */
public class CustomerBranchSearch extends AppCompatActivity {
    private DatabaseReference offeredServicesRef, userInfoRef, serviceRef;
    private FirebaseDatabase firebaseDatabase;

    private LinearLayout addressOption, hoursOption, serviceOption;
    private ListView branchList;
    private EditText citySearch, addressSearch, timeEditText, serviceEditText;
    private RadioButton searchByAddressRB, searchByHoursRB, searchByServiceRB;
    private Spinner serviceSpinner;

    private String day;

    private ArrayList<String> branchArrayList;
    private ArrayList<String> serviceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search);

        initializeInstanceVariables();

        serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //serviceArrayList.clear();
                for (DataSnapshot service : snapshot.getChildren()) {
                    try {
                        serviceArrayList.add(String.valueOf(service.getKey())); //Finally it adds the branchId to branchArrayList
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //displays the serviceArrayList onto the spinner
                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerBranchSearch.this, android.R.layout.simple_spinner_item, serviceArrayList);
                serviceSpinner.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerBranchSearch.this, "ERROR. CANNOT ACCESS SERVICES IN DATABASE.", Toast.LENGTH_LONG).show();
            }
        });

        //When a branch is pressed, the user is moved to their branch profile
        branchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String branchToView = branchArrayList.get(i);

                Intent moveToCustomerBranchProfile = new Intent(CustomerBranchSearch.this, CustomerBranchProfile.class);
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
        userInfoRef = firebaseDatabase.getReference("User Info");
        serviceRef = firebaseDatabase.getReference("Services");

        branchList = (ListView) findViewById(R.id.branchList);
        branchArrayList = new ArrayList<String>();

        citySearch = (EditText) findViewById(R.id.citySearch);
        addressSearch = (EditText) findViewById(R.id.addressSearch);
        timeEditText = (EditText) findViewById(R.id.timeEditText);

        searchByAddressRB = (RadioButton) findViewById(R.id.searchByAddressRB);
        searchByHoursRB = (RadioButton) findViewById(R.id.searchByHoursRB);
        searchByServiceRB = (RadioButton) findViewById(R.id.searchByServiceRB);

        serviceSpinner = (Spinner) findViewById(R.id.serviceSpinner);
        serviceArrayList = new ArrayList<String>();


    }

    ///////Search functions

    /**
     * Is called by the search button in the activity.
     */
    private void searchByAddress() {
        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String city = citySearch.getText().toString().toUpperCase().trim(); //sets the city and address to what is in the editTexts
                final String address = addressSearch.getText().toString().toUpperCase().trim();
                branchArrayList.clear();

                for (DataSnapshot branchUser : snapshot.getChildren()) {
                    try {
                        if (String.valueOf(branchUser.child("City").getValue()).toUpperCase().contains(city) && //Compares the city inputted to the branch city location stored
                                String.valueOf(branchUser.child("Street Address").getValue()).toUpperCase().contains(address)) { //Compares the address inputted to the branch address stored
                            branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //displays the branchArrayList onto the ListView
                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerBranchSearch.this, android.R.layout.simple_list_item_1, branchArrayList);
                branchList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerBranchSearch.this, "ERROR. CANNOT ACCESS DATABASE.", Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * searches
     */
    private void searchByHours() {
        final String time = timeEditText.toString().trim().replace(":", "");
    }

    /**
     * searches
     */
    private void searchByService() {
        offeredServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String service = serviceSpinner.getSelectedItem().toString(); //sets the city and address to what is in the spinner
                branchArrayList.clear();

                for (DataSnapshot branchUser : snapshot.getChildren()) {
                    for (DataSnapshot offeredService : branchUser.getChildren()) {
                        try {
                            if (String.valueOf(offeredService.getKey()).equals(service)) { //Compares the address inputted to the branch address stored
                                branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //displays the branchArrayList onto the ListView
                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerBranchSearch.this, android.R.layout.simple_list_item_1, branchArrayList);
                branchList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerBranchSearch.this, "ERROR. CANNOT ACCESS DATABASE.", Toast.LENGTH_LONG).show();
            }

        });
    }

    //////RadioButton onClick functions

    /**
     * Is called when a radioButton is clicked.
     * The selected search option will be the only visible usable input for the user
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.searchByAddressRB://searchByAddress is available for the user's search
                if (checked)
                    addressOption.setVisibility(View.VISIBLE);
                hoursOption.setVisibility(View.GONE);
                serviceOption.setVisibility(View.GONE);
                break;
            case R.id.searchByHoursRB://searchByHours is available for the user's search
                if (checked)
                    addressOption.setVisibility(View.GONE);
                hoursOption.setVisibility(View.VISIBLE);
                serviceOption.setVisibility(View.GONE);

                break;
            case R.id.searchByServiceRB://searchByService is available for the user's search
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
    public void setDayOnClick(View view) {
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

    public void onSearchButtonClicked(View view) {
        if (searchByAddressRB.isChecked()) {
            searchByAddress();
        } else if (searchByHoursRB.isChecked()) {
            searchByHours();
        } else if (searchByServiceRB.isChecked()) {
            searchByService();
        }
        /*
        offeredServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchArrayList.clear();
                for (DataSnapshot branchOfferingService : snapshot.getChildren()) {
                    if (searchByAddressRB.isChecked()) { //Chooses the appropriate search option based off of which radioButton is pressed
                        searchByAddress(branchOfferingService.getKey().toString());
                    } else if (searchByHoursRB.isChecked()) {
                        searchByHours(branchOfferingService);
                    } else if (searchByServiceRB.isChecked()) {
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

         */
    }

    public void onBackButtonClicked(View view) {
        Intent moveToCustomerWelcomeActivity = new Intent(CustomerBranchSearch.this, CustomerWelcomeActivity.class);
        startActivity(moveToCustomerWelcomeActivity);
    }

}