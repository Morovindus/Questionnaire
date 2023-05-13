package com.example.questionnaire;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.questionnaire.databinding.ActivityUploadBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    private static ActivityUploadBinding binding_upload;
    Button saveButton, enterButton;
    Integer flag = 0;
    EditText uploadTopic;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_upload = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding_upload.getRoot();
        setContentView(view);

        uploadTopic = binding_upload.uploadTopic;
        enterButton = binding_upload.enterButton;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();
        String name = intent2.getStringExtra("name");

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

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("surveys");

        ArrayList<String> users = new ArrayList<String>();

        users.add("Admin");

        HelperClass2 helperClass = new HelperClass2(title, users);
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