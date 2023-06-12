package com.example.questionnaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.questionnaire.databinding.FragmentQuestionCreatorBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// Фрагмент в котором пользователь создает вопросы
public class QuestionCreatorFragment extends Fragment implements
        CompoundButton.OnCheckedChangeListener{

    private List<View> allEds;
    private Integer counter = 0;
    private Integer questions = 0;
    private Integer counterQuestions = 1;
    String title, nameUser;
    Boolean flagSelection = false;
    private FragmentQuestionCreatorBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentQuestionCreatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button createButton = binding.createButton;
        Button deleteButton = binding.deleteButton;
        Button newButton = binding.buttonNew;
        Button exitButton = binding.buttonExit;
        Button saveButton = binding.buttonSave;

        Switch switch_button = binding.switchButton;
        switch_button.setOnCheckedChangeListener(this);

        // Получаем значение логина пользователя
        nameUser = ((MainActivity)getActivity()).name;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
        }

        allEds = new ArrayList<View>();

        final LinearLayout linear = binding.linear;

        for(int i = 0; i < 2; i++) {
            counter++;
            final View view2 = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
            EditText text = (EditText) view2.findViewById(R.id.editText);
            text.setHint("Ответ №" + counter);
            allEds.add(view2);
            linear.addView(view2);
        }

        // Обработчик нажатия на кнопку выхода
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);

                builder
                        .setMessage("Вы уверены что хотите выйти?\n\nНесохраненные данные будут потеряны")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("name", nameUser);
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Обработчик нажатия на кнопку, добавления нового ответа
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
                EditText text = (EditText) view.findViewById(R.id.editText);
                text.setHint("Ответ №" + counter);
                allEds.add(view);
                linear.addView(view);
            }
        });

        // Обработчик нажатия на кнопку, удаления нового ответа
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 2) {
                    try {
                        counter -= 1;
                        linear.removeViewAt(allEds.size() - 1);
                        allEds.remove(allEds.size()-1);
                    } catch (IndexOutOfBoundsException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Обработчик нажатия на кнопку, создания нового вопроса
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer flag = 0;
                String[] items = new String[allEds.size()];
                if (questions == 0) {
                    for (int i = 0; i < allEds.size(); i++) {
                        if (((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString().isEmpty()) {
                            ((EditText) allEds.get(i).findViewById(R.id.editText)).setError("Введите ответ");
                            flag = 1;
                        }
                        items[i] = ((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString();
                    }

                    if (!validateData())
                        return;
                    if (flag == 1)
                        return;

                    String question = "Question №" + counterQuestions;
                    counterQuestions++;

                    HelperQuestion helperQuestion = new HelperQuestion(binding.question.getText().toString(), flagSelection);

                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("surveys").child(title);

                    reference3.child(question).setValue(helperQuestion);

                    for (int i = 0; i < counter; i++) {
                        HelperAnswers helperAnswers = new HelperAnswers(items[i], 0);

                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("surveys")
                                .child(title)
                                .child(question)
                                .child("Answers");

                        reference5.child("Answer №" + String.valueOf(i + 1)).setValue(helperAnswers);
                    }
                    Toast.makeText(getActivity(), "Успешно сохранено", Toast.LENGTH_LONG).show();
                } else
                    counterQuestions++;

                questions = 0;
                binding.question.setText("");
                linear.removeAllViews();
                allEds.clear();
                items = null;
                counter = 0;

                for(int i = 0; i < 2; i++) {
                    counter++;
                    final View view2 = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
                    EditText text = (EditText) view2.findViewById(R.id.editText);
                    text.setHint("Ответ №" + counter);
                    allEds.add(view2);
                    linear.addView(view2);
                }
            }
        });

        // Обработчик нажатия на кнопку сохранения
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer flag = 0;
                String [] items = new String[allEds.size()];
                for(int i=0; i < allEds.size(); i++) {
                    if (((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString().isEmpty()) {
                        ((EditText) allEds.get(i).findViewById(R.id.editText)).setError("Введите ответ");
                        flag = 1;
                    }
                    items[i] = ((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString();
                }

                if (!validateData())
                    return;
                if (flag == 1)
                    return;

                String question = "Question №" + counterQuestions;

                HelperQuestion helperQuestion = new HelperQuestion(binding.question.getText().toString(), flagSelection);

                DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("surveys").child(title);

                reference3.child(question).setValue(helperQuestion);

                for (int i = 0;i < counter;i++) {
                    HelperAnswers helperAnswers = new HelperAnswers(items[i], 0);

                    DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("surveys")
                            .child(title)
                            .child(question)
                            .child("Answers");

                    reference5.child("Answer №" + String.valueOf(i+1)).setValue(helperAnswers);
                }

                Toast.makeText(getActivity(), "Успешно сохранено", Toast.LENGTH_LONG).show();
                questions = 1;
            }
        });
        return view;
    }

    // Проверка, что пользователь заполнил поле вопроса
    public Boolean validateData(){
        String val = binding.question.getText().toString();
        if (val.isEmpty()){
            binding.question.setError("Введите вопрос");
            return false;
        } else {
            binding.question.setError(null);
            return true;
        }
    }

    // Обработчик нажатия на переключатель
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked){
            Toast.makeText(getActivity(), "Множественный", Toast.LENGTH_LONG).show();
            flagSelection = false;
        } else {
            Toast.makeText(getActivity(), "Одиночный", Toast.LENGTH_LONG).show();
            flagSelection = true;
        }
    }
}