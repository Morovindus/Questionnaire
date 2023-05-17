package com.example.questionnaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.Objects;

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


        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("moderators");
        ValueEventListener eventListener = reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> moderators = new ArrayList<String>();
                Boolean flag = false;

                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    String dataClass = itemSnapshot.getValue(String.class);

                    moderators.add(dataClass);

                    for (String w : moderators){
                        if (Objects.equals(w, name)){
                            flag = true;
                            binding.buttonClose.setEnabled(true);
                        }
                    }
                    if (!flag){
                        binding.buttonClose.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowResult.this, R.style.AlertDialog);

                builder
                        .setMessage("Вы уверены что хотите безвозвратно удалить данный опрос?\n")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance()
                                        .getReference("surveys")
                                        .child(describe).removeValue();
                                Toast.makeText(ShowResult.this, "Успешно удалено", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ShowResult.this, MainActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);

                                finish();
                            }
                        })

                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}