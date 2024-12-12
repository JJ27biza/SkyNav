package com.example.skynav.Adaptadores;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.skynav.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mItems;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mItems = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextColor(ContextCompat.getColor(mContext, R.color.white)); // Cambia el color del texto aquí
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextColor(ContextCompat.getColor(mContext, R.color.black)); // Cambia el color del texto aquí
        return view;
    }
}
