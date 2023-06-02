package com.example.questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.questionnaire.databinding.FragmentSignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignupFragment extends Fragment {

    private static FragmentSignupBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.signupName.getText().toString();
                String email = binding.signupEmail.getText().toString();
                String username = binding.signupUsername.getText().toString();
                String password = binding.signupPassword.getText().toString();

                if (!validateUsername(username) | !validatePassword(password) | !validateName(name) | !validateEmail(email)){
                } else{
                    checkUser(username, email, name, password);
                }
            }
        });


        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                LoginFragment fragment = new LoginFragment();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }

    public void checkUser(String username, String email, String name, String password){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.signupUsername.setError("Пользователь с таким именем уже существует");
                    binding.signupUsername.requestFocus();
                }

                else{
                    HelperRegistration helperClass = new HelperRegistration(name, email, username, password);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(getActivity(), "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    LoginFragment fragment = new LoginFragment();
                    ft.replace(R.id.frameLayout, fragment);
                    ft.commit();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public Boolean validateName(String val){
        if (val.isEmpty()){
            binding.signupName.setError("Введите свое имя");
            return false;
        } else {
            binding.signupName.setError(null);
            return true;
        }
    }

    public Boolean validateEmail(String val){
        if (val.isEmpty()){
            binding.signupEmail.setError("Введите адрес электронной почты");
            return false;
        } else {
            binding.signupEmail.setError(null);
            return true;
        }
    }

    public Boolean validateUsername(String val){
        if (val.isEmpty()){
            binding.signupUsername.setError("Введите имя пользователя");
            return false;
        } else {
            binding.signupUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(String val){
        if (val.isEmpty()){
            binding.signupPassword.setError("Введите пароль");
            return false;
        } else {
            binding.signupPassword.setError(null);
            return true;
        }
    }
}