package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.questionnaire.databinding.ActivityEndPoolBinding;

public class EndPool extends AppCompatActivity {
    private ActivityEndPoolBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEndPoolBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button buttonResult = binding.buttonResult;

        Intent intent2 = getIntent();
        String describe = intent2.getStringExtra("describe");
        String name = intent2.getStringExtra("name");

        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndPool.this, ShowResult.class);
                intent.putExtra("describe", describe);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}