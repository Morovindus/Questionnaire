package com.example.questionnaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.questionnaire.databinding.FragmentEndPoolBinding;

// Фрагмент, который уведомляет пользователя о намерении просмотра результатов
public class EndPoolFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FragmentEndPoolBinding binding = FragmentEndPoolBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String describe = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
        }

        // Обработчик нажатия кнопки, который переносит пользователя на фрагмент с результатами
        String finalDescribe = describe;
        binding.buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("describe", finalDescribe);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ShowResultFragment fragment = new ShowResultFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }
}