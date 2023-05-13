package com.example.questionnaire;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import com.example.questionnaire.databinding.ItemBinding;

import java.util.ArrayList;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Surveys> objects;
    BoxAdapter(Context context, ArrayList<Surveys> surveys) {
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemBinding binding = ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Surveys p = getSurveys(position);
        binding.tvDescribe.setText(p.describe);
        if (p.check){
            binding.lineCard.setCardBackgroundColor(Color.rgb(57,255,20));
        } else
            binding.lineCard.setCardBackgroundColor(Color.rgb(134,146,247));
        //binding.ivImage.setImageResource(p.image);
        return binding.getRoot();
    }

    Surveys getSurveys(int position){
        return ((Surveys) getItem(position));
    }

}