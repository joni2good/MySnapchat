package com.example.mysnapchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mysnapchat.R;
import com.example.mysnapchat.models.MyImage;

import java.util.List;

public class Adapter extends BaseAdapter {
    private List<MyImage> items;
    private LayoutInflater layoutInflater;

    public Adapter(List<MyImage> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null){
            view = layoutInflater.inflate(R.layout.myrow, null);
        }
        ConstraintLayout constraintLayout = (ConstraintLayout) view;
        TextView userText = view.findViewById((R.id.userText));
        TextView textView = view.findViewById(R.id.textView);
//        TextView textView1 = view.findViewById(R.id.content);
        if (userText != null) {
            userText.setText(items.get(position).getUser());
        }
        if (textView != null){
            textView.setText(items.get(position).getText());
        }
        return constraintLayout;
    }
}
