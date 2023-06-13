package com.example.questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.questionnaire.databinding.FooterBinding;
import com.example.questionnaire.databinding.FragmentAnswersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Класс, блягодаря которому пользователь, вносит ответы
public class AnswersFragment extends Fragment {
    ArrayList<Answer> answers;
    String describe, question;
    private FragmentAnswersBinding binding;
    private static FooterBinding binding_footer;
    Integer countQuestion;
    Boolean flagSelection = false;
    ArrayList<String> _answers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentAnswersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        _answers = new ArrayList<>();

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding_footer = FooterBinding.inflate(getLayoutInflater());

        Button buttonNew = binding_footer.createButton;
        buttonNew.setText("Следующий вопрос");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
        }

        answers = new ArrayList<Answer>();

        countQuestion = 1;
        question = "Question №" + countQuestion.toString();

        checkQuestion(question);


        // Слушатель кнопки добавление нового вопроса
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = "";
                for (Answer p : answers) {
                    if (p.box) {
                        answer += "1";
                    }
                    else {
                        answer += "0";
                    }
                }
                _answers.add(answer);
                countQuestion++;
                question = "Question №" + countQuestion.toString();
                checkQuestion(question);
            }
        });

        return view;
    }

    // Проверка что в базе данных есть еще один вопрос
    public void checkQuestion(String _question){

        Query checkUserDatabase = FirebaseDatabase.getInstance().getReference("surveys")
                .child(describe)
                .child(_question);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("describe").exists()) {

                    String post = snapshot.child("describe").getValue(String.class);
                    binding.question.setText(post);

                    answers = new ArrayList<Answer>();

                    ListView lvMain = binding.lvMain;
                    BoxAdapterAnswer adapter = new BoxAdapterAnswer(getActivity(), answers);;
                    BoxAdapterAnswersSingle adapterSingle =  new BoxAdapterAnswersSingle(getActivity(), answers);

                    answers.clear();
                    for (DataSnapshot itemSnapshot: snapshot
                            .child("Answers")
                            .getChildren()){
                        String dataClass = itemSnapshot.child("answer").getValue(String.class);
                        Answer helper = new Answer(dataClass, false);
                        answers.add(helper);
                    }

                    flagSelection = snapshot.child("selection").getValue(Boolean.class);
                    if (!flagSelection){
                        adapter.notifyDataSetChanged();
                        lvMain.addFooterView(binding_footer.getRoot());
                        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        lvMain.setAdapter(adapter);
                    } else {
                        adapterSingle.notifyDataSetChanged();
                        lvMain.addFooterView(binding_footer.getRoot());
                        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        lvMain.setAdapter(adapterSingle);

                        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView <? > parent, View view, int position, long id)
                            {
                                adapterSingle.setSelectedIndex(position);
                                adapterSingle.notifyDataSetChanged();
                            }
                        });
                    }


                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("describe", describe);
                    bundle.putStringArrayList("answers", _answers);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    FinalFragment fragment = new FinalFragment();
                    fragment.setArguments(bundle);
                    ft.replace(R.id.frameLayout, fragment);
                    ft.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}