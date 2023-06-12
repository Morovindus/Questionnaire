package com.example.questionnaire;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentUploadBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

// Фрагмент, на котором пользователь вводит название опроса
public class UploadFragment extends Fragment {

    private static FragmentUploadBinding binding;
    Integer flag = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentUploadBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String nameUser = ((MainActivity)getActivity()).name;

        // Обработчик нажатия на кнопку ввода
        binding.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData(nameUser);
                if (flag == 1)
                    return;

                Bundle bundle = new Bundle();
                bundle.putString("title", binding.uploadTopic.getText().toString());
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                QuestionCreatorFragment fragment = new QuestionCreatorFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }

    // Метод сохранения названия опроса
    public void saveData(String nameUser){

        String title = binding.uploadTopic.getText().toString();

        if (!validateData())
            return;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("surveys");

        ArrayList<String> users = new ArrayList<String>();

        users.add("0");

        HelperQuiz helperClass = new HelperQuiz(title, users, nameUser);
        reference.child(title).setValue(helperClass);

    }

    // Проверка, что пользователь заполнил поле названия опроса
    public Boolean validateData(){
        String val = binding.uploadTopic.getText().toString();
        if (val.isEmpty()){
            binding.uploadTopic.setError("Введите название опроса");
            flag = 1;
            return false;
        } else {
            binding.uploadTopic.setError(null);
            flag = 0;
            return true;
        }
    }
}