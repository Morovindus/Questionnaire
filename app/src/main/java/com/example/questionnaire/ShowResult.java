package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.questionnaire.databinding.ActivityLoginBinding;
import com.example.questionnaire.databinding.ActivityShowResultBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowResult extends AppCompatActivity {
    private ActivityShowResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();
        String describe = intent2.getStringExtra("describe");
        String name = intent2.getStringExtra("name");

        Integer countQuestion = 1;
        String question = "Question №" + countQuestion.toString();
        ArrayList<PieEntry> answers = new ArrayList<>();

        Log.d("myLogs", "Start");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("surveys").child(describe).child(question);

        Log.d("myLogs", "Request");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String _question = dataSnapshot.child("describe").getValue(String.class);
                PieChart pieChart = binding.pieCharts;
                answers.clear();

                for (DataSnapshot itemSnapshot: dataSnapshot.child("Answers").getChildren()){
                    String answer = itemSnapshot.child("answer").getValue(String.class);
                    Integer quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    if (quantity != 0)
                        answers.add(new PieEntry(quantity, answer));

                }

                Log.d("myLogs", "Read");

                PieDataSet pieDataSet = new PieDataSet(answers, "Опрос");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText(_question);
                pieChart.animate();

                Log.d("myLogs", "End");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResult.this, MainActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}