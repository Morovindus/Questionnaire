package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import java.util.List;

public class ShowResult extends AppCompatActivity {
    private ActivityShowResultBinding binding;
    private List<View> allEds;
    Integer countQuestion = 1;
    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final LinearLayout linear = binding.linear;

        Intent intent2 = getIntent();
        String describe = intent2.getStringExtra("describe");
        String name = intent2.getStringExtra("name");

        question = "Question №" + countQuestion.toString();

        allEds = new ArrayList<View>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("surveys").child(describe);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                while (true){

                    ArrayList<PieEntry> answers = new ArrayList<>();

                    String _question = dataSnapshot.child(question).child("describe").getValue(String.class);
                    try {
                        Log.d("myLogs", _question);
                    } catch (Throwable t){
                        return;
                    }

                    final View view2 = getLayoutInflater().inflate(R.layout.circle, null);

                    linear.addView(view2);
                    allEds.add(view2);

                    PieChart pieChart = (((PieChart) allEds.get(countQuestion - 1).findViewById(R.id.pieCharts)));

                        answers.clear();

                        for (DataSnapshot itemSnapshot : dataSnapshot.child(question).child("Answers").getChildren()) {
                            String answer = itemSnapshot.child("answer").getValue(String.class);
                            Integer quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                            if (quantity != 0)
                                answers.add(new PieEntry(quantity, answer));

                        }

                        PieDataSet pieDataSet = new PieDataSet(answers, "Опрос");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieDataSet.setValueTextColor(Color.BLACK);
                        pieDataSet.setValueTextSize(16f);

                        PieData pieData = new PieData(pieDataSet);

                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.setCenterText(_question);
                        pieChart.animate();

                        countQuestion++;
                        question = "Question №" + countQuestion.toString();
                    }
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