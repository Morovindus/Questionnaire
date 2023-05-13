package com.example.questionnaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.questionnaire.databinding.ActivityFinalBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FinalActivity extends AppCompatActivity {
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFinalBinding binding_final = ActivityFinalBinding.inflate(getLayoutInflater());
        View view = binding_final.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();
        name = intent2.getStringExtra("name");
        String describe = intent2.getStringExtra("describe");

        binding_final.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference2 = database.getReference("surveys")
                        .child(describe)
                        .child("users");

                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> users = new ArrayList<String>();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            String post = postSnapshot.getValue(String.class);
                            users.add(post);
                        }
                        users.add(name);
                        reference2.setValue(users);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Intent intent = new Intent(FinalActivity.this, MainActivity.class);
                //startActivity(intent);

                startActivityAfterCleanup(MainActivity.class);
            }
        });
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}