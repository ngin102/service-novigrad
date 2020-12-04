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
     * First it gets the city (EditText) and address (EditText) and iterates through all of the branch user info.
     * It checks that the inputted city and address strings are contained in the branch user info.
     * If both are present in the stored info, the branch is displayed in branchList (ListView)
     *
     * It does not need to be an exact match. Capitalization does not matter.
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
     * First it gets the inputted time (EditText) and day (RadioGroup) and iterates through all of the branch user info.
     * At each branch user, it checks the working hours under the tab of the selected day. If the inputted
     * time is during the respective branch's working hours, then it is displayed on the branchList (ListView)
     */
    private void searchByHours() {
        String tmpTime = ValidateString.validateTime(timeEditText.getText().toString().trim());
        final int time;
        final String date = day;

        if (tmpTime.equals("-1")) { //if the inputted time is not in the proper format, give an error message and stop running the code
            Toast.makeText(CustomerBranchSearch.this, "TIME ENTERED IN AN INCORRECT FORMAT.", Toast.LENGTH_LONG).show();
            return;
        }

        time = Integer.parseInt(tmpTime.replace(":", ""));


        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchArrayList.clear();

                for (DataSnapshot branchUser : snapshot.getChildren()) {
                    boolean isAlreadyAdded = false;
                    try {
                       // Toast.makeText(CustomerBranchSearch.this,  Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Monday" + "/closeTime").getValue()).replace(":", "")), Toast.LENGTH_LONG).show();
                    //Checking for cases where the value of the close time will be lower than the open time.
                        //For example: the Branch's working hours on Monday are from 12:00 to 3:00 (so the Branch is open from Monday at 12:00 all the way straight through until Tuesday at 3:00)
                        //If the day to view working hours is Monday, we want to check if the Branch opens on Sunday but doesn't close until some time Monday (the Branch will technically be open during a period of time on Monday in this case).

                        if (isAlreadyAdded == false && day.equals("Monday") && ! branchUser.child("Working Hours/" + "Sunday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Sunday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Sunday" + "/closeTime").getValue()).replace(":", "")) //First checks if Sunday's close time is lower than its open time
                                < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Sunday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Sunday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Sunday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }

                        //If the day to view working hours is Tuesday, we want to check if the Branch opens on Monday but doesn't close until some time Tuesday (the Branch will technically be open during a period of time on Tuesday in this case).
                        if (isAlreadyAdded == false && day.equals("Tuesday") && ! branchUser.child("Working Hours/" + "Monday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Monday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Monday" + "/closeTime").getValue()).replace(":", "")) //First checks if Monday's close time is lower than its open time
                                    < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Monday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Monday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Monday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }

                        //If the day to view working hours is Wednesday, we want to check if the Branch opens on Tuesday but doesn't close until some time Wednesday (the Branch will technically be open during a period of time on Wednesday in this case).
                        if (isAlreadyAdded == false && day.equals("Wednesday") && ! branchUser.child("Working Hours/" + "Tuesday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Tuesday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Tuesday" + "/closeTime").getValue()).replace(":", "")) //First checks if Tuesday's close time is lower than its open time
                                    < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Tuesday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Tuesday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Tuesday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }


                        //If the day to view working hours is Thursday, we want to check if the Branch opens on Wednesday but doesn't close until some time Thursday (the Branch will technically be open during a period of time on Thursday in this case).
                        if (isAlreadyAdded == false && day.equals("Thursday") && ! branchUser.child("Working Hours/" + "Wednesday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Wednesday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Wednesday" + "/closeTime").getValue()).replace(":", "")) //First checks if Wednesday's close time is lower than its open time
                                    < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Wednesday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Wednesday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Wednesday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }


                        //If the day to view working hours is Friday, we want to check if the Branch opens on Thursday but doesn't close until some time Friday (the Branch will technically be open during a period of time on Friday in this case).
                        if (isAlreadyAdded == false && day.equals("Friday") && ! branchUser.child("Working Hours/" + "Thursday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Thursday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Thursday" + "/closeTime").getValue()).replace(":", "")) //First checks if Thursday's close time is lower than its open time
                                    < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Thursday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Thursday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Thursday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }


                        //If the day to view working hours is Saturday, we want to check if the Branch opens on Friday but doesn't close until some time Saturday (the Branch will technically be open during a period of time on Saturday in this case).
                        if (isAlreadyAdded == false && day.equals("Saturday") && ! branchUser.child("Working Hours/" + "Friday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Friday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Friday" + "/closeTime").getValue()).replace(":", "")) //First checks if Friday's close time is lower than its open time
                                    < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Friday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Friday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Friday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }


                        //If the day to view working hours is Sunday, we want to check if the Branch opens on Saturday but doesn't close until some time Sunday (the Branch will technically be open during a period of time on Sunday in this case).
                        if (isAlreadyAdded == false && day.equals("Sunday") && ! branchUser.child("Working Hours/" + "Saturday" + "/closeTime").getValue(String.class).equals("CLOSED")
                                && ! branchUser.child("Working Hours/" + "Saturday" + "/openTime").getValue(String.class).equals("CLOSED")){

                            if  (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Saturday" + "/closeTime").getValue()).replace(":", "")) //First checks if Saturday's close time is lower than its open time
                                    < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Saturday" + "/openTime").getValue()).replace(":", ""))) {

                                if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + "Saturday" + "/closeTime").getValue()).replace(":", "")) > time) { //Then checks if time entered is less than Saturday's close time
                                    branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                    isAlreadyAdded = true;
                                }
                            }
                        }


                        if (isAlreadyAdded == false && Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + day + "/closeTime").getValue()).replace(":", "")) //First checks if the close time is lower than the open time
                                < Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + day + "/openTime").getValue()).replace(":", ""))) {
                            if (Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + day + "/openTime").getValue()).replace(":", "")) < time) { //Then checks if time entered is greater than the open time
                                branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                                isAlreadyAdded = true;
                            }
                        }

                    //If the value of the close time is greater than the open time...
                        if (isAlreadyAdded == false && Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + day + "/closeTime").getValue()).replace(":", "")) > time //First checks if the time entered is before each branch's closing time
                                && Integer.parseInt(String.valueOf(branchUser.child("Working Hours/" + day + "/openTime").getValue()).replace(":", "")) <= time) { //Then checks if it is after or equal to the opening time
                            branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList
                            isAlreadyAdded = true;
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
     * First it gets the inputted service type contained in serviceSpinner, and then iterates over "Offered Services".
     * It checks each branch for the service and displays the branch on the branchList (ListView) if it offers the selected service.
     */
    private void searchByService() {
        offeredServicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String service = serviceSpinner.getSelectedItem().toString(); //sets the selected service to what is selected in the spinner
                branchArrayList.clear();

                for (DataSnapshot branchUser : snapshot.getChildren()) {
                    for (DataSnapshot offeredService : branchUser.getChildren()) {
                        try {
                            if (String.valueOf(offeredService.getKey()).equals(service)) { //Compares the service inputted to the service stored
                                branchArrayList.add(String.valueOf(branchUser.getKey())); //Finally it adds the branchId to branchArrayList if that branch contains the service
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