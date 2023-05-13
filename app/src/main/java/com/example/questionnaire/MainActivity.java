package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.questionnaire.databinding.ActivityMainBinding;
import com.example.questionnaire.databinding.FooterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ArrayList<Surveys> surveys;
    ListView lvMain;
    DatabaseReference reference;
    ValueEventListener eventListener;
    String name;
    private static ActivityMainBinding binding_main;
    private static FooterBinding binding_footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_main = ActivityMainBinding.inflate(getLayoutInflater());
        binding_footer = FooterBinding.inflate(getLayoutInflater());
        View view = binding_main.getRoot();
        setContentView(view);

        Intent intent2 = getIntent();
        name = intent2.getStringExtra("name");

        Button createButton = binding_footer.createButton;

        surveys = new ArrayList<Surveys>();

        reference = FirebaseDatabase.getInstance().getReference("surveys");

        BoxAdapter adapter = new BoxAdapter(MainActivity.this, surveys);

        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                surveys.clear();

                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    String dataClass = itemSnapshot.child("describe").getValue(String.class);
                    boolean check = false;

                    ArrayList<String> users = new ArrayList<String>();

                    for (DataSnapshot postSnapshot: itemSnapshot.child("users").getChildren()) {
                        String post = postSnapshot.getValue(String.class);
                        users.add(post);
                    }

                    for (String w : users){
                        if (Objects.equals(w, name)){
                            check = true;
                        }
                    }

                    Surveys help = new Surveys(dataClass, check);
                    surveys.add(help);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lvMain = binding_main.lvMain;
        lvMain.addFooterView(binding_footer.getRoot());
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean checkQuiz = surveys.get(position).check;
                Intent intent;
                if (checkQuiz){
                    intent = new Intent(MainActivity.this, EndPool.class);
                } else {
                    intent = new Intent(MainActivity.this, DetailActivity.class);
                }
                intent.putExtra("name", name);
                intent.putExtra("describe", surveys.get(position).describe);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        int id = item.getItemId();
        if (id == R.id.item1)
        {
            intent = new Intent(MainActivity.this, Instruction.class);
            intent.putExtra("name", name);
            startActivity(intent);
        } else if (id == R.id.item2)
        {
            intent = new Intent(MainActivity.this, ReferenceInformation.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }  else if (id == R.id.item3)
        {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}