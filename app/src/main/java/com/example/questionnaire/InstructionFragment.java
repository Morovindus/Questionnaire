package com.example.questionnaire;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentInstructionBinding;

// Фрагмент демонстрирующий руководство пользования
public class InstructionFragment extends Fragment {
    private FragmentInstructionBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentInstructionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        return view;
    }
}