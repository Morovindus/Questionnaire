package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.questionnaire.databinding.ActivityMainBinding;

// Главный экран приложения
public class MainActivity extends AppCompatActivity {
    String name;
    MainFragment mainFragment;
    EndPoolFragment endFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding_main = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding_main.getRoot();
        setContentView(view);

        Intent intent2 = getIntent();
        name = intent2.getStringExtra("name");

        mainFragment = new MainFragment();
        endFragment = new EndPoolFragment();
        setNewFragment(mainFragment);
    }


    // Метод установки основного фрагмента
    public void setNewFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        ft.commit();
    }

    // Метод, в котором указывается файл разметки меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Метод, который обрабатывает нажатия на кнопки меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        int id = item.getItemId();
        if (id == R.id.item1)
        {
            InstructionFragment instructionFragment = new InstructionFragment();
            setNewFragment(instructionFragment);
        } else if (id == R.id.item2)
        {
            ReferenceFragment referenceFragment = new ReferenceFragment();
            setNewFragment(referenceFragment);
        }  else if (id == R.id.item3)
        {
            AuthorFragment authorFragment = new AuthorFragment();
            setNewFragment(authorFragment);
        } else if (id == R.id.item4)
        {
            intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        } else if (id == android.R.id.home){
            setNewFragment(mainFragment);
        }

        return super.onOptionsItemSelected(item);
    }
}