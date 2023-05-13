package com.example.questionnaire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questionnaire.databinding.ActivitySignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignupActivity extends AppCompatActivity {

    private static ActivitySignupBinding binding_signup;
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    String name, email, username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_signup = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding_signup.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        signupName = binding_signup.signupName;
        signupEmail = binding_signup.signupEmail;
        signupUsername = binding_signup.signupUsername;
        signupPassword = binding_signup.signupPassword;
        signupButton = binding_signup.signupButton;
        loginRedirectText = binding_signup.loginRedirectText;

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
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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
                    HelperClass helperClass = new HelperClass(name, email, username, password);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(SignupActivity.this, "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
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