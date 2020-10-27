package com.example.a2105projectgroup13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NewForm extends AppCompatActivity {

    LinearLayout fieldList;
    Button addFieldButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_form);

        fieldList = findViewById(R.id.fieldList);
        addFieldButton = findViewById(R.id.addFieldButton);

        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                addFieldToList();
            }
        });

    }

    private void addFieldToList(){
        LayoutInflater inflater = getLayoutInflater();
        final View field = inflater.inflate(R.layout.add_field, null);

        EditText editTextFieldName = (EditText) field.findViewById(R.id.editTextFieldName);
        Button removeFieldButton = (Button) field.findViewById(R.id.removeFieldButton);

        removeFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                fieldList.removeView(field);
            }
        });

        fieldList.addView(field);
    }

}