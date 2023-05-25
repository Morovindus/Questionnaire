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
import android.widget.Toast;

import com.example.questionnaire.databinding.ActivitySignupBinding;
import com.example.questionnaire.databinding.FragmentMainBinding;
import com.example.questionnaire.databinding.FragmentSignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignupFragment extends Fragment {

    private static FragmentSignupBinding binding;
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    String name, email, username, password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Surveys> surveys;

        super.onCreate(savedInstanceState);

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        signupName = binding.signupName;
        signupEmail = binding.signupEmail;
        signupUsername = binding.signupUsername;
        signupPassword = binding.signupPassword;
        signupButton = binding.signupButton;
        loginRedirectText = binding.loginRedirectText;

        //ArrayList<String> moderators = new ArrayList<String>();
        //moderators.add("admin");
        //DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("moderators");
        //Helper helper = new Helper(moderators);
        //reference4.setValue(helper);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                name = signupName.getText().toString();
                email = signupEmail.getText().toString();
                username = signupUsername.getText().toString();
                password = signupPassword.getText().toString();

                if (!validateUsername() | !validatePassword() | !validateName() | !validateEmail()){
                } else{
                    checkUser();
                }
            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                //startActivity(intent);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                LoginFragment fragment = new LoginFragment();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }

    public void checkUser(){
        String userUsername = signupUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    signupUsername.setError("Пользователь с таким именем уже существует");
                    signupUsername.requestFocus();
                }

                else{
                    HelperRegistration helperClass = new HelperRegistration(name, email, username, password);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(getActivity(), "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    LoginFragment fragment = new LoginFragment();
                    ft.replace(R.id.frameLayout, fragment);
                    ft.commit();
                    //Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    //startActivity(intent);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public Boolean validateName(){
        String val = signupName.getText().toString();
        if (val.isEmpty()){
            signupName.setError("Введите свое имя");
            return false;
        } else {
            signupName.setError(null);
            return true;
        }
    }

    public Boolean validateEmail(){
        String val = signupEmail.getText().toString();
        if (val.isEmpty()){
            signupEmail.setError("Введите адрес электронной почты");
            return false;
        } else {
            signupEmail.setError(null);
            return true;
        }
    }

    public Boolean validateUsername(){
        String val = signupUsername.getText().toString();
        if (val.isEmpty()){
            signupUsername.setError("Введите имя пользователя");
            return false;
        } else {
            signupUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = signupPassword.getText().toString();
        if (val.isEmpty()){
            signupPassword.setError("Введите пароль");
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }
    }
}