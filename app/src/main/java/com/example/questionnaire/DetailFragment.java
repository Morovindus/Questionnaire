package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.questionnaire.databinding.FragmentDetailBinding;
import com.example.questionnaire.databinding.FragmentEndPoolBinding;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    String describe, nameUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        nameUser = ((MainActivity)getActivity()).name;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
        }
        TextView detailDesc = binding.detailDesc;
        Button buttonEnter = binding.buttonEnter;
        Button buttonBack = binding.buttonBack;


        detailDesc.setText(describe);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment fragment = new MainFragment();

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

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

                //Intent intent = new Intent(getActivity(), AnswersActivity.class);
                //intent.putExtra("describe", describe);
                //intent.putExtra("name", nameUser);
                //startActivity(intent);
            }
        });

        return view;
    }
}