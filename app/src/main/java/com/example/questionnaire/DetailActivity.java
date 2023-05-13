package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.questionnaire.databinding.ActivityDetailBinding;
public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_detail = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding_detail.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView detailDesc = binding_detail.detailDesc;
        Button buttonEnter = binding_detail.buttonEnter;
        Button buttonBack = binding_detail.buttonBack;

        Intent intent2 = getIntent();
        String describe = intent2.getStringExtra("describe");
        String name = intent2.getStringExtra("name");

        detailDesc.setText(describe);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, AnswersActivity.class);
                intent.putExtra("describe", describe);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }
}