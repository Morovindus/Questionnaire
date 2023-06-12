package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.questionnaire.databinding.FragmentLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;

// Фрагменты, предоставляющий пользователю возможность авторизации
public class LoginFragment extends Fragment {
    private static FragmentLoginBinding binding;
    EditText loginUsername, loginPassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loginUsername = binding.loginUsername;
        loginPassword = binding.loginPassword;

        // Обработчик нажатия кнопки, отвечающий за вход
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()){

                } else {
                    try {
                        checkUser();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        // Обработчик нажатия кнопки, отвечающий переход на экран регистрации
        binding.signupRedictText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                SignupFragment fragment = new SignupFragment();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });

        return view;
    }

    // Проверка, что пользователь не оставил пустым поля для логина
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

    // Проверка, что пользователь не оставил пустым поля для пароля
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

    // Проверка, что введеные данные пользователем верны
    public void checkUser() throws UnsupportedEncodingException {
        String userUsername = loginUsername.getText().toString().trim();
        String _userPassword = loginPassword.getText().toString().trim();
        String userPassword = Encode(_userPassword);

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

    private static String Encode(String s) throws UnsupportedEncodingException {
        byte[] bytes = s.getBytes("UTF-8");

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%s0x", b));
        }

        return sb.toString();
    }
}