package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.questionnaire.databinding.ActivityReferenceInformationBinding;

public class ReferenceInformation extends AppCompatActivity {

    private static ActivityReferenceInformationBinding binding_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_info = ActivityReferenceInformationBinding.inflate(getLayoutInflater());
        View view = binding_info.getRoot();
        setContentView(view);
        Button buttonBack = binding_info.backButton;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();
        String name = intent2.getStringExtra("name");

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferenceInformation.this, MainActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}