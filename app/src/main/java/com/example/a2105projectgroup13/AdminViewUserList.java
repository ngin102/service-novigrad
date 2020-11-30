package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class allows the Admin to view and delete user accounts.
 */

public class AdminViewUserList extends AppCompatActivity {
    // instance variables
    private DatabaseReference usersInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private ListView usersList;
    private ArrayList<String> usersArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //variables
        firebaseDatabase = firebaseDatabase.getInstance();
        usersInDatabase = firebaseDatabase.getReference("Users");
        usersList = (ListView) findViewById(R.id.userList);

        //
        usersInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot user : snapshot.getChildren() ) {
                    String key = user.getKey();

                    if (!key.equals("htb18OBBKHQ3OmeBlg3qg8pzY2A3")) { //prevent the Admin from deleting themselves
                        usersArrayList.add(key);
                    }
                   // User userToAdd = user.getValue(User.class);
                   // usersArrayList.add(userToAdd.getFirstName() + " " + userToAdd.getLastName() + ": " + userToAdd.getAccountType());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(AdminViewUserList.this, android.R.layout.simple_list_item_1, usersArrayList);
                usersList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewUserList.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

        //Adapted from Lab 5
        usersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userToDelete = usersArrayList.get(i);
                showDeleteDialog(userToDelete);
                return true;
            }
        });
    }

    //Adapted from deleteProduct() method from Lab 5
    private boolean deleteUser(String uid){
        //Getting the specified User reference
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userReference.removeValue();
        Toast.makeText(AdminViewUserList.this, "User deleted.", Toast.LENGTH_LONG).show();
        return true;
    }

    //Adapted from showUpdateDeleteDialog() method from Lab 5
    private void showDeleteDialog(final String uid){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);
        final TextView firstNameDialog = (TextView) dialogView.findViewById(R.id.changingFirstNameText);
        final TextView lastNameDialog = (TextView) dialogView.findViewById(R.id.changingLastNameText);
        final TextView accountTypeDialog = (TextView) dialogView.findViewById(R.id.changingAccountTypeText);

        DatabaseReference firstName = firebaseDatabase.getReference("Users").child(uid).child("firstName");
        DatabaseReference lastName = firebaseDatabase.getReference("Users").child(uid).child("lastName");
        DatabaseReference accountType = firebaseDatabase.getReference("Users").child(uid).child("accountType");

        //Use dataSnapshot to set a TextView to display the user's first name.
        firstName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                firstNameDialog.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminViewUserList.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        //Use dataSnapshot to set a TextView to display the user's account type.
        lastName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                lastNameDialog.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminViewUserList.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        //Use dataSnapshot to set a TextView to display the user's account type.
        accountType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                accountTypeDialog.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminViewUserList.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });


        dialogBuilder.setTitle(uid);
        final AlertDialog alert = dialogBuilder.create();
        alert.show();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteUser(uid);
                alert.dismiss();
            }
        });
    }

}