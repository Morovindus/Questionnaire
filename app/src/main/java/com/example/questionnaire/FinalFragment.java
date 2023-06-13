package com.example.questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentFinalBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Фрагмент, который уведомляет пользователя о завершении опроса
public class FinalFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FragmentFinalBinding binding = FragmentFinalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> answers = new ArrayList<>();

        String nameUser = ((MainActivity)getActivity()).name;
        String describe = "";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
            answers = bundle.getStringArrayList("answers");
        }

        Integer countQuestion = 1;

        for (String _answer : answers) {

            for (Integer i = 1; i <= _answer.length(); i++) {
                if (_answer.charAt(i-1) == '1') {

                    String question = "Question №" + countQuestion.toString();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("surveys")
                            .child(describe)
                            .child(question)
                            .child("Answers")
                            .child("Answer №" + i.toString())
                            .child("quantity");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer count = snapshot.getValue(Integer.class);
                            reference.setValue(count + 1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

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
                            users.add(nameUser);
                            reference2.setValue(users);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            countQuestion++;
        }

        // Обработчик нажатия кнопки, которая возвращает на главный экран
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment fragment = new MainFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }
}