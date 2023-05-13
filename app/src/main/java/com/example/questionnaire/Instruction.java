package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.questionnaire.databinding.ActivityInstructionBinding;

public class Instruction extends AppCompatActivity {

    private static ActivityInstructionBinding binding_help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_help = ActivityInstructionBinding.inflate(getLayoutInflater());
        View view = binding_help.getRoot();
        setContentView(view);
        Button buttonBack = binding_help.backButton;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();
        String name = intent2.getStringExtra("name");

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Instruction.this, MainActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}