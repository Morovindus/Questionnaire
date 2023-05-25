package com.example.questionnaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentAuthorBinding;
import com.example.questionnaire.databinding.FragmentInstructionBinding;

public class InstructionFragment extends Fragment {

    private FragmentInstructionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentInstructionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}