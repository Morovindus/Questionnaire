package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding_main = ActivityMainBinding.inflate(getLayoutInflater());
        FooterBinding binding_footer = FooterBinding.inflate(getLayoutInflater());
        View view = binding_main.getRoot();
        setContentView(view);

        Intent intent2 = getIntent();
        name = intent2.getStringExtra("name");

        Button createButton = binding_footer.createButton;

        surveys = new ArrayList<Surveys>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("surveys");

        BoxAdapter adapter = new BoxAdapter(MainActivity.this, surveys);

        ValueEventListener eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                surveys.clear();

                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    String dataClass = itemSnapshot.child("describe").getValue(String.class);
                    Integer check = 0;

                    ArrayList<String> users = new ArrayList<String>();

                    String creator = itemSnapshot.child("creator").getValue(String.class);

                    if (Objects.equals(name, creator)){
                        check = 1;
                    } else {
                        for (DataSnapshot postSnapshot : itemSnapshot.child("users").getChildren()) {
                            String post = postSnapshot.getValue(String.class);
                            users.add(post);
                        }

                        for (String w : users) {
                            if (Objects.equals(w, name)) {
                                check = 2;
                            }
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

        ListView lvMain = binding_main.lvMain;
        lvMain.addFooterView(binding_footer.getRoot());

        /*DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("moderators");
        eventListener = reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> moderators = new ArrayList<String>();

                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    String dataClass = itemSnapshot.getValue(String.class);

                    moderators.add(dataClass);

                    for (String w : moderators){
                        if (Objects.equals(w, name)){
                            lvMain.addFooterView(binding_footer.getRoot());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer checkQuiz = surveys.get(position).check;
                Intent intent;
                if (checkQuiz == 2 || checkQuiz == 1){
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
            intent = new Intent(MainActivity.this, AuthorInformation.class);
            intent.putExtra("name", name);
            startActivity(intent);
        } else if (id == R.id.item4)
        {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}