package com.example.questionnaire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.example.questionnaire.databinding.ItemAnswersSingleBinding;

import java.util.ArrayList;

public class BoxAdapterAnswersSingle extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Answer> objects;
    int selectedIndex = -1;
    BoxAdapterAnswersSingle(Context context, ArrayList<Answer> surveys) {
        ctx = context;
        objects = surveys;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemAnswersSingleBinding binding = ItemAnswersSingleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Answer p = getAnswer(position);
        binding.tvDescribe.setText(p.answer.toString());

        RadioButton rbSelect = binding.cbBox;

        if(selectedIndex == position){
            rbSelect.setChecked(true);
            p.box = true;
        }
        else{
            rbSelect.setChecked(false);
            p.box = false;
        }
        return binding.getRoot();
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    Answer getAnswer(int position){
        return ((Answer) getItem(position));
    }

}
