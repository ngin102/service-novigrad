package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OpenDocument extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private Intent previousScreen;
    private String requestKey;
    private String branchId;
    private String documentToDownload;
    private ImageView documentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_document);

        initializeInstanceVariables();

        //Adapted from the official Firebase webpage on downloading files from Firebase Storage to Android devices:
            //https://firebase.google.com/docs/storage/android/download-files#java_3
        firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Documents").child(documentToDownload).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String filename = snapshot.getValue(String.class);

                StorageReference referenceToDocument = firebaseStorage.getReference().child(branchId + "/" + requestKey + "/" + filename);
                Toast.makeText(OpenDocument.this, "Loading " + filename + "...", Toast.LENGTH_LONG).show();

                referenceToDocument.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(OpenDocument.this)
                                .load(uri)
                                .into(documentImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OpenDocument.this, "ERROR.", Toast.LENGTH_LONG).show();
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
        firebaseStorage = firebaseStorage.getInstance();

        //getting the branch id
        FirebaseUser branchAccount = firebaseAuth.getInstance().getCurrentUser();
        branchId = branchAccount.getUid();


        //initialize ImageView
        documentImageView = (ImageView) findViewById(R.id.documentImageView);

        //get intent
        previousScreen = getIntent();
        documentToDownload = previousScreen.getStringExtra("documentToDownload");
        requestKey = previousScreen.getStringExtra("requestKey");
    }
}