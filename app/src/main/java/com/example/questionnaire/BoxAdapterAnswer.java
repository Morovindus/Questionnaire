package com.example.questionnaire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.questionnaire.databinding.ItemAnswersBinding;

import java.util.ArrayList;

public class BoxAdapterAnswer extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Answer> objects;
    BoxAdapterAnswer(Context context, ArrayList<Answer> surveys) {
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
        ItemAnswersBinding binding = ItemAnswersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Answer p = getAnswer(position);
        binding.tvDescribe.setText(p.answer.toString());

        CheckBox cbBuy = binding.cbBox;

        cbBuy.setOnCheckedChangeListener(myCheckChangeList);
        cbBuy.setTag(position);
        cbBuy.setChecked(p.box);
        return binding.getRoot();
    }
    Answer getAnswer(int position){
        return ((Answer) getItem(position));
    }

    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
            getAnswer((Integer) buttonView.getTag()).box = isChecked;
        }
    };
}