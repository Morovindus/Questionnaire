package com.example.questionnaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentReferenceBinding;

public class ReferenceFragment extends Fragment {

    private FragmentReferenceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentReferenceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
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