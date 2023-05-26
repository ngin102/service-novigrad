package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

/**
 * This class allows a Customer to view reviews left by Customers for a certain Branch
 * or to submit a review for a Branch.
 */
public class CustomerReviewBranch extends AppCompatActivity {
    //instance variables
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reviewReference;

    ArrayList<String> reviewsArrayList = new ArrayList<String>();

    Intent previousScreen;
    String branchId;
    String customerId;

    ListView reviewList;

    EditText ratingEditText;
    EditText commentEditText;

    TextView reviewListInfo;

    Button submitReviewButton;
    Button backToBranchProfileButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_review_branch);

        initializeInstanceVariables();

        //reviews list
        reviewReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsArrayList.clear();
                for (DataSnapshot review : snapshot.getChildren()) {
                    Review selectedReview = review.getValue(Review.class);
                    reviewsArrayList.add(selectedReview.toString());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(CustomerReviewBranch.this, android.R.layout.simple_list_item_1, reviewsArrayList);
                reviewList.setAdapter(arrayAdapter);
            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerReviewBranch.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        backToBranchProfileButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
            Intent moveToProfile = new Intent(CustomerReviewBranch.this, CustomerBranchProfile.class);
            moveToProfile.putExtra("branchID", branchId);
            startActivity(moveToProfile);
            }
        });

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                submitReviewOnClick(view);
            }
        });
    }

    /**
     * Helper method for initializing instance variables.
     */
    private void initializeInstanceVariables() {
        //Initialize each instance variable by finding the first view that corresponds with its id.

        ratingEditText = findViewById(R.id.ratingEditText);
        commentEditText = findViewById(R.id.commentEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        backToBranchProfileButton2 = findViewById(R.id.backToBranchProfileButton2);

        reviewList = findViewById(R.id.reviewList);

        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //getting the customer id
        FirebaseUser customerAccount = firebaseAuth.getCurrentUser();
        customerId = customerAccount.getUid();

        previousScreen = getIntent();
        branchId = previousScreen.getStringExtra("branchID");

        reviewListInfo = findViewById(R.id.reviewListInfo);
        reviewListInfo.setText("A list of reviews for Branch " + branchId);

        reviewReference = firebaseDatabase.getReference("Reviews").child(branchId);
    }

    private void submitReviewOnClick(View v) {
        final String rating = ratingEditText.getText().toString().trim();
        final String comment = commentEditText.getText().toString().trim();

        // display and error if the form's name is empty
        if (rating.equals("")) {
            Toast.makeText(CustomerReviewBranch.this, "Please enter a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comment.equals("")) {
            Toast.makeText(CustomerReviewBranch.this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Integer.parseInt(rating) > 5 || Integer.parseInt(rating) < 0) {
            Toast.makeText(CustomerReviewBranch.this, "Please enter a rating between 0 to 5.", Toast.LENGTH_SHORT).show();
            return;

        }

        firebaseDatabase.getReference("Users").child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String firstName = dataSnapshot.child("firstName").getValue(String.class);
                final String lastName = dataSnapshot.child("lastName").getValue(String.class);

                firebaseDatabase.getReference("Users").child(branchId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String branchFirstName = dataSnapshot.child("firstName").getValue(String.class);
                        final String branchLastName = dataSnapshot.child("lastName").getValue(String.class);


                        firebaseDatabase.getReference("Reviews").child(branchId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int value = (int) dataSnapshot.getChildrenCount() + 1;

                                Customer customer = new Customer(firstName, lastName, "Customer Account");
                                Branch branch = new Branch(branchFirstName, branchLastName, "Branch Account");

                                Review review = new Review(comment, Integer.parseInt(rating), branch, customer);

                                firebaseDatabase.getReference("Reviews").child(branchId).child("Review " + Integer.toString(value)).setValue(review);

                            }

                            // display an error if there is a problem finding the service in the database
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(CustomerReviewBranch.this, "There was a problem submitting your review.", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    // display an error if there is a problem finding the service in the database
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CustomerReviewBranch.this, "There was a problem submitting your review.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // display an error if there is a problem finding the service in the database
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerReviewBranch.this, "There was a problem submitting your review.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}