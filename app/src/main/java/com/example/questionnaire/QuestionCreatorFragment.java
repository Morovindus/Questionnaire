package com.example.questionnaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    String title, nameUser;
    Boolean flagSelection = false;
    private FragmentQuestionCreatorBinding binding;
    Quiz quiz;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = FragmentQuestionCreatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        quiz = new Quiz();

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.switchButton.setOnCheckedChangeListener(this);

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
        binding.buttonExit.setOnClickListener(new View.OnClickListener() {
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
        binding.createButton.setOnClickListener(new View.OnClickListener() {
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
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
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
        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] flag = new int[1];
                String [] items = checkItems(flag);

                if (!validateData())
                    return;
                if (flag[0] == 1)
                    return;

                quiz.flagSelection.add(flagSelection);
                quiz.nameQuestion.add(binding.question.getText().toString());

                for (int i = 0;i < counter;i++) {
                    quiz.answers.add(items[i]);
                }
                quiz.countAnswers.add(counter);

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
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int [] flag = new int[1];
                String [] items = checkItems(flag);

                if (!validateData())
                    return;
                if (flag[0] == 1)
                    return;

                quiz.flagSelection.add(flagSelection);
                quiz.nameQuestion.add(binding.question.getText().toString());

                for (int i = 0;i < counter;i++) {
                    quiz.answers.add(items[i]);
                }
                quiz.countAnswers.add(counter);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("surveys");
                ArrayList<String> users = new ArrayList<String>();
                users.add("0");
                HelperQuiz helperClass = new HelperQuiz(title, users, nameUser);
                reference.child(title).setValue(helperClass);

                int i = 0;
                int _counter = 0;

                for (String _question : quiz.nameQuestion) {
                    String question = "Question №" + String.valueOf(i+1);
                    HelperQuestion helperQuestion = new HelperQuestion(_question, quiz.flagSelection.get(i));
                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("surveys").child(title);
                    reference3.child(question).setValue(helperQuestion);


                    for (int j = 0; j < quiz.countAnswers.get(i); j++) {
                        HelperAnswers helperAnswers = new HelperAnswers(quiz.answers.get(_counter), 0);
                        DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("surveys")
                                .child(title)
                                .child(question)
                                .child("Answers");

                        reference5.child("Answer №" + String.valueOf(j+1)).setValue(helperAnswers);

                        Log.d("myLogs", "Название ответ " + quiz.answers.get(_counter));
                        _counter++;
                    }
                    i++;
                }


                Toast.makeText(getActivity(), "Успешно сохранено", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    // Проверка, что пользователь заполнил все поля ответов
    public String[] checkItems(int [] flag){

        String [] items = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++) {
            if (((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString().isEmpty()) {
                ((EditText) allEds.get(i).findViewById(R.id.editText)).setError("Введите ответ");
                flag[0] = 1;
            }
            items[i] = ((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString();
        }

        return items;

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