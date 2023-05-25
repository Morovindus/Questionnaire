package com.example.questionnaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.questionnaire.databinding.FragmentUploadBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UploadFragment extends Fragment {

    private static FragmentUploadBinding binding;
    Integer flag = 0;
    String nameUser;
    EditText uploadTopic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentUploadBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        uploadTopic = binding.uploadTopic;
        nameUser = ((MainActivity)getActivity()).name;

        binding.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
                if (flag == 1)
                    return;

                Bundle bundle = new Bundle();
                bundle.putString("title", uploadTopic.getText().toString());
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                QuestionCreatorFragment fragment = new QuestionCreatorFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }

    public void saveData(){

        String title = uploadTopic.getText().toString();

        if (!validateData())
            return;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("surveys");

        ArrayList<String> users = new ArrayList<String>();

        users.add("0");

        HelperQuiz helperClass = new HelperQuiz(title, users, nameUser);
        reference.child(title).setValue(helperClass);

    }

    public Boolean validateData(){
        String val = uploadTopic.getText().toString();
        if (val.isEmpty()){
            uploadTopic.setError("Введите название опроса");
            flag = 1;
            return false;
        } else {
            uploadTopic.setError(null);
            flag = 0;
            return true;
        }
    }
}