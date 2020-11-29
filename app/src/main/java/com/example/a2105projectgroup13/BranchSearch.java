package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BranchSearch extends AppCompatActivity {
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    LinearLayout addressOption, hoursOption, serviceOption;
    ListView branchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search);
    }

    private void initializeInstanceVariables() {
        addressOption = (LinearLayout)findViewById(R.id.addressOption);
        hoursOption = (LinearLayout)findViewById(R.id.hoursOption);
        serviceOption = (LinearLayout)findViewById(R.id.serviceOption);

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Offered Services");
        branchList = (ListView) findViewById(R.id.branchList);
    }

    private void searchByAddress(String address) {

    }

    /**
     * Is called when a radioButton is clicked.
     * The selected search option will be the only visible usable input for the user
     */
    private void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
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


}