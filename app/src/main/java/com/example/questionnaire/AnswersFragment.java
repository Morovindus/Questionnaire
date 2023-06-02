package com.example.questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnswersFragment extends Fragment {
    ArrayList<Answer> answers;
    ListView lvMain;
    FirebaseDatabase database;
    String describe, question;
    String nameUser;
    private FragmentAnswersBinding binding;
    private static FooterBinding binding_footer;
    Integer countQuestion;
    Boolean flagSelection = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentAnswersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding_footer = FooterBinding.inflate(getLayoutInflater());

        Button buttonNew = binding_footer.createButton;
        buttonNew.setText("Следующий вопрос");

        nameUser = ((MainActivity)getActivity()).name;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
        }

        answers = new ArrayList<Answer>();

        countQuestion = 1;
        question = "Question №" + countQuestion.toString();

        checkQuestion(question);

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

        return view;
    }

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
                    lvMain = binding.lvMain;
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