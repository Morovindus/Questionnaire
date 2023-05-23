package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.questionnaire.databinding.ActivityUploadBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    private static ActivityUploadBinding binding_upload;
    Integer flag = 0;
    String name;
    EditText uploadTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_upload = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding_upload.getRoot();
        setContentView(view);

        uploadTopic = binding_upload.uploadTopic;
        Button enterButton = binding_upload.enterButton;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();
        name = intent2.getStringExtra("name");

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                if (flag == 1)
                    return;

                Intent intent = new Intent(UploadActivity.this, QuestionCreator.class);
                intent.putExtra("name", name);
                intent.putExtra("title", uploadTopic.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void saveData(){

        String title = uploadTopic.getText().toString();

        if (!validateData())
            return;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("surveys");

        ArrayList<String> users = new ArrayList<String>();

        users.add("0");

        HelperClass2 helperClass = new HelperClass2(title, users, name);
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