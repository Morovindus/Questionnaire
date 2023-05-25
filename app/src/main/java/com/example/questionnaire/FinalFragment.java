package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.questionnaire.databinding.FragmentFinalBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FinalFragment extends Fragment {

    String nameUser, describe;
    private FragmentFinalBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentFinalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        nameUser = ((MainActivity)getActivity()).name;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            describe = bundle.getString("describe");
        }

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference2 = database.getReference("surveys")
                        .child(describe)
                        .child("users");

                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> users = new ArrayList<String>();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            String post = postSnapshot.getValue(String.class);
                            users.add(post);
                        }
                        users.add(nameUser);
                        reference2.setValue(users);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                MainFragment fragment = new MainFragment();

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();

                //Intent intent = new Intent(FinalActivity.this, MainActivity.class);
                //startActivity(intent);

                //startActivityAfterCleanup(MainActivity.class);
            }
        });

        return view;
    }

}