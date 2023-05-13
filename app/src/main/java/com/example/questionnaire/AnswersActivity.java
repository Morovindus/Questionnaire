package com.example.questionnaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.questionnaire.databinding.ActivityAnswersBinding;
import com.example.questionnaire.databinding.FooterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnswersActivity extends AppCompatActivity {
    ArrayList<Answer> answers;
    ListView lvMain;
    FirebaseDatabase database;
    BoxAdapterAnswer adapter;
    String describe, question, name;
    private ActivityAnswersBinding binding_answers;
    private static FooterBinding binding_footer;
    Integer countQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_answers = ActivityAnswersBinding.inflate(getLayoutInflater());
        binding_footer = FooterBinding.inflate(getLayoutInflater());
        View view = binding_answers.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button buttonNew = binding_footer.createButton;
        buttonNew.setText("Следующий вопрос");

        Intent intent2 = getIntent();
        describe = intent2.getStringExtra("describe");
        name = intent2.getStringExtra("name");

        answers = new ArrayList<Answer>();
        adapter = new BoxAdapterAnswer(AnswersActivity.this, answers);


        countQuestion = 1;
        question = "Question №" + countQuestion.toString();

        checkQuestion(question);
        setQuestion(question);
        setAnswers(question);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> ready_answers = new ArrayList<Integer>();
                for (Answer p : answers) {
                    if (p.box)
                        ready_answers.add(1);
                    else
                        ready_answers.add(0);
                }

                for (Integer i = 1;i <= ready_answers.size();i++){
                    if (ready_answers.get(i-1) == 1){

                        database = FirebaseDatabase.getInstance();
                        DatabaseReference reference2 = database.getReference("surveys")
                                .child(describe)
                                .child(question)
                                .child("Answers")
                                .child("Answer №" + i.toString())
                                .child("quantity");

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer count = snapshot.getValue(Integer.class);
                                reference2.setValue(count+1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                countQuestion++;
                question = "Question №" + countQuestion.toString();
                checkQuestion(question);
            }
        });
    }

    public void checkQuestion(String _question){
        FirebaseDatabase _database = FirebaseDatabase.getInstance();

        Query checkUserDatabase = _database.getReference("surveys").child(describe).child(_question).child("describe");
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setQuestion(_question);
                    setAnswers(_question);
                } else {
                    Intent intent = new Intent(AnswersActivity.this, FinalActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("describe", describe);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setQuestion(String _question){
        FirebaseDatabase _database = FirebaseDatabase.getInstance();
        DatabaseReference _reference = _database.getReference("surveys")
                .child(describe)
                .child(_question)
                .child("describe");

        _reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post = dataSnapshot.getValue(String.class);
                binding_answers.question.setText(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAnswers(String _question){
        DatabaseReference _reference = FirebaseDatabase.getInstance().getReference("surveys").child(describe).child(_question).child("Answers");

        _reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                answers.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    String dataClass = itemSnapshot.child("answer").getValue(String.class);
                    Answer helper = new Answer(dataClass, false);
                    answers.add(helper);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        lvMain = binding_answers.lvMain;
        lvMain.addFooterView(binding_footer.getRoot());
        lvMain.setAdapter(adapter);
    }
}