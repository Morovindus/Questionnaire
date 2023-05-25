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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.questionnaire.databinding.FragmentLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private static FragmentLoginBinding binding;
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loginUsername = binding.loginUsername;
        loginPassword = binding.loginPassword;
        signupRedirectText = binding.signupRedictText;
        loginButton = binding.loginButton;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()){

                } else {
                    checkUser();;
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                SignupFragment fragment = new SignupFragment();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
                //Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                //startActivity(intent);
            }
        });

        return view;
    }

    public Boolean validateUsername(){
        String val = loginUsername.getText().toString();
        if (val.isEmpty()){
            loginUsername.setError("Поле пользователя не может быть пустым");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Поле пароля не может быть пустым");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        loginUsername.setError(null);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("name", userUsername);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Неправильный пароль");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("Пользователь с таким именем не найден");
                    loginUsername.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}