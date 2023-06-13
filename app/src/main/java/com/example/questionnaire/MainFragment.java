package com.example.questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.questionnaire.databinding.FooterBinding;
import com.example.questionnaire.databinding.FragmentMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

// Основной фрагмент, в котором расположен список со всеми опросами
public class MainFragment extends Fragment {
    String nameUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Surveys> surveys;

        super.onCreate(savedInstanceState);

        FragmentMainBinding binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FooterBinding binding_footer = FooterBinding.inflate(getLayoutInflater());

        nameUser = ((MainActivity)getActivity()).name;

        surveys = new ArrayList<Surveys>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("surveys");

        BoxAdapter adapter = new BoxAdapter(getActivity(), surveys);

        // Получаем значения полей опросов и составляем список
        ValueEventListener eventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    surveys.clear();

                    for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                        String dataClass = itemSnapshot.child("describe").getValue(String.class);
                        Integer check = 0;

                        ArrayList<String> users = new ArrayList<String>();

                        String creator = itemSnapshot.child("creator").getValue(String.class);

                        if (Objects.equals(nameUser, creator)){
                            check = 1;
                        } else {
                            for (DataSnapshot postSnapshot : itemSnapshot.child("users").getChildren()) {
                                String post = postSnapshot.getValue(String.class);
                                users.add(post);
                            }

                            for (String w : users) {
                                if (Objects.equals(w, nameUser)) {
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

            ListView lvMain = binding.lvMain;
            lvMain.addFooterView(binding_footer.getRoot());

            lvMain.setAdapter(adapter);

            // Слушатель нажатия на список
            lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Integer checkQuiz = surveys.get(position).check;
                    String describeQuestion = surveys.get(position).describe;

                    Bundle bundle = new Bundle();
                    bundle.putString("describe", describeQuestion);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();

                    if (checkQuiz == 2 || checkQuiz == 1){

                        EndPoolFragment fragment = new EndPoolFragment();
                        fragment.setArguments(bundle);
                        ft.replace(R.id.frameLayout, fragment);

                    } else {

                        DetailFragment fragment = new DetailFragment();
                        fragment.setArguments(bundle);
                        ft.replace(R.id.frameLayout, fragment);
                    }
                    ft.commit();
                }
            });

            // Обработчик нажатия на кнопку "Создать новый опрос"
            binding_footer.createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    UploadFragment fragment = new UploadFragment();
                    ft.replace(R.id.frameLayout, fragment);
                    ft.commit();
                }
            });
        return view;
    }
}