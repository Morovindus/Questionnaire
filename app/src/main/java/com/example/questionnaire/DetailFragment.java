package com.example.questionnaire;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.questionnaire.databinding.FragmentDetailBinding;

// Фрагмент, который оповещает пользователя о намерении пройти опрос
public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    String describe, nameUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameUser = ((MainActivity)getActivity()).name;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
        }
        TextView detailDesc = binding.detailDesc;
        Button buttonEnter = binding.buttonEnter;

        detailDesc.setText(describe);

        // Обработчик нажатия кнопки вперед
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("describe", describe);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                AnswersFragment fragment = new AnswersFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });
        return view;
    }
}