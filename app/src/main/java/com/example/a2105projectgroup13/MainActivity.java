package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class is the main activity of the Service Novigrad app.
 * It is the Welcome Screen.
 */
public class MainActivity extends AppCompatActivity {
    //Variables required to retrieve information from Firebase Authentication and Firebase Database:
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    //Text that appears on screen:
    private TextView firstNameText;
    private TextView accountTypeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameText = (TextView) findViewById(R.id.firstNameText);
        accountTypeText = (TextView) findViewById(R.id.accountTypeText);

        //The unique user uId of the user who is currently logged in to the app.
        getUid();

        //Get the references to the user's first name and account type in Firebase Database based on the user's unique uId.
        DatabaseReference firstName = firebaseDatabase.getReference("Users").child(uid).child("firstName");
        DatabaseReference accountType = firebaseDatabase.getReference("Users").child(uid).child("accountType");

        //Use dataSnapshot to set a TextView to display the user's first name.
        firstName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                firstNameText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        //Use dataSnapshot to set a TextView to display the user's account type.
        accountType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                accountTypeText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();;
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