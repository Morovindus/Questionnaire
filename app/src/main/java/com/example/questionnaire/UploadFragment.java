package com.example.questionnaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentUploadBinding;

// Фрагмент, на котором пользователь вводит название опроса
public class UploadFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentUploadBinding binding = FragmentUploadBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Обработчик нажатия на кнопку ввода
        binding.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData(binding))
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

    // Проверка, что пользователь заполнил поле названия опроса
    public Boolean validateData(FragmentUploadBinding binding){
        String val = binding.uploadTopic.getText().toString();
        if (val.isEmpty()){
            binding.uploadTopic.setError("Введите название опроса");
            return false;
        } else {
            binding.uploadTopic.setError(null);
            return true;
        }
    }
}